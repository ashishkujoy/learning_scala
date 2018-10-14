package com.thoughtworks.streams.actors
import akka.actor.{Actor, ActorRef, Cancellable, Props, Terminated}
import com.thoughtworks.streams.actors.Device.RespondTemperature
import com.thoughtworks.streams.actors.DeviceGroup._

import scala.concurrent.duration.FiniteDuration

object DeviceGroupQuery {
  case object CollectionTimeout

  def props(
      actorToDeviceId: Map[ActorRef, String],
      requestId: Long,
      requester: ActorRef,
      timeout: FiniteDuration
  ): Props = {
    Props(new DeviceGroupQuery(actorToDeviceId, requestId, requester, timeout))
  }
}

class DeviceGroupQuery(actorToDeviceId: Map[ActorRef, String], requestId: Long, requester: ActorRef, timeout: FiniteDuration)
    extends Actor {
  import DeviceGroupQuery._
  import context.dispatcher
  val queryTimeoutTimer: Cancellable = context.system.scheduler.scheduleOnce(timeout, self, CollectionTimeout)

  override def preStart(): Unit = {
    actorToDeviceId.keysIterator.foreach { deviceActor =>
      context.watch(deviceActor)
      deviceActor ! Device.ReadTemperature(0)
    }
  }

  override def postStop(): Unit = {
    queryTimeoutTimer.cancel()
  }

  override def receive: Receive = waitingForReplies(Map.empty, actorToDeviceId.keySet)

  def waitingForReplies(
      repliesSoFar: Map[String, TemperatureReading],
      stillWaiting: Set[ActorRef]
  ): Receive = {

    case RespondTemperature(0, valueOption) => recordDeviceTemperatureResponse(repliesSoFar, stillWaiting, valueOption)
    case Terminated(deviceActor)            => receivedResponse(deviceActor, DeviceNotAvailable, stillWaiting, repliesSoFar)
    case CollectionTimeout                  => sendCollectionTimeOutResponse(repliesSoFar, stillWaiting)
  }

  private def recordDeviceTemperatureResponse(
      repliesSoFar: Map[String, TemperatureReading],
      stillWaiting: Set[ActorRef],
      valueOption: Option[Double]
  ): Unit = {
    val deviceActor = sender()
    val reading = valueOption match {
      case Some(value) => Temperature(value)
      case None        => TemperatureNotAvailable
    }
    receivedResponse(deviceActor, reading, stillWaiting, repliesSoFar)
  }

  private def sendCollectionTimeOutResponse(
      repliesSoFar: Map[String, TemperatureReading],
      stillWaiting: Set[ActorRef]
  ): Unit = {
    val timedOutReplies =
      stillWaiting.map { deviceActor =>
        val deviceId = actorToDeviceId(deviceActor)
        deviceId -> DeviceGroup.DeviceTimedOut
      }
    requester ! RespondAllTemperatures(requestId, repliesSoFar ++ timedOutReplies)
    context.stop(self)
  }

  private def receivedResponse(deviceActor: ActorRef,
                               readingRes: TemperatureReading,
                               remainingActors: Set[ActorRef],
                               repliesSoFar: Map[String, TemperatureReading]): Unit = {
    val deviceId           = actorToDeviceId(deviceActor)
    val newRepliesSoFar    = repliesSoFar.updated(deviceId, readingRes)
    val newRemainingActors = remainingActors - deviceActor
    context.unwatch(deviceActor)
    if (newRemainingActors.isEmpty) {
      requester ! DeviceGroup.RespondAllTemperatures(requestId, newRepliesSoFar)
    } else {
      context.become(waitingForReplies(newRepliesSoFar, newRemainingActors))
    }
  }
}

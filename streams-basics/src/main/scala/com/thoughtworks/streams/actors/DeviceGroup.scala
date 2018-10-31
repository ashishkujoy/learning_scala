package com.thoughtworks.streams.actors
import akka.actor.{Actor, ActorRef, Props, Terminated}
import com.thoughtworks.streams.actors.DeviceGroup.{ReplyDeviceList, RequestAllTemperatures, RequestDeviceList}
import com.thoughtworks.streams.actors.DeviceManager.RequestTrackDevice
import com.thoughtworks.ImplicitConverters._

import scala.concurrent.duration._

object DeviceGroup {
  def props(groupId: String): Props = Props(new DeviceGroup(groupId))

  final case class RequestDeviceList(requestId: Long)
  final case class ReplyDeviceList(requestId: Long, ids: Set[String])

  final case class RequestAllTemperatures(requestId: Long)
  final case class RespondAllTemperatures(requestId: Long, temperatures: Map[String, TemperatureReading])

  sealed trait TemperatureReading
  final case class Temperature(value: Double) extends TemperatureReading
  case object TemperatureNotAvailable         extends TemperatureReading
  case object DeviceNotAvailable              extends TemperatureReading
  case object DeviceTimedOut                  extends TemperatureReading
}

class DeviceGroup(groupId: String) extends Actor {
  private var deviceIdToActor = Map.empty[String, ActorRef]

  override def receive: Receive = {
    case trackMsg @ RequestTrackDevice(`groupId`, _)  => handleValidTrackDeviceRequest(trackMsg)
    case RequestTrackDevice(unKnownGroupId, deviceId) =>
    case Terminated(deviceActor)                      => removeDeadActor(deviceActor)
    case RequestDeviceList(requestId)                 => sender() ! ReplyDeviceList(requestId, deviceIdToActor.keys.toSet)
    case RequestAllTemperatures(requestId)            => queryAllDeviceActors(requestId)
  }

  private def queryAllDeviceActors(requestId: Long): ActorRef = {
    context.actorOf(
      DeviceGroupQuery.props(
        actorToDeviceId = deviceIdToActor.swap,
        requestId = requestId,
        requester = sender(),
        3.seconds
      )
    )
  }
  private def handleValidTrackDeviceRequest(trackMsg: RequestTrackDevice): Unit = {
    deviceIdToActor.get(trackMsg.deviceId) match {
      case Some(deviceActor) => deviceActor forward trackMsg
      case None              => createNewDeviceActor(trackMsg.deviceId) forward trackMsg
    }
  }

  private def removeDeadActor(deviceActor: ActorRef): Unit = {
    val mayBeDeviceId = deviceIdToActor.findKeyByValue(deviceActor)
    mayBeDeviceId.foreach(deviceIdToActor -= _)
  }

  private def createNewDeviceActor(deviceId: String): ActorRef = {
    val deviceActor = context.actorOf(Device.props(groupId, deviceId), s"device-$deviceId")
    deviceIdToActor += deviceId -> deviceActor
    context.watch(deviceActor)
  }
}

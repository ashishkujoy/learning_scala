package com.thoughtworks.streams.actors
import akka.actor.{ActorRef, ActorSystem, PoisonPill}
import akka.testkit.TestProbe
import com.thoughtworks.streams.actors.Device.{ReadTemperature, RespondTemperature}
import com.thoughtworks.streams.actors.DeviceGroup._
import com.thoughtworks.streams.actors.DeviceGroupQuery.CollectionTimeout

import scala.concurrent.duration._

class DeviceGroupQueryTest(_system: ActorSystem) extends ActorsTest(_system) {

  def this() = this(ActorSystem("DeviceTest"))

  private var requester: TestProbe = _
  private var device1: TestProbe   = _
  private var device2: TestProbe   = _
  private var queryActor: ActorRef = _

  private val queryRequestId = 1
  private val device1Id      = "device1"
  private val device2Id      = "device2"
  override protected def beforeEach(): Unit = {
    requester = TestProbe()
    device1 = TestProbe()
    device2 = TestProbe()

    queryActor = system.actorOf(
      DeviceGroupQuery.props(
        actorToDeviceId = Map(device1.ref -> device1Id, device2.ref -> device2Id),
        queryRequestId,
        requester = requester.ref,
        timeout = 3.seconds
      )
    )

  }
  override protected def afterAll(): Unit = system.terminate()

  "Query Actor" should {
    val tempRequestId = 0
    "give temperature of active device actors" in {
      device1.expectMsg(ReadTemperature(tempRequestId))
      device2.expectMsg(ReadTemperature(tempRequestId))

      queryActor.tell(RespondTemperature(tempRequestId, Some(1.0)), device1.ref)
      queryActor.tell(RespondTemperature(tempRequestId, Some(2.0)), device2.ref)

      requester.expectMsg(resTempForDevice1and2(Temperature(1.0), Temperature(2.0)))
    }

    "give TemperatureNotAvailable for device which has no temperature recording" in {
      device1.expectMsg(ReadTemperature(tempRequestId))
      device2.expectMsg(ReadTemperature(tempRequestId))

      queryActor.tell(RespondTemperature(tempRequestId, Some(1.0)), device1.ref)
      queryActor.tell(RespondTemperature(tempRequestId, None), device2.ref)

      requester.expectMsg(resTempForDevice1and2(Temperature(1.0), TemperatureNotAvailable))
    }

    "give DeviceNotAvailable if device shutdown before sending its temperature" in {
      device1.expectMsg(ReadTemperature(tempRequestId))
      device2.expectMsg(ReadTemperature(tempRequestId))

      device2.ref ! PoisonPill

      queryActor.tell(RespondTemperature(tempRequestId, Some(1.0)), device1.ref)

      requester.expectMsg(resTempForDevice1and2(Temperature(1.0), DeviceNotAvailable))
    }

    "give Temperature for a device which shutdown after giving it temperature recording" in {
      device1.expectMsg(ReadTemperature(tempRequestId))
      device2.expectMsg(ReadTemperature(tempRequestId))

      queryActor.tell(RespondTemperature(tempRequestId, Some(1.0)), device1.ref)
      queryActor.tell(RespondTemperature(tempRequestId, Some(2.0)), device2.ref)

      device2.ref ! PoisonPill

      DeviceNotAvailable
    }

    "give DeviceTimeOut for device which does not gives its temperature reading before query timeout" in {
      device1.expectMsg(ReadTemperature(tempRequestId))
      device2.expectMsg(ReadTemperature(tempRequestId))

      queryActor.tell(RespondTemperature(tempRequestId, Some(1.0)), device1.ref)
      queryActor.tell(CollectionTimeout, requester.ref)

      requester.expectMsg(resTempForDevice1and2(Temperature(1.0), DeviceTimedOut))
    }
  }

  private def resTempForDevice1and2(t1: TemperatureReading, t2: TemperatureReading): RespondAllTemperatures = {
    RespondAllTemperatures(
      queryRequestId,
      temperatures = Map(
        device1Id -> t1,
        device2Id -> t2
      )
    )
  }
}

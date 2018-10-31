package com.thoughtworks.streams.actors
import akka.actor.{ActorRef, ActorSystem}
import akka.testkit.TestProbe
import com.thoughtworks.streams.actors.Device.{ReadTemperature, RecordTemperature, RespondTemperature, TemperatureRecorded}
import com.thoughtworks.streams.actors.DeviceManager.{DeviceRegistered, RequestTrackDevice}

import scala.concurrent.duration._

class DeviceTest(_system: ActorSystem) extends ActorsTest(_system) {

  def this() = this(ActorSystem("DeviceTest"))

  private var deviceActor: ActorRef = _
  private var probe: TestProbe      = _

  override def afterAll: Unit = {
    shutdown(system)
  }

  override protected def beforeEach(): Unit = {
    probe = TestProbe()
    deviceActor = system.actorOf(Device.props("group", "device"))
  }

  "A Device" should {
    "reply with empty reading if no temperature is known" in {
      deviceActor.tell(ReadTemperature(requestId = 42), probe.ref)

      val response = probe.expectMsgType[RespondTemperature]

      response.requestId shouldBe 42
      response.value shouldBe None
    }

    "reply with temperature recorded after recording temperature" in {
      deviceActor.tell(RecordTemperature(1L, 21.0), probe.ref)

      val response = probe.expectMsgType[TemperatureRecorded]

      response.requestId shouldBe 1
    }

    "reply with latest known temperature readings" in {
      deviceActor.tell(RecordTemperature(requestId = 1, 24.0), probe.ref)
      probe.expectMsg(TemperatureRecorded(requestId = 1))

      deviceActor.tell(ReadTemperature(requestId = 2), probe.ref)
      val response1 = probe.expectMsgType[RespondTemperature]
      response1.requestId shouldBe 2
      response1.value shouldBe Some(24.0)

      deviceActor.tell(RecordTemperature(requestId = 3, 55.0), probe.ref)
      probe.expectMsg(TemperatureRecorded(requestId = 3))

      deviceActor.tell(ReadTemperature(requestId = 4), probe.ref)
      val response2 = probe.expectMsgType[RespondTemperature]
      response2.requestId shouldBe 4
      response2.value shouldBe Some(55.0)
    }

    "reply with device registered" in {
      deviceActor.tell(RequestTrackDevice("group", "device"), probe.ref)
      probe.expectMsg(DeviceRegistered)
    }

    "not reply when group name is unknown" in {
      deviceActor.tell(RequestTrackDevice("unknownGroup", "device"), probe.ref)
      probe.expectNoMessage(500.milliseconds)
    }

    "not reply when device name is unknown" in {
      deviceActor.tell(RequestTrackDevice("group", "unknownDevice"), probe.ref)
      probe.expectNoMessage(500.milliseconds)
    }

    "not reply when both group and device name are unknown" in {
      deviceActor.tell(RequestTrackDevice("unknownGroup", "unknownDevice"), probe.ref)
      probe.expectNoMessage(500.milliseconds)
    }
  }
}

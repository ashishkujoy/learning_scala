package com.thoughtworks.streams.actors
import akka.actor.{ActorRef, ActorSystem}
import akka.testkit.{TestKit, TestProbe}
import com.thoughtworks.streams.actors.DeviceManager.{DeviceRegistered, RequestTrackDevice}
import org.scalatest._

import scala.concurrent.duration._

class DeviceGroupTest(_system: ActorSystem)
    extends TestKit(_system)
    with Matchers
    with WordSpecLike
    with BeforeAndAfterAll
    with BeforeAndAfterEach {

  private val groupId  = "group"
  private val deviceId = "device"

  private var deviceGroup: ActorRef = _
  private var probe: TestProbe      = _

  def this() = this(ActorSystem("DeviceTest"))

  override protected def afterAll(): Unit = system.terminate()

  override protected def beforeEach(): Unit = {
    deviceGroup = system.actorOf(DeviceGroup.props(groupId))
    probe = TestProbe()
  }

  "Device Group" should {
    "reply with registration acknowledgement for correct " + groupId in {
      deviceGroup.tell(RequestTrackDevice(groupId, deviceId), probe.ref)
      probe.expectMsg(DeviceRegistered)
    }

    "reply be able to register more than one device" in {
      deviceGroup.tell(RequestTrackDevice(groupId, deviceId), probe.ref)
      probe.expectMsg(DeviceRegistered)
      val device1 = probe.lastSender

      val anotherDeviceId = "device2"
      deviceGroup.tell(RequestTrackDevice(groupId, anotherDeviceId), probe.ref)
      probe.expectMsg(DeviceRegistered)
      val device2 = probe.lastSender
      device1 shouldNot be(device2)
    }

    "reuse device actor if device is already registered" in {
      deviceGroup.tell(RequestTrackDevice(groupId, deviceId), probe.ref)
      probe.expectMsg(DeviceRegistered)
      val device1 = probe.lastSender

      deviceGroup.tell(RequestTrackDevice(groupId, deviceId), probe.ref)
      probe.expectMsg(DeviceRegistered)
      val device2 = probe.lastSender
      device1 shouldBe device2
    }

    "not reply with registration acknowledgement for incorrect " + groupId in {
      val unKnownGroupId = "unKnownGroup"
      deviceGroup.tell(RequestTrackDevice(unKnownGroupId, deviceId), probe.ref)
      probe.expectNoMessage(500.millisecond)
    }
  }
}

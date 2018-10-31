package com.thoughtworks.streams.actors
import akka.actor.{ActorRef, ActorSystem}
import akka.testkit.TestProbe
import com.thoughtworks.streams.actors.DeviceManager._

import scala.concurrent.duration._

class DeviceManagerTest(_system: ActorSystem) extends ActorsTest(_system) {

  private val groupId        = "group"
  private val deviceId       = "device"
  private val anotherGroupId = "group2"

  private var deviceManager: ActorRef = _
  private var probe: TestProbe        = _

  def this() = this(ActorSystem("DeviceTest"))

  override protected def afterAll(): Unit = system.terminate()

  override protected def beforeEach(): Unit = {
    deviceManager = system.actorOf(DeviceManager.props())
    probe = TestProbe()
  }

  "Device Manager" should {
    "add new device actors to a group actor" in {
      deviceManager.tell(RequestTrackDevice(groupId, deviceId), probe.ref)
      probe.expectMsg(DeviceRegistered)

      deviceManager.tell(RequestTrackDevice(groupId, "device2"), probe.ref)
      probe.expectMsg(DeviceRegistered)
    }

    "give all active group actors" in {
      deviceManager.tell(RequestGroupList(21), probe.ref)
      probe.expectMsg(3000.millisecond, ReplyGroupList(21, Set.empty))

      deviceManager.tell(RequestTrackDevice(groupId, deviceId), probe.ref)
      probe.expectMsg(DeviceRegistered)

      deviceManager.tell(RequestGroupList(21), probe.ref)
      probe.expectMsg(3000.millisecond, ReplyGroupList(21, Set(groupId)))

      deviceManager.tell(RequestTrackDevice(anotherGroupId, deviceId), probe.ref)
      probe.expectMsg(DeviceRegistered)

      deviceManager.tell(RequestGroupList(21), probe.ref)
      probe.expectMsg(3000.millisecond, ReplyGroupList(21, Set(groupId, anotherGroupId)))
    }

  }
}

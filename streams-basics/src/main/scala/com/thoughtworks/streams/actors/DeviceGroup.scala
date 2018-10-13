package com.thoughtworks.streams.actors
import akka.actor.{Actor, ActorRef, Props, Terminated}
import com.thoughtworks.streams.actors.DeviceGroup.{ReplyDeviceList, RequestDeviceList}
import com.thoughtworks.streams.actors.DeviceManager.RequestTrackDevice
import com.thoughtworks.streams.actors.ImplicitConverters._

object DeviceGroup {
  def props(groupId: String): Props = Props(new DeviceGroup(groupId))

  final case class RequestDeviceList(requestId: Long)
  final case class ReplyDeviceList(requestId: Long, ids: Set[String])
}

class DeviceGroup(groupId: String) extends Actor {
  private var deviceIdToActor = Map.empty[String, ActorRef]

  override def receive: Receive = {
    case trackMsg @ RequestTrackDevice(`groupId`, _)  => handleValidTrackDeviceRequest(trackMsg)
    case RequestTrackDevice(unKnownGroupId, deviceId) =>
    case Terminated(deviceActor)                      => removeDeadActor(deviceActor)
    case RequestDeviceList(requestId)                 => sender() ! ReplyDeviceList(requestId, deviceIdToActor.keys.toSet)
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

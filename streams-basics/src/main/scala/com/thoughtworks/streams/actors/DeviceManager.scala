package com.thoughtworks.streams.actors
import akka.actor.{Actor, ActorRef, Props, Terminated}
import com.thoughtworks.streams.actors.DeviceManager.{ReplyGroupList, RequestGroupList, RequestTrackDevice}
import com.thoughtworks.streams.actors.ImplicitConverters._

object DeviceManager {
  final case class RequestTrackDevice(groupId: String, deviceId: String)
  final case class UnknownGroupOrDeviceId(groupId: String, deviceId: String)
  final case class RequestGroupList(requestId: Long)
  final case class ReplyGroupList(requestId: Long, ids: Set[String])
  case object DeviceRegistered

  def props(): Props = Props(new DeviceManager)

}

class DeviceManager extends Actor {
  private var groupIdToActor = Map.empty[String, ActorRef]

  override def receive: Receive = {
    case trackMsg @ RequestTrackDevice(_, _) => handleValidRequestTrackDevice(trackMsg)
    case Terminated(groupActor)              => removeDeadActor(groupActor)
    // TODO: Find a way to test this functionality without breaking encapsulation
    case RequestGroupList(requestId) => sender() ! ReplyGroupList(requestId, groupIdToActor.keys.toSet)
  }

  private def removeDeadActor(groupActor: ActorRef): Unit = {
    val mayBeGroupId = groupIdToActor.findKeyByValue(groupActor)
    mayBeGroupId.foreach(groupIdToActor -= _)
  }

  private def handleValidRequestTrackDevice(trackMsg: RequestTrackDevice): Unit = {
    val groupId = trackMsg.groupId
    groupIdToActor.get(groupId) match {
      case Some(ref) => ref forward trackMsg
      case None      => createNewGroup(groupId) forward trackMsg
    }

  }
  private def createNewGroup(groupId: String): ActorRef = {
    val groupActor = context.actorOf(DeviceGroup.props(groupId), "group-" + groupId)
    context.watch(groupActor)
    groupIdToActor += groupId -> groupActor
    groupActor
  }

}

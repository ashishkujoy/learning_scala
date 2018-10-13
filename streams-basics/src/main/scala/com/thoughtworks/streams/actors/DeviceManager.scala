package com.thoughtworks.streams.actors
import akka.actor.Actor

object DeviceManager {
  final case class RequestTrackDevice(groupId: String, deviceId: String)
  final case class UnknownGroupOrDeviceId(groupId: String, deviceId: String)
  case object DeviceRegistered
}

class DeviceManager extends Actor { override def receive: Receive = ??? }

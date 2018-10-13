package com.thoughtworks.streams.actors

import akka.actor.{Actor, Props}

object Device {
  def props(groupId: String, deviceId: String): Props = Props(new Device(groupId, deviceId))

  final case class RecordTemperature(requestId: Long, value: Double)
  final case class TemperatureRecorded(requestId: Long)

  final case class ReadTemperature(requestId: Long)
  final case class RespondTemperature(requestId: Long, value: Option[Double])
}

class Device(groupId: String, deviceId: String) extends Actor {
  import Device._

  var lastTemperatureReading: Option[Double] = None

  override def receive: Receive = {
    case RecordTemperature(id, value) ⇒
      lastTemperatureReading = Some(value)
      sender() ! TemperatureRecorded(id)

    case ReadTemperature(id) ⇒
      sender() ! RespondTemperature(id, lastTemperatureReading)
  }

}

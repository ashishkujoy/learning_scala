package com.thoughtworks.streams.actors.routing

import akka.actor.{Actor, Props}

class EchoActor extends Actor {
  override def receive: Receive = {
    case message => println(s"${self.path.name} : Message Received $message")
  }
}

object EchoActor {
  def props = Props(new EchoActor)
}

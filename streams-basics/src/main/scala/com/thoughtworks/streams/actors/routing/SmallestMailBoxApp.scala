package com.thoughtworks.streams.actors.routing

import akka.actor.{ActorRef, ActorSystem}
import akka.routing.SmallestMailboxPool
import com.thoughtworks.streams.actors.routing.EchoActor.props

object SmallestMailBoxApp extends App {
  private val actorSystem = ActorSystem("Smallest-MailBox-App")

  private val router: ActorRef = actorSystem.actorOf(SmallestMailboxPool(5).props(props))

  // Sends message to actor which has lowest number of message in message box
  for(i <- 1 to 9) {
    router ! "Hello World!"
  }
}

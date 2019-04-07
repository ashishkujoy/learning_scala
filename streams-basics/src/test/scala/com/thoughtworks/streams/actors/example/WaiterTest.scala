package com.thoughtworks.streams.actors.example

import akka.actor.{ActorRef, ActorSystem}
import akka.testkit.ImplicitSender
import com.thoughtworks.streams.actors.example.Waiter.OrderAcknowledgment
import com.thoughtworks.streams.actors.iot.ActorsTest

class WaiterTest extends ActorsTest(ActorSystem("Waiter-Test")) with ImplicitSender{
  private var waiter: ActorRef = _
  private val RajmaChawal      = "RajmaChawal"

  override protected def afterAll(): Unit = system.terminate()

  override protected def beforeEach(): Unit = {
    waiter = system.actorOf(Waiter.props("Test Waiter"))
  }

  "Waiter" should {
    "acknowledge on receiving an order" in {
      waiter ! Waiter.Order(1, RajmaChawal, 42)
      expectMsg(OrderAcknowledgment(1))
    }
  }

}


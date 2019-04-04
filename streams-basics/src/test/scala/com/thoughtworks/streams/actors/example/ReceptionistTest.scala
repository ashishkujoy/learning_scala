package com.thoughtworks.streams.actors.example

import akka.actor.{ActorRef, ActorSystem}
import akka.testkit.ImplicitSender
import com.thoughtworks.streams.actors.example.Receptionist._
import com.thoughtworks.streams.actors.iot.ActorsTest

class ReceptionistTest extends ActorsTest(ActorSystem("ReceptionistTest")) with ImplicitSender {

  private val RajmaChawal            = "RajmaChawal"
  private val RajmaChawalPrice       = BigDecimal(34.45)
  private var receptionist: ActorRef = _
  private val orderIdGenerator = new OrderIdGenerator {
    override def generate: OrderId = 1
  }
  private val priceCatalog = Map(
    RajmaChawal -> RajmaChawalPrice
  )

  override protected def beforeEach(): Unit = {
    receptionist = system.actorOf(props(priceCatalog, orderIdGenerator))
  }

  override protected def afterAll(): Unit = {
    system.terminate()
  }

  "Receptionist" should {
    "accept food order" in {
      receptionist ! PlaceOrder(RajmaChawal, "Customer")
      expectMsg(Receipt(1, RajmaChawalPrice, RajmaChawal))
    }
  }

}

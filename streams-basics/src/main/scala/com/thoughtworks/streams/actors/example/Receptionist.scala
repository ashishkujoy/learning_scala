package com.thoughtworks.streams.actors.example

import akka.actor.{Actor, ActorLogging, Props}
import com.thoughtworks.streams.actors.example.Receptionist._

class Receptionist(priceCatalog: PriceCatalog, orderIdGenerator: OrderIdGenerator) extends Actor with ActorLogging {

  override def receive: Receive = {
    case PlaceOrder(foodItem, requester) =>
      log.info("Receive a order for {} by {}", foodItem, requester)
      sender ! Receipt(orderIdGenerator.generate, priceCatalog(foodItem), foodItem)
    case _ => log.error("Unknown request")
  }
}

object Receptionist {
  type FoodItem     = String
  type Requester    = String
  type OrderId      = Int
  type PriceCatalog = Map[FoodItem, BigDecimal]

  trait OrderIdGenerator {
    def generate: OrderId
  }

  case class PlaceOrder(foodItem: FoodItem, requester: Requester)
  case class Receipt(id: OrderId, cost: BigDecimal, foodItem: FoodItem)

  def props(priceCatalog: PriceCatalog, orderIdGenerator: OrderIdGenerator): Props =
    Props(new Receptionist(priceCatalog, orderIdGenerator))
}

package com.thoughtworks.streams.actors.example

import akka.actor.{Actor, ActorLogging, Props}
import com.thoughtworks.streams.actors.example.Receptionist.FoodItem
import com.thoughtworks.streams.actors.example.Waiter.{Order, OrderAcknowledgment}

class Waiter(name: String) extends Actor with ActorLogging {
  override def receive: Receive = {
    case Order(reqId, foodItem, table) =>
      log.info("Receive order of {} for table {}", foodItem, table)
      sender ! OrderAcknowledgment(reqId)
    case unknownMessage => log.error("Unknown Message {}", unknownMessage)
  }
}

object Waiter {
  def props(name: String = "Waiter"): Props = Props(new Waiter(name))

  type Table = Int
  case class Order(reqId: Int, foodItem: FoodItem, table: Table)
  case class OrderAcknowledgment(reqId: Int)
}

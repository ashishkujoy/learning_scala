package com.thoughtworks.streams.actors.routing

import akka.actor.ActorSystem
import akka.routing.ConsistentHashingPool
import akka.routing.ConsistentHashingRouter.{ConsistentHashMapping, ConsistentHashableEnvelope}
import com.thoughtworks.streams.actors.routing.Cache._

object ConsistentHashingPoolApp extends App {
  val actorSystem = ActorSystem("Hello-Akka")
  def hashMapping: ConsistentHashMapping = {
    case Evict(key) => key
  }
  val cache =
    actorSystem.actorOf(ConsistentHashingPool(10, hashMapping = hashMapping).props(Cache.props), "Cache")

  cache ! ConsistentHashableEnvelope(message = Entry("hello", "HELLO"), hashKey = "hello")
  cache ! ConsistentHashableEnvelope(message = Entry("hi", "HI"), hashKey = "hi")
  cache ! Get("hello")
  cache ! Get("hi")
  cache ! Evict("hi")
  cache ! Evict("hello")
}

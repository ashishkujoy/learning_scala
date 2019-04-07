package com.thoughtworks.streams.actors.routing

import akka.actor.{Actor, Props}
import akka.routing.ConsistentHashingRouter.ConsistentHashable

class Cache extends Actor {
  import Cache._

  var cache = Map.empty[String, String]

  override def receive: Receive = {
    case Entry(key, value) =>
      println(s" ${self.path.name} adding key $key")
      cache += (key -> value)
    case Get(key) =>
      println(s" ${self.path.name} fetching key $key")
      sender() ! cache.get(key)
    case Evict(key) =>
      println(s" ${self.path.name} removing key $key")
      cache -= key
  }
}

object Cache {
  case class Evict(key: String)
  case class Get(key: String) extends ConsistentHashable {
    override def consistentHashKey: Any = key
  }
  case class Entry(key: String, value: String)

  def props = Props(new Cache)
}

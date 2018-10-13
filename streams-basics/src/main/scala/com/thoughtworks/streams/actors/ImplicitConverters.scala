package com.thoughtworks.streams.actors

object ImplicitConverters {
  implicit class MyMap[A, B](map: Map[A, B]) {
    def findKeyByValue(value: B): Option[A] = {
      map.find(kv => kv._2 == value).map(h => h._1)
    }
  }
}

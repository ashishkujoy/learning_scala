package com.thougthworks

object ImplicitConverters {
  implicit class MyMap[A, B](mapAtoB: Map[A, B]) {
    def findKeyByValue(value: B): Option[A] = {
      mapAtoB.find(kv => kv._2 == value).map(h => h._1)
    }

    def swap: Map[B, A] = mapAtoB.map(kv => kv._2 -> kv._1)
  }
}

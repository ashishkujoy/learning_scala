package com.thoughtworks.streams

import akka.actor.ActorSystem
import akka.stream.ActorMaterializer

import scala.concurrent.ExecutionContextExecutor

trait Implicits {
  implicit val actorSystem: ActorSystem             = ActorSystem("Streams")
  implicit val actorMaterializer: ActorMaterializer = ActorMaterializer()
  implicit val dispatcher: ExecutionContextExecutor = actorSystem.dispatcher
}

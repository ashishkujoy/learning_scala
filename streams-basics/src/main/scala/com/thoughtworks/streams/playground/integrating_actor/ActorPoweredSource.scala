package com.thoughtworks.streams.playground.integrating_actor

import akka.actor.ActorRef
import akka.stream.OverflowStrategy
import akka.stream.scaladsl.{Sink, Source}
import com.thoughtworks.streams.Implicits

object ActorPoweredSource extends Implicits {
  private val source: Source[Int, ActorRef] = Source.actorRef[Int](bufferSize = 4, OverflowStrategy.dropNew)
  private val materializedActorRef: ActorRef = source.to(Sink.foreach(println)).run()

  def main(args: Array[String]): Unit = {
    (1 to 10).foreach(materializedActorRef ! _)
  }

}

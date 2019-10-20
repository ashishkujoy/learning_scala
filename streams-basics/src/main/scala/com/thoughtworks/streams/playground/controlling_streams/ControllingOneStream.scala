package com.thoughtworks.streams.playground.controlling_streams

import akka.stream.scaladsl.{Keep, Sink, Source}
import akka.stream.{DelayOverflowStrategy, KillSwitches}
import com.thoughtworks.streams.Implicits

import scala.concurrent.duration._
import scala.util.{Failure, Success}

object ControllingOneStream extends App with Implicits {
  private val source = Source(Stream.from(1)).delay(1.seconds, DelayOverflowStrategy.backpressure)
  private val sink   = Sink.last[Int]

  private val (killSwitch, mayBeLastElement) = source
    .viaMat(KillSwitches.single)(Keep.right)
    .toMat(sink)(Keep.both)
    .run()

  mayBeLastElement.onComplete {
    case Success(value)     => println(s"Last element received $value")
    case Failure(exception) => println(s"Stream failed with $exception")
  }

  actorSystem.scheduler.scheduleOnce(3.seconds)(killSwitch.shutdown)
}

package com.thoughtworks.streams.playground.source

import akka.stream.{OverflowStrategy, QueueOfferResult}
import akka.stream.scaladsl.{Keep, Sink, Source, SourceQueueWithComplete}
import com.thoughtworks.streams.Implicits

import scala.concurrent.duration._
import scala.language.postfixOps

object QueueSource extends Implicits {
  private val queue: SourceQueueWithComplete[Int] = Source
    .queue[Int](50, OverflowStrategy.dropNew)
    .async("")
    .throttle(3, 1 seconds)
    .toMat(Sink.foreach(x => println(s"completed $x")))(Keep.left)
    .run()
  private val numbers = Source(1 to 1000)

  def main(args: Array[String]): Unit = {
    numbers.mapAsync(1)(x => {
      queue.offer(x).map {
        case QueueOfferResult.Enqueued    => println(s"enqueued $x")
        case QueueOfferResult.Dropped     => println(s"dropped $x")
        case QueueOfferResult.Failure(ex) => println(s"Offer failed ${ex.getMessage}")
        case QueueOfferResult.QueueClosed => println("Source Queue closed")
      }
    }).runWith(Sink.ignore)
  }
}

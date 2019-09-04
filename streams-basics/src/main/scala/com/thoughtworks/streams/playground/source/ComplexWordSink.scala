package com.thoughtworks.streams.playground.source

import akka.stream.SinkShape
import akka.stream.scaladsl.{Broadcast, Flow, GraphDSL, Keep, Sink, Source}
import akka.{Done, NotUsed}
import com.thoughtworks.streams.Implicits

import scala.concurrent.Future
import scala.util.{Failure, Success}

object ComplexWordSink extends Implicits {
  private val source: Source[String, NotUsed]        = Source(List("Akka", "Tea", "gap", "Party", "Foo", "bar"))
  private val printerSink: Sink[Any, Future[Done]]   = Sink.foreach(println)
  private val counterSink: Sink[String, Future[Int]] = Sink.fold(0)((count, _) => count + 1)

  private val complexWordSink: Sink[String, Future[Int]] = Sink.fromGraph(GraphDSL.create(counterSink) {
    implicit builder => counterSinkShape =>
      import GraphDSL.Implicits._

      val broadcast            = builder.add(Broadcast[String](2))
      val uppercaseFilterShape = builder.add(Flow[String].filter(word => word.head == word.head.toUpper))
      val wordSizeFilterShape  = builder.add(Flow[String].filter(_.length > 3))

      broadcast.out(0) ~> uppercaseFilterShape ~> printerSink
      broadcast.out(1) ~> wordSizeFilterShape ~> counterSinkShape

      SinkShape(broadcast.in)
  })

  def main(args: Array[String]): Unit = {
    val wordsGreaterThanSize3 = source.toMat(complexWordSink)(Keep.right).run()
    wordsGreaterThanSize3.onComplete {
      case Success(value)     => println(s"Total words greater than size 3 are $value")
      case Failure(exception) => println(exception)
    }
  }
}

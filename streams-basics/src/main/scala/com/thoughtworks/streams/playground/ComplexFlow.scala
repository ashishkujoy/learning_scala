package com.thoughtworks.streams.playground

import akka.NotUsed
import akka.stream.FlowShape
import akka.stream.scaladsl.{Broadcast, Flow, GraphDSL, Keep, Sink, Source}
import com.thoughtworks.streams.Implicits

import scala.concurrent.Future
import scala.util.{Failure, Success}

object ComplexFlow extends Implicits {
  private val source: Source[Int, NotUsed]            = Source(1 to 100)
  private val multiplierFlow: Flow[Int, Int, NotUsed] = Flow[Int].map(_ * 2)

  private def enhanceFlow[A, B](flow: Flow[A, B, _]): Flow[A, B, Future[Int]] = {
    val counterSink = Sink.fold[Int, B](0)((count, _) => count + 1)

    Flow.fromGraph(GraphDSL.create(counterSink) { implicit builder => counterSinkShape =>
      import GraphDSL.Implicits._

      val broadcast   = builder.add(Broadcast[B](2))
      val flowShape = builder.add(flow)

      flowShape ~> broadcast ~> counterSinkShape

      FlowShape(flowShape.in, broadcast.out(1))
    })
  }

  def main(args: Array[String]): Unit = {
    source
      .viaMat(enhanceFlow(multiplierFlow))(Keep.right)
      .toMat(Sink.foreach(println))(Keep.left)
      .run()
      .onComplete {
        case Success(value) => println(s"Total Number of elements passed $value")
        case Failure(ex) => println(ex)
      }
  }
}

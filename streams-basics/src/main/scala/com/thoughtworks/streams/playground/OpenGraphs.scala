package com.thoughtworks.streams.playground

import akka.NotUsed
import akka.actor.ActorSystem
import akka.stream.scaladsl.{Broadcast, Concat, Flow, GraphDSL, Sink, Source}
import akka.stream.{ActorMaterializer, FlowShape, SinkShape, SourceShape}

object OpenGraphs {

  def main(args: Array[String]): Unit = {
    implicit val actorSystem: ActorSystem             = ActorSystem("OpenGraph")
    implicit val actorMaterializer: ActorMaterializer = ActorMaterializer()

//    complexSource.runForeach(println)
//    complexSource.to(complexSink).run()
    Source(1 to 3).via(flowFromSinkAndSource).runWith(Sink.ignore)
  }

  /*Sends element of both first and second source but first send all elements of source1*/
  def complexSource: Source[Int, NotUsed] = {
    val firstSource  = Source(1 to 10)
    val secondSource = Source(100 to 200)

    Source.fromGraph(GraphDSL.create() { implicit builder =>
      import GraphDSL.Implicits._
      val concat = builder.add(Concat[Int](2))

      firstSource ~> concat
      secondSource ~> concat

      SourceShape(concat.out)
    })
  }
  /*Sends incoming element to both first and second sink*/
  def complexSink: Sink[Int, NotUsed] = {
    val firstSink  = Sink.foreach[Int](a => println(s"Sink1 $a"))
    val secondSink = Sink.foreach[Int](a => println(s"Sink2 $a"))

    Sink.fromGraph(GraphDSL.create() { implicit builder =>
      import GraphDSL.Implicits._

      val broadcast = builder.add(Broadcast[Int](2))
      broadcast ~> firstSink
      broadcast ~> secondSink
      SinkShape(broadcast.in)
    })
  }

  def complexFlow: Flow[Int, Int, NotUsed] = {
    val firstFlow  = Flow[Int].map(_ + 1)
    val secondFlow = Flow[Int].map(_ * 10)

    Flow.fromGraph(GraphDSL.create() { implicit builder =>
      import GraphDSL.Implicits._

      val firstFlowShape  = builder.add(firstFlow)
      val secondFlowShape = builder.add(secondFlow)

      firstFlowShape ~> secondFlowShape

      FlowShape(firstFlowShape.in, secondFlowShape.outlet)
    })
  }

  def flowFromSinkAndSource = {
    val source = Source(11 to 20)
    val sink   = Sink.foreach[Int](println)

    Flow.fromGraph(GraphDSL.create() { implicit builder =>
      val sinkShape   = builder.add(sink)
      val sourceShape = builder.add(source)
      FlowShape(sinkShape.in, sourceShape.out)
    })
  }
}

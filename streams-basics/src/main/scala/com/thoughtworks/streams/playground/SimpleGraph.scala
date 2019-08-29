package com.thoughtworks.streams.playground

import akka.NotUsed
import akka.actor.ActorSystem
import akka.stream.scaladsl.{Balance, Broadcast, Flow, GraphDSL, Merge, RunnableGraph, Sink, Source, Zip}
import akka.stream.{ActorMaterializer, ClosedShape}

object SimpleGraph {
  private val simpleSource    = Source(1 to 1000)
  private val incrementalFlow = Flow[Int].map(_ + 1)
  private val multiplierFlow  = Flow[Int].map(_ * 2)
  private val simpleSink      = Sink.foreach[(Int, Int)](println)

  def main(args: Array[String]): Unit = {
    implicit val system: ActorSystem             = ActorSystem("SimpleGraph")
    implicit val materializer: ActorMaterializer = ActorMaterializer()

    graphWithMergeAndBalance run
  }

  private def getRunnableGraph = {
    RunnableGraph.fromGraph(GraphDSL.create() { implicit builder: GraphDSL.Builder[NotUsed] =>
      val broadcast = builder.add(Broadcast[Int](2))
      val zip       = builder.add(Zip[Int, Int])
      import GraphDSL.Implicits._

      simpleSource ~> broadcast

      broadcast.out(0) ~> incrementalFlow ~> zip.in0
      broadcast.out(1) ~> multiplierFlow ~> zip.in1

      zip.out ~> simpleSink

      ClosedShape
    })
  }

  def graphWithTwoSink = {
    val prettySink = Sink.foreach[Int](a => println(s"Let prettify $a"))
    val uglySink   = Sink.foreach[Int](a => println(s"Let's uglify it $a"))
    val source     = Source(1 to 10)

    RunnableGraph.fromGraph(GraphDSL.create() { implicit builder =>
      val broadcast = builder.add(Broadcast[Int](2))
      import GraphDSL.Implicits._

      source ~> broadcast
      broadcast.out(0) ~> prettySink
      broadcast.out(1) ~> uglySink
      ClosedShape
    })
  }

  def graphWithMergeAndBalance = {
    val sink1 = Sink.foreach[Int](a => println(s"Sink1 $a"))
    val sink2 = Sink.foreach[Int](a => println(s"Sink2 $a"))
    import GraphDSL.Implicits._

    import scala.concurrent.duration._

    val source     = Source(1 to 100)
    val slowSource = Source(100 to 110).throttle(1, 1 seconds)

    RunnableGraph.fromGraph(GraphDSL.create() { implicit builder =>
      val merge   = builder.add(Merge[Int](2))
      val balance = builder.add(Balance[Int](2))

      source ~> merge
      slowSource ~> merge ~> balance ~> sink1
      balance ~> sink2

      ClosedShape
    })

  }
}

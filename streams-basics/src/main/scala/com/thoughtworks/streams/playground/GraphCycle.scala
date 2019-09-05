package com.thoughtworks.streams.playground

import akka.NotUsed
import akka.stream.scaladsl._
import akka.stream.{ClosedShape, Graph, OverflowStrategy, UniformFanInShape}
import com.thoughtworks.streams.Implicits
import scala.concurrent.duration._

object GraphCycle extends Implicits {
  private val acceleratorWithCyclicDeadLock: Graph[ClosedShape.type, NotUsed] = GraphDSL.create() { implicit builder =>
    import GraphDSL.Implicits._

    val source = builder.add(Source(1 to 100))
    val merge  = builder.add(Merge[Int](2))
    val incremental = builder.add(Flow[Int].map { number =>
      println(s"Incrementing $number")
      number
    })

    source ~> merge ~> incremental ~> merge

    ClosedShape
  }

  private val acceleratorWithMergePreferred: Graph[ClosedShape.type, NotUsed] = GraphDSL.create() { implicit builder =>
    import GraphDSL.Implicits._

    val source = builder.add(Source(1 to 100))
    val merge  = builder.add(MergePreferred[Int](1))
    val incremental = builder.add(Flow[Int].map { number =>
      println(s"Incrementing $number")
      number + 1
    })

    source ~> merge ~> incremental ~> merge.preferred

    ClosedShape
  }

  private val repeaterWithBuffer: Graph[ClosedShape.type, NotUsed] = GraphDSL.create() { implicit builder =>
    import GraphDSL.Implicits._

    val source = builder.add(Source(1 to 100))
    val merge  = builder.add(MergePreferred[Int](1))
    val incremental = builder.add(Flow[Int].buffer(5, OverflowStrategy.dropHead).map { number =>
      println(s"Incrementing $number")
      Thread.sleep(200)
      number + 1
    })

    source ~> merge ~> incremental ~> merge.preferred

    ClosedShape
  }

  private val fibonacciShape: Graph[UniformFanInShape[BigInt, BigInt], NotUsed] = GraphDSL.create() { implicit builder =>
    val zipShape                      = builder.add(Zip[BigInt, BigInt]())
    val seedElementProviderShape      = builder.add(MergePreferred[(BigInt, BigInt)](1))
    val newSeedElementCalculatorShape = builder.add(Flow[(BigInt, BigInt)].map(a => (a._2, a._1 + a._2)).throttle(5 , 1 seconds))
    val newSeedElementBroadcast       = builder.add(Broadcast[(BigInt, BigInt)](2))
    val fibonacciNumberExtractorShape = builder.add(Flow[(BigInt, BigInt)].map(_._1))

    import GraphDSL.Implicits._

    zipShape.out ~> seedElementProviderShape ~> newSeedElementCalculatorShape ~> newSeedElementBroadcast ~> fibonacciNumberExtractorShape
    seedElementProviderShape.preferred <~ newSeedElementBroadcast

    UniformFanInShape(fibonacciNumberExtractorShape.out, zipShape.in0, zipShape.in1)
  }

  private val fibonacciGraph: RunnableGraph[NotUsed] = RunnableGraph.fromGraph(GraphDSL.create() { implicit builder =>
    val firstElementSourceShape  = builder.add(Source.single[BigInt](1))
    val secondElementSourceShape = builder.add(Source.single[BigInt](1))
    val fiboShape                = builder.add(fibonacciShape)
    val printerSinkShape         = builder.add(Sink.foreach(println))

    import GraphDSL.Implicits._

    firstElementSourceShape ~> fiboShape
    secondElementSourceShape ~> fiboShape
    fiboShape ~> printerSinkShape

    ClosedShape
  })

  def main(args: Array[String]): Unit = {
//    RunnableGraph.fromGraph(repeaterWithBuffer).run()
    fibonacciGraph.run()
  }
}

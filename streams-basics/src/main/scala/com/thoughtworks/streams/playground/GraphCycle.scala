package com.thoughtworks.streams.playground

import akka.NotUsed
import akka.stream.scaladsl._
import akka.stream.{ClosedShape, Graph, OverflowStrategy}
import com.thoughtworks.streams.Implicits

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

  def main(args: Array[String]): Unit = {
    RunnableGraph.fromGraph(repeaterWithBuffer).run()
  }
}

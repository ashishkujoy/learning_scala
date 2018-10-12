package com.thoughtworks.streams.playground

import akka.NotUsed
import akka.actor.Actor
import akka.stream.scaladsl.{Flow, GraphDSL, Keep, RunnableGraph, Sink, Source}
import akka.stream.{ActorMaterializer, ClosedShape}
import com.thoughtworks.streams.GlobalVariables._

import scala.concurrent.Future
import scala.util.{Failure, Success}

object Application {
  private val source: Source[Int, NotUsed]              = Source(1 to 100)
  private val flow                                      = Flow[Int].map(_ * 2)
  private val sink: Sink[Int, Future[Int]]              = Sink.fold[Int, Int](0)(_ + _)
  private val runnableGraph: RunnableGraph[Future[Int]] = source.toMat(sink)(Keep.right)
  private val otherSink: Sink[Int, NotUsed] =
    Flow[Int]
      .alsoTo(Sink.fold(0)(_ + _))
      .to(Sink.foreach(a => println(s"this is $a")))

  val r12: RunnableGraph[(NotUsed, NotUsed, Future[Int])] =
    RunnableGraph.fromGraph(GraphDSL.create(source, flow, sink)((_, _, _)) { implicit builder ⇒ (src, f, dst) ⇒
      import GraphDSL.Implicits._
      src ~> f ~> dst
      ClosedShape
    })

  final class RunWithMyself extends Actor {
    implicit val mat: ActorMaterializer = ActorMaterializer()
    println("running")

    Source.maybe
      .runWith(Sink.onComplete {
        case Success(done) ⇒ println(s"Completed: $done")
        case Failure(ex)   ⇒ println(s"Failed: ${ex.getMessage}")
      })

    def receive: PartialFunction[Any, Unit] = {
      case "boom" ⇒
        context.stop(self) // will also terminate the stream
    }
  }

  def main(args: Array[String]): Unit = {
//    source.runForeach(i ⇒ println(i))(materializer)
//    println(runnableGraph.run().await)
//    Source(1 to 6).to(otherSink).run()
    Source(List(1, 2, 3))
      .map(_ + 1)
      .async
      .map(_ * 2)
      .to(Sink.foreach(println(_)))
      .run()

    system.terminate()
  }

}

package com.thoughtworks.streams
import java.nio.file.Paths

import akka.Done
import akka.actor.ActorSystem
import akka.stream.scaladsl.{FileIO, Flow, Keep, Sink, Source}
import akka.stream.{ActorMaterializer, IOResult}
import akka.util.ByteString

import scala.concurrent.duration._
import scala.concurrent.{Await, ExecutionContextExecutor, Future}

object GlobalVariables {
  implicit val system: ActorSystem                  = ActorSystem("akka-streams")
  implicit val materializer: ActorMaterializer      = ActorMaterializer()
  implicit val dispatcher: ExecutionContextExecutor = system.dispatcher

  def lineSink[A](filename: String): Sink[A, Future[IOResult]] =
    Flow[A]
      .map(s ⇒ ByteString(s.toString + "\n"))
      .toMat(FileIO.toPath(Paths.get(filename)))(Keep.right)

  def toConsole(source: Source[Any, Any]): Future[Done] = {
    source.map(_.toString).runWith(Sink.foreach(println))
  }

  val count: Flow[Any, Int, Any] = Flow[Any].map(_ ⇒ 1)

  val sumSink: Sink[Int, Future[Int]] = Sink.fold[Int, Int](0)(_ + _)

  implicit class RichFuture[A](future: Future[A]) {
    def await: A = Await.result(future, 5.second)
  }
}

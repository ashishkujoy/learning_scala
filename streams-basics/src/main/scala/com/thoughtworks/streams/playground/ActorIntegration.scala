package com.thoughtworks.streams.playground

import akka.NotUsed
import akka.actor.{Actor, ActorLogging, ActorRef, Props}
import akka.stream.OverflowStrategy
import akka.stream.scaladsl.{Flow, Sink, Source}
import akka.util.Timeout
import com.thoughtworks.streams.Implicits

import scala.concurrent.duration._
import scala.language.postfixOps

object ActorIntegration extends Implicits {
  class SimpleActor extends Actor with ActorLogging {
    override def receive: Receive = {
      case message: String => log.info(s"Simple Actor got a message: $message")
      case integer: Int =>
        log.info(s"Simple Actor got a integer: $integer")
        sender() ! (2 * integer)
    }
  }

  class SimpleSinkActor extends Actor with ActorLogging {
    override def receive: Receive = {
      case StreamInit =>
        log.info("Stream initialize")
        sender ! StreamAck
      case StreamComplete   => log.info("Stream complete")
      case StreamFailed(ex) => log.error(ex.toString)
      case message =>
        log.info(s"Receive a message $message")
        sender ! StreamAck
    }
  }

  case object StreamInit
  case object StreamAck
  case object StreamComplete
  case class StreamFailed(throwable: Throwable)

  private val simpleActor: ActorRef                         = actorSystem.actorOf(Props(new SimpleActor), "SimpleActor")
  private implicit val timeout: Timeout                     = Timeout(2 seconds)
  private val actorPoweredFlow: Flow[Int, Int, NotUsed]     = Flow[Int].ask[Int](4)(simpleActor)
  private val source: Source[Int, NotUsed]                  = Source(1 to 10)
  private val actorPoweredSource: Source[Nothing, ActorRef] = Source.actorRef(5, OverflowStrategy.dropNew)
  private val sinkActor                                     = actorSystem.actorOf(Props(new SimpleSinkActor()), "SimpleSinkActor")
  private val actorPoweredSink: Sink[Any, NotUsed] = Sink.actorRefWithAck(
    ref = sinkActor,
    onInitMessage = StreamInit,
    ackMessage = StreamAck,
    onCompleteMessage = StreamComplete,
    onFailureMessage = throwable => StreamFailed(throwable)
  )

  def main(args: Array[String]): Unit = {
//    source.via(actorPoweredFlow).to(Sink.ignore).run()
//    val materialisedActor = actorPoweredSource.to(Sink.foreach[Int](int => println(s"receive int $int"))).run()

//    Seq(1, 2, 3, 4, 5, 56, 7, 8, 9, 0).foreach(materialisedActor ! _)
    Source(1 to 10).to(actorPoweredSink).run()
  }
}

package com.thoughtworks.streams.playground.integrating_actor

import akka.NotUsed
import akka.actor.{Actor, ActorLogging, ActorRef, Props}
import akka.stream.scaladsl.{Sink, Source}
import com.thoughtworks.streams.Implicits

object ActorBasedSink extends Implicits {
  private val sinkActor: ActorRef = actorSystem.actorOf(Props[SquareActor], "SinkActor")

  private val actorBasedSink: Sink[Int, NotUsed] = Sink.actorRefWithAck(
    ref = sinkActor,
    onInitMessage = StreamInit,
    ackMessage = StreamAck,
    onCompleteMessage = StreamComplete,
    onFailureMessage = (ex) => StreamException(ex)
  )

  def main(args: Array[String]): Unit = {
    Source(1 to 10).runWith(actorBasedSink)
  }

  class SquareActor extends Actor with ActorLogging {
    override def receive: Receive = {
      case StreamInit =>
        log.info("Streaming started")
        sender() ! StreamAck
      case StreamComplete =>
        log.info("Stream complete")
      case ex: StreamException =>
        log.error(ex.toString)
      case number: Int =>
        log.info(s"Receive $number")
        if (number != 5)         sender() ! StreamAck

      case _ => log.error("Unexpected message")
    }
  }
  case object StreamInit
  case object StreamComplete
  case object StreamAck
  case class StreamException(ex: Throwable)

}

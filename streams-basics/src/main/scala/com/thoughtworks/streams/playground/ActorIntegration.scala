package com.thoughtworks.streams.playground

import akka.NotUsed
import akka.actor.{Actor, ActorLogging, ActorRef, Props}
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
  private val simpleActor: ActorRef                         = actorSystem.actorOf(Props(new SimpleActor), "SimpleActor")
  private implicit val timeout: Timeout                     = Timeout(2 seconds)
  private val actorPoweredFlow: Flow[Int, Int, NotUsed] = Flow[Int].ask[Int](4)(simpleActor)
  private val source: Source[Int, NotUsed]                  = Source(1 to 10)

  def main(args: Array[String]): Unit = {
    source.via(actorPoweredFlow).to(Sink.ignore).run()
  }
}

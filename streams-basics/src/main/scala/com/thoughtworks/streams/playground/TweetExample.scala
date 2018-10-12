package com.thoughtworks.streams.playground

import akka.NotUsed
import akka.stream.scaladsl._
import akka.stream.{ClosedShape, IOResult}
import com.thoughtworks.streams.GlobalVariables._
import com.thoughtworks.streams.playground.TweetExample.{tweets, Author, HashTag, Tweet}

import scala.concurrent.Future

object TweetExample {

  final case class Author(handle: String)

  final case class HashTag(name: String)

  final case class Tweet(author: Author, timestamp: Long, body: String) {
    def hashTags: Set[HashTag] =
      body
        .split(" ")
        .collect {
          case t if t.startsWith("#") ⇒ HashTag(t.replaceAll("[^#\\w]", ""))
        }
        .toSet
  }

  abstract class TweetSourceDecl {
    val tweets: Source[Tweet, NotUsed]
  }

  val tweets: Source[Tweet, NotUsed] = Source(
    Tweet(Author("rolandkuhn"), System.currentTimeMillis, "#akka rocks!") ::
    Tweet(Author("patriknw"), System.currentTimeMillis, "#akka !") ::
    Tweet(Author("bantonsson"), System.currentTimeMillis, "#akka !") ::
    Tweet(Author("drewhk"), System.currentTimeMillis, "#akka !") ::
    Tweet(Author("ktosopl"), System.currentTimeMillis, "#akka on the rocks!") ::
    Tweet(Author("mmartynas"), System.currentTimeMillis, "wow #akka !") ::
    Tweet(Author("akkateam"), System.currentTimeMillis, "#akka rocks!") ::
    Tweet(Author("bananaman"), System.currentTimeMillis, "#bananas rock!") ::
    Tweet(Author("appleman"), System.currentTimeMillis, "#apples rock!") ::
    Tweet(Author("drama"), System.currentTimeMillis, "we compared #apples to #oranges!") ::
    Nil
  )
}

object Runner {
  val akkaTag = HashTag("#akka")

  val authors: Source[Author, NotUsed] = {
    tweets
      .filter(_.hashTags.contains(akkaTag))
      .map(_.author)
  }

  val tags: Source[String, NotUsed] = {
    tweets
      .map(_.hashTags)
      .reduce(_ ++ _)
      .mapConcat(identity)
      .map(_.name.toUpperCase)
  }

  val writeAuthors: Sink[Author, Future[IOResult]]   = lineSink[Author]("authors.txt")
  val writeHashTags: Sink[HashTag, Future[IOResult]] = lineSink[HashTag]("hashTag.txt")

  val g: RunnableGraph[NotUsed] = RunnableGraph.fromGraph(GraphDSL.create() { implicit b =>
    import GraphDSL.Implicits._

    val bcast = b.add(Broadcast[Tweet](2))
    tweets ~> bcast.in
    bcast.out(0) ~> Flow[Tweet].map(_.author) ~> writeAuthors
    bcast.out(1) ~> Flow[Tweet].mapConcat(_.hashTags.toList) ~> writeHashTags
    ClosedShape
  })

  val counterGraph: RunnableGraph[Future[Int]] =
    tweets
      .via(count)
      .toMat(sumSink)(Keep.right)

  def main(args: Array[String]): Unit = {
    toConsole(tags)
    toConsole(authors)
    g.run()
  }
}

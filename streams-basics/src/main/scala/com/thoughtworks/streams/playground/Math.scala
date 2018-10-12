package com.thoughtworks.streams.playground
import akka.NotUsed
import akka.stream.scaladsl.Source

object Math {
  def factorial(n: Int): Option[Source[Int, NotUsed]] = {
    if (n < 0) None
    else {
      val nn = if (n == 0) 1 else n
      Some(Source(1 to nn).reduce(_ * _))
    }
  }

}

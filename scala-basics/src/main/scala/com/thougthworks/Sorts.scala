package com.thougthworks

object Sorts {
  def quickSort(ints: Seq[Int])(implicit comparator: (Int, Int) => Boolean): Seq[Int] = {

    def go(base: Int, left: Seq[Int], right: Seq[Int], remaining: Seq[Int]): Seq[Int] = {
      remaining match {
        case Nil => (left :+ base) ++ right
        case _ =>
          val head = remaining.head
          if (comparator(base, head)) {
            go(base, left, quickSort(right :+ head)(comparator), remaining.tail)
          } else go(base, quickSort(left :+ head)(comparator), right, remaining.tail)
      }
    }

    ints match {
      case Nil => ints
      case _   => go(ints.head, Seq.empty, Seq.empty, ints.tail)
    }
  }
}

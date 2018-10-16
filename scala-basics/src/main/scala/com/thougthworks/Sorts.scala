package com.thougthworks

object Sorts {
  def quickSort[A](ints: Seq[A])(implicit comparator: (A, A) => Boolean): Seq[A] = {

    def go(base: A, left: Seq[A], right: Seq[A], remaining: Seq[A]): Seq[A] = {
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

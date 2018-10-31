package com.thoughtworks.dataStructure

sealed trait List[+A] {
  def dropWhile(predicate: A => Boolean): List[A]

  def drop(n: Int): List[A]

  def tail: List[A]

  def head: A

  def foldLeft[B](initial: => B)(f: (B, A) => B): B

  def reverse: List[A]
}

case object Nil extends List[Nothing] {

  override def tail: List[Nothing] = throwException("tail of empty list")

  override def head: Nothing = throwException("head of empty list")

  override def drop(n: Int): List[Nothing] = this

  override def dropWhile(predicate: Nothing => Boolean): List[Nothing] = this

  private def throwException(message: String) = {
    throw new UnsupportedOperationException(message)
  }
  override def foldLeft[B](initial: => B)(f: (B, Nothing) => B): B = initial

  override def reverse: List[Nothing] = this
}

case class Cons[+A](h: A, t: List[A]) extends List[A] {
  override def tail: List[A] = t

  override def head: A = h

  override def drop(n: Int): List[A] = {
    def go(cons: List[A], n: Int): List[A] = {
      if (n > 0) {
        cons match {
          case Cons(_, rt) => go(rt, n - 1)
          case Nil         => Nil
        }
      } else cons
    }

    go(this, n)
  }

  override def dropWhile(predicate: A => Boolean): List[A] = {
    def go(predicate: A => Boolean, list: List[A]): List[A] = {
      list match {
        case Nil        => Nil
        case a: Cons[A] => if (predicate(a.head)) go(predicate, a.tail) else a
      }
    }

    go(predicate, this)
  }

  override def foldLeft[B](initial: => B)(f: (B, A) => B): B = {
    def go(acc: B, list: List[A]): B = {
      list match {
        case Cons(head, tail) => go(f(acc, head), tail)
        case _                => acc
      }
    }

    go(initial, this)
  }

  override def reverse: List[A] = {
    def go(reversed: List[A], original: List[A]): List[A] = {
      original match {
        case Cons(head, tail) => go(Cons(head, reversed), tail)
        case _                => reversed
      }
    }

    go(List.empty, this)
  }
}

object List {
  def concat[A](lists: List[A]*): List[A] = {
    def go(concatenatedList: List[A], remainingLists: Seq[List[A]]): List[A] = {
      if (remainingLists.isEmpty) concatenatedList
      else go(append(concatenatedList, remainingLists.head), remainingLists.tail)
    }
    go(List.empty, lists)
  }

  def apply[A](vals: A*): List[A] = {

    @scala.annotation.tailrec
    def go(cons: List[A], vals: A*): List[A] = {
      if (vals.isEmpty) cons
      else go(Cons(vals.head, cons), vals.tail: _*)
    }

    go(Nil, vals.reverse: _*)

//     Recursive solution
    /*def recursiveGo(vals: A*): List[A] = {
      if (vals.isEmpty) Nil
      else Cons(vals.head, recursiveGo(vals.tail: _*))
    }

    recursiveGo(vals: _*)*/
  }

  def empty[A]: List[A] = Nil

  def sum(ints: List[Int], acc: Int = 0): Int = {
    ints match {
      case Nil        => acc
      case Cons(h, t) => sum(t, acc + h)
    }
  }

  def append[A](l1: List[A], l2: List[A]): List[A] = {
    l1 match {
      case Nil        => l2
      case Cons(h, t) => Cons(h, append(t, l2))
    }
  }

}

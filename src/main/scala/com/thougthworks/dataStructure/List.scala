package com.thougthworks.dataStructure

sealed trait List[+A] {
  def dropWhile(predicate: A => Boolean): List[A]

  def drop(n: Int): List[A]

  def tail: List[A]

  def head: A
}

case object Nil extends List[Nothing] {

  override def tail: List[Nothing] = throwException("tail of empty list")

  override def head: Nothing = throwException("head of empty list")

  override def drop(n: Int): List[Nothing] = this

  override def dropWhile(predicate: Nothing => Boolean): List[Nothing] = this

  private def throwException(message: String) = {
    throw new UnsupportedOperationException(message)
  }

}

case class Cons[+A](h: A, t: List[A]) extends List[A] {
  override def tail: List[A] = t

  override def head: A = h

  override def drop(n: Int): List[A] = {
    def go(cons: List[A], n: Int): List[A] = {
      if (n > 0) {
        cons match {
          case Cons(_, rt) => go(rt, n - 1)
          case Nil => Nil
        }
      } else cons
    }

    go(this, n)
  }

  override def dropWhile(predicate: A => Boolean): List[A] = {
    def go(predicate: A => Boolean, list: List[A]): List[A] = {
      list match {
        case Nil => Nil
        case a: Cons[A] => if (predicate(a.head)) go(predicate, a.tail) else a
      }
    }

    go(predicate, this)
  }
}

object List {

  def apply[A](vals: A*): List[A] = {

    def go(cons: List[A], vals: A*): List[A] = {
      if (vals.isEmpty) cons
      else go(Cons(vals.head, cons), vals.tail: _*)
    }

    go(Nil, vals.reverse: _*)

    /* Recursive solution
    def recursiveGo(vals: A*): List[A] = {
      if (vals.isEmpty) Nil
      else Cons(vals.head, recursiveGo(vals.tail: _*))
    }

    recursiveGo(vals: _*)
    */
  }

  def empty[A]: List[A] = Nil

  def sum(ints: List[Int], acc: Int = 0): Int = {
    ints match {
      case Nil => acc
      case Cons(h, t) => sum(t, acc + h)
    }
  }

}
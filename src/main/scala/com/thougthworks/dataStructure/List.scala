package com.thougthworks.dataStructure

sealed trait List[+A]

case object Nil extends List[Nothing]

case class Cons[+A](head: A, tail: List[A]) extends List[A]

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

  def sum(ints: List[Int], acc: Int = 0): Int = {
    ints match {
      case Nil => acc
      case Cons(h, t) => sum(t, acc + h)
    }
  }
}
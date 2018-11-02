package com.thoughtworks.dataStructure

sealed trait MyList[+A] {

  def dropWhile(predicate: A => Boolean): MyList[A]

  def drop(n: Int): MyList[A]

  def tail: MyList[A]

  def head: A

  def foldLeft[B](initial: => B)(f: (B, A) => B): B

  def reverse: MyList[A]
}

case object Nil extends MyList[Nothing] {

  override def tail: MyList[Nothing] = throwException("tail of empty list")

  override def head: Nothing = throwException("head of empty list")

  override def drop(n: Int): MyList[Nothing] = this

  override def dropWhile(predicate: Nothing => Boolean): MyList[Nothing] = this

  private def throwException(message: String) = {
    throw new UnsupportedOperationException(message)
  }

  override def foldLeft[B](initial: => B)(f: (B, Nothing) => B): B = initial

  override def reverse: MyList[Nothing] = this
}

case class Cons[+A](h: A, t: MyList[A]) extends MyList[A] {
  override def tail: MyList[A] = t

  override def head: A = h

  override def drop(n: Int): MyList[A] = {
    def go(cons: MyList[A], n: Int): MyList[A] = {
      if (n > 0) {
        cons match {
          case Cons(_, rt) => go(rt, n - 1)
          case Nil         => Nil
        }
      } else cons
    }

    go(this, n)
  }

  override def dropWhile(predicate: A => Boolean): MyList[A] = {
    def go(predicate: A => Boolean, list: MyList[A]): MyList[A] = {
      list match {
        case Nil        => Nil
        case a: Cons[A] => if (predicate(a.head)) go(predicate, a.tail) else a
      }
    }

    go(predicate, this)
  }

  override def foldLeft[B](initial: => B)(f: (B, A) => B): B = {
    def go(acc: B, list: MyList[A]): B = {
      list match {
        case Cons(head, tail) => go(f(acc, head), tail)
        case _                => acc
      }
    }

    go(initial, this)
  }

  override def reverse: MyList[A] = {
    def go(reversed: MyList[A], original: MyList[A]): MyList[A] = {
      original match {
        case Cons(head, tail) => go(Cons(head, reversed), tail)
        case _                => reversed
      }
    }

    go(MyList.empty, this)
  }

}

object MyList {
  def concat[A](lists: MyList[A]*): MyList[A] = {
    def go(concatenatedList: MyList[A], remainingLists: Seq[MyList[A]]): MyList[A] = {
      if (remainingLists.isEmpty) concatenatedList
      else go(append(concatenatedList, remainingLists.head), remainingLists.tail)
    }
    go(MyList.empty, lists)
  }

  def apply[A](vals: A*): MyList[A] = {

    @scala.annotation.tailrec
    def go(cons: MyList[A], vals: A*): MyList[A] = {
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

  def empty[A]: MyList[A] = Nil

  def sum(ints: MyList[Int], acc: Int = 0): Int = {
    ints match {
      case Nil        => acc
      case Cons(h, t) => sum(t, acc + h)
    }
  }

  def append[A](l1: MyList[A], l2: MyList[A]): MyList[A] = {
    l1 match {
      case Nil        => l2
      case Cons(h, t) => Cons(h, append(t, l2))
    }
  }

  def +:[A](list: MyList[A], a: A): MyList[A] = {
    def go(list: MyList[A], a: A): MyList[A] = list match {
      case Nil          => Cons(a, Nil)
      case Cons(h, Nil) => Cons(h, Cons(a, Nil))
      case Cons(h, t)   => Cons(h, go(t, a))
    }
    go(list, a)
  }

}

package com.thoughtworks.dataStructure
import com.thoughtworks.dataStructure.MyStream.{cons, empty}

trait MyStream[+A] {
  def uncons: Option[(A, MyStream[A])]

  def isEmpty: Boolean = uncons.isEmpty

  def toList: List[A] = uncons match {
    case None         => List.empty[A]
    case Some((h, t)) => List(h) ++ t.toList
  }

  def takeWhile(predicate: A => Boolean): MyStream[A] = uncons match {
    case Some((h, t)) if predicate(h) => cons(h, t.takeWhile(predicate))
    case _                            => empty[A]
  }

  def takeWhile_1(f: A => Boolean): MyStream[A] =
    foldRight(empty[A])(
      (h, t) =>
        if (f(h)) cons(h, t)
        else empty
    )

  def take(noOfElements: Int): MyStream[A] = uncons match {
    case Some((h, t)) if noOfElements > 0 => cons(h, t.take(noOfElements - 1))
    case _                                => empty[A]
  }

  def foldRight[B](z: => B)(f: (A, => B) => B): B = uncons match {
    case Some((h, t)) => f(h, t.foldRight(z)(f))
    case None         => z
  }

  def exists(predicate: A => Boolean): Boolean = foldRight(false)((a, b) => predicate(a) || b)

  def forAll(predicate: A => Boolean): Boolean = foldRight(true)((a, b) => if (predicate(a)) b else false)

  def map[B](mapper: A => B): MyStream[B] = foldRight(empty[B])((a, b) => cons(mapper(a), b))

  def filter(predicate: A => Boolean): MyStream[A] = foldRight(empty[A])((a, b) => if (predicate(a)) cons(a, b) else b)

}

object MyStream {

  def empty[A]: MyStream[A] = new MyStream[A] {
    lazy val uncons: Option[(A, MyStream[A])] = None

    def append(a: A): MyStream[A] = MyStream(a)

  }

  def cons[A](h: => A, t: => MyStream[A]): MyStream[A] = new MyStream[A] {
    lazy val uncons: Option[(A, MyStream[A])] = Some((h, t))

  }

  def apply[A](as: A*): MyStream[A] = {
    if (as.isEmpty) empty[A]
    else cons(as.head, apply(as.tail: _*))
  }

}

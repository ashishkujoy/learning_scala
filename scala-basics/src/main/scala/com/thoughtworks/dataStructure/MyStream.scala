package com.thoughtworks.dataStructure
import com.thoughtworks.dataStructure.MyStream.{cons, empty, unfold}

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

  def concat[B >: A](s: => MyStream[B]): MyStream[B] = foldRight(s)((h, t) => cons(h, t))

  def flatMap[B](f: A => MyStream[B]): MyStream[B] = foldRight(empty[B])((h, t) => f(h).concat(t))

  def zip[B](that: => MyStream[B]): MyStream[(A, B)] =
    uncons match {
      case None => empty[(A, B)]
      case Some((h, t)) =>
        that.uncons match {
          case None           => empty[(A, B)]
          case Some((th, tt)) => cons((h, th), t.zip(tt))
        }
    }

  def zipWith[B, C](s2: MyStream[B])(f: (A, B) => C): MyStream[C] = {
    unfold((uncons, s2.uncons)) {
      case (Some((h1, t1)), Some((h2, t2))) => Some((f(h1, h2), (t1.uncons, t2.uncons)))
      case _                                => None
    }
  }

  def head: A = uncons match {
    case None         => throw new Exception("Head of empty MyStream")
    case Some((h, t)) => h
  }

  def tail: MyStream[A] = uncons match {
    case None         => throw new Exception("Tail of empty MyStream")
    case Some((h, t)) => t
  }

}

object MyStream {

  def empty[A]: MyStream[A] = new MyStream[A] {
    lazy val uncons: Option[(A, MyStream[A])] = None

    def append(a: A): MyStream[A] = MyStream(a)

  }

  def cons[A](h: => A, t: => MyStream[A]): MyStream[A] = new MyStream[A] {
    lazy val uncons: Option[(A, MyStream[A])] = Some((h, t))

  }

  def constant[A](a: A): MyStream[A] = cons(a, constant(a))

  def from(a: Int): MyStream[Int] = cons(a, from(a + 1))

  def fibonacci: MyStream[Int] = {
    def go(a: Int, b: Int): MyStream[Int] = {
      val nextTerm = a + b
      cons(nextTerm, go(b, nextTerm))
    }
    apply(0, 1).concat(go(0, 1))
  }

  def unfold[A, S](z: S)(f: S => Option[(A, S)]): MyStream[A] = f(z) match {
    case Some((a, s)) => cons(a, unfold(s)(f))
    case None         => empty[A]
  }

  def apply[A](as: A*): MyStream[A] = {
    if (as.isEmpty) empty[A]
    else cons(as.head, apply(as.tail: _*))
  }

  def startsWith[A](s1: MyStream[A], s2: MyStream[A]): Boolean = {
    def go(starts: Boolean, s1: MyStream[A], s2: MyStream[A]): Boolean = {
      if (starts) (s1.uncons, s2.uncons) match {
        case (Some((h1, t1)), Some((h2, t2))) => if (h1 != h2) false else go(starts = true, t1, t2)
        case (None, Some(_))                  => false
        case _                                => true
      } else starts
    }

    go(starts = true, s1, s2)
  }

}

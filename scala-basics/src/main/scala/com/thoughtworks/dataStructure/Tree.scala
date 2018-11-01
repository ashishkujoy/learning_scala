package com.thoughtworks.dataStructure

sealed trait Tree[+A] {
  def size: Int

  def depth: Int

  def map[B](f: A => B): Tree[B]
}

case class Leaf[A](value: A) extends Tree[A] {
  override def size: Int                  = 1
  override def depth: Int                 = 0
  override def map[B](f: A => B): Tree[B] = Leaf(f(value))
}

case class Branch[A](left: Tree[A], right: Tree[A]) extends Tree[A] {

  override def size: Int = {
    (left, right) match {
      case (l @ Leaf(_), r @ Leaf(_)) => l.size + r.size
      case (l, r)                     => l.size + r.size
    }
  }

  override def depth: Int = {
    def depthOfEle(tree: Tree[A]): Int = {
      tree match {
        case leaf @ Leaf(_) => leaf.depth
        case Branch(l, r)   => 1 + l.depth.max(r.depth)
      }
    }
    1 + depthOfEle(left).max(depthOfEle(right))
  }

  override def map[B](f: A => B): Tree[B] = {
    (left, right) match {
      case (l @ Leaf(_), r @ Leaf(_)) => Branch(l.map(f), r.map(f))
      case (l, r)                     => Branch(l.map(f), r.map(f))
    }
  }
}

object Tree {
  def max(tree: Tree[Int]): Int = {
    tree match {
      case Leaf(value)  => value
      case Branch(l, r) => max(l).max(max(r))
    }
  }
}

package com.thoughtworks

import scala.language.experimental.macros
import scala.reflect.macros.blackbox

object Macros {
  def hello(): Unit = macro helloImpl

  def helloImpl(c: blackbox.Context)(): c.Expr[Unit] = {
    import c.universe._
    reify { println("Hello World!") }
  }

  def printParam(param: Any): Unit = macro printParamImpl

  def printParamImpl(c: blackbox.Context)(param: c.Expr[Any]): c.Expr[Unit] = {
    import c.universe._
    reify { println(param.splice) }
  }

  def debug(param: Any): String = macro debug_impl

  def debug_impl(c: blackbox.Context)(param: c.Expr[Any]): c.Expr[String] = {
    import c.universe._
    val paramRep     = show(param.tree)
    val paramRepTree = Literal(Constant(paramRep))
    val paramRepExpr = c.Expr[String](paramRepTree)
    reify { paramRepExpr.splice + " = " + param.splice }
  }

  def debug2(param: Any*): List[String] = macro impl

  def impl(c: blackbox.Context)(param: c.Expr[Any]*): c.Expr[List[String]] = ???

}

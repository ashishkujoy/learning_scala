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
    val paramRepExpr = toParamRefNameExp(c)(param)
    reify { paramRepExpr.splice + " = " + param.splice }
  }

//  def debug2(params: Any*): Map[String, String] = macro impl
//
//  def impl(c: blackbox.Context)(params: c.Expr[Any]*): c.Expr[Map[String, String]] = {
//    import c.universe._
//    val paramNames       = params.map(param => toParamRefNameExp(c)(param))
//    val paramNameToValue = Map.empty[String, String]
//    reify {
//      paramNames
//        .zip(params)
//        .foldLeft(paramNameToValue)(
//          (nameToValue, nameAndValue) => nameToValue.updated(nameAndValue._1.splice, nameAndValue._2.splice.asInstanceOf[String])
//        )
//    }
//  }

  private def toParamRefNameExp(c: blackbox.Context)(param: c.Expr[Any]): c.Expr[String] = {
    import c.universe._
    val paramRep     = show(param.tree)
    val paramRepTree = Literal(Constant(paramRep))
    c.Expr[String](paramRepTree)
  }
}

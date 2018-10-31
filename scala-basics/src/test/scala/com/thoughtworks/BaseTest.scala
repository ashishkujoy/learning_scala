package com.thoughtworks
import org.scalatest.{BeforeAndAfterAll, BeforeAndAfterEach, FunSuite, Matchers}

abstract class BaseTest extends FunSuite with Matchers with BeforeAndAfterEach with BeforeAndAfterAll {}

package com.thoughtworks

class MacroTest extends BaseTest {

  test("should give param value with param name") {
    val student = "John"
    val str     = Macros.debug(student)
    str shouldBe "student = John"
  }

}

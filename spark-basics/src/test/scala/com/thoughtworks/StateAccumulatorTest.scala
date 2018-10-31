package com.thoughtworks

class StateAccumulatorTest extends BaseTest {
  private var accumulator: StateAccumulator = _

  override protected def beforeEach(): Unit = {
    accumulator = new StateAccumulator
  }

  test("should give true for empty accumulator") {
    accumulator.isZero shouldBe true
  }

  test("should give false for non-empty accumulator") {
    accumulator.add(YearPopulation(2018, 123L))
    accumulator.isZero shouldBe false
  }

  test("should give a new accumulator with same value") {
    accumulator.add(YearPopulation(2019, 3321312312L))

    val copiedAccumulator = accumulator.copy()

    accumulator.value shouldBe copiedAccumulator.value
  }

  test("should reset the value of accumulator") {
    accumulator.add(YearPopulation(2019, 3321312312L))

    accumulator.reset()

    accumulator.value shouldBe YearPopulation(0, 0)
  }

  test("should merge the value of two accumulators") {
    accumulator.add(YearPopulation(2019, 200L))
    val anotherAccumulator = new StateAccumulator
    anotherAccumulator.add(YearPopulation(2001, 100L))

    accumulator.merge(anotherAccumulator)

    accumulator.value shouldBe YearPopulation(4020, 300L)
  }
}

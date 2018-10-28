package com.thoughtworks
import org.apache.spark.util.AccumulatorV2

class StateAccumulator extends AccumulatorV2[YearPopulation, YearPopulation] {
  private var year       = 0
  private var population = 0L

  override def isZero: Boolean = year == 0

  override def copy(): StateAccumulator = {
    val stateAccumulator = new StateAccumulator
    stateAccumulator.year = year
    stateAccumulator.population = population
    stateAccumulator
  }

  override def reset(): Unit = {
    year = 0
    population = 0L
  }

  override def add(v: YearPopulation): Unit = {
    year += v.year
    population += v.population
  }

  override def merge(other: AccumulatorV2[YearPopulation, YearPopulation]): Unit = {
    other match {
      case stateAccumulator: StateAccumulator =>
        year += stateAccumulator.year
        population += stateAccumulator.population
      case _ =>
    }

  }
  override def value: YearPopulation = YearPopulation(year, population)
}

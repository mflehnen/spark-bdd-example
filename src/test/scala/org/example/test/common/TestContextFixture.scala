package org.example.test.common

import org.apache.spark.sql.{DataFrame, Row}

import scala.collection.mutable

trait TestContextFixture {

  protected lazy val dataFrameMap: mutable.Map[String, DataFrame] = mutable.Map[String, DataFrame]()
  protected lazy val rowValuesMap: mutable.Map[String, Array[Row]] = mutable.Map[String, Array[Row]]()
}

package org.example.test.common

import org.apache.spark.sql.types.{StringType, StructField, StructType}
import org.apache.spark.sql.{DataFrame, Row, SparkSession}

import scala.collection.convert.ImplicitConversions.`list asScalaBuffer`

object GherkinTableConverter {

  def asDataFrame(spark: SparkSession, dataTable: io.cucumber.datatable.DataTable): DataFrame = {

    val headers: Seq[String] = dataTable.asLists().head.toSeq
    val rows: Seq[Row] = dataTable.asLists().tail.map(f => {
      Row.fromSeq(f.toSeq)
    }).toSeq

    val tableSchema: StructType = StructType(headers.map(f => StructField(f, StringType)))
    val df = spark.createDataFrame(spark.sparkContext.parallelize(rows), schema = tableSchema)
    df
  }

}

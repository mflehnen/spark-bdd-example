package org.example.test.common

import org.apache.spark.SparkConf
import org.apache.spark.sql.SparkSession
import org.scalatest.BeforeAndAfterAll
import org.scalatest.funsuite.AnyFunSuite

/**
 * Provides the basic session and context for all the tests
 */
class SparkTestSuite extends AnyFunSuite with BeforeAndAfterAll {
  // Define spark session config
  private lazy val sparkConf = {
    val conf = new SparkConf()
    conf.set("dfs.client.read.shortcircuit.skip.checksum", "true")
    conf.set("spark.memory.offHeap.enabled", "false")
    conf.set("spark.executor.memory", "4g")
    conf.set("spark.executor.memoryOverhead", "1g")
    conf.set("spark.ui.enabled", "true")
    conf
  }

  // Define SparkSession as a member variable
  @transient protected lazy val spark: SparkSession = {
    val sparkSession = SparkSession.builder()
      .config(sparkConf)
      .master("local[*]")
      .appName("Unit test")
      .getOrCreate()
    val sparkId = sparkSession.sparkContext.hashCode()
    println(s"Initiating SparkSession with hashCode: $sparkId.")
    // change spark log level only for tests
    sparkSession.sparkContext.setLogLevel("WARN")
    sparkSession
  }

  // Initialize SparkSession before running tests
  override def beforeAll(): Unit = {
    super.beforeAll()
    // Enforce spark initialization
    spark.sparkContext.hashCode()
  }

  // Clean up resources after running tests
  override def afterAll(): Unit = {
    try {
      // Stop SparkSession
      if (spark != null) {
        spark.stop()
        println("Stopping sparkSession")
      }
    } finally {
      super.afterAll()
    }
  }

}
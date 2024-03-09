package org.example.test.steps.settlement

import io.cucumber.scala.{EN, ScalaDsl}
import org.apache.spark.sql.Row
import org.example.MyDataTransformation
import org.example.test.common.{GherkinTableConverter, SparkTestSuite, TestContextFixture}

class SettlementSteps extends SparkTestSuite with ScalaDsl with EN  with TestContextFixture {

  Before {
    // setup any pre-requirements
    dataFrameMap.clear()
    rowValuesMap.clear()
  }

  After {
    // cleaning resources
    println("Cleanup")
  }

  Given("""That a I have an external table with metrics about the seller relationship:""") { (dataTable: io.cucumber.datatable.DataTable) =>
    // Write code here that turns the phrase above into concrete actions
    val df = GherkinTableConverter.asDataFrame(spark, dataTable)
    dataFrameMap += ("metrics_data" -> df)
  }
  When("""I have a bunch of transactions from all the sellers at the end of the month""") { (dataTable: io.cucumber.datatable.DataTable) =>
    // Write code here that turns the phrase above into concrete actions
    val df = GherkinTableConverter.asDataFrame(spark, dataTable)
    dataFrameMap += ("source_data" -> df)
  }
  When("""The seller is not currently blocked:""") { (dataTable: io.cucumber.datatable.DataTable) =>
    // Write code here that turns the phrase above into concrete actions
    val df = GherkinTableConverter.asDataFrame(spark, dataTable)
    dataFrameMap += ("seller_data" -> df)
  }
  When("""The seller is not listed int the fraud suspicion list:""") { (dataTable: io.cucumber.datatable.DataTable) =>
    // Write code here that turns the phrase above into concrete actions
    val df = GherkinTableConverter.asDataFrame(spark, dataTable)
    dataFrameMap += ("fraud_data" -> df)
  }
  When("""The seller has completed a minimum of {int} transactions per day in the last {int} months""") { (txCount: Int, qtMonths: Int) =>
    // Write code here that turns the phrase above into concrete actions
    // TODO: implement-me
  }
  When("""The company has a special promotion {string} for the month in the seller's region""") { (string: String) =>
    // Write code here that turns the phrase above into concrete actions
    // TODO: implement-me
  }
  When("""I run the tax calculation job""") { () =>
    // Write code here that turns the phrase above into concrete actions
    val transactionsData = dataFrameMap("source_data")
    val sellersData = dataFrameMap("seller_data")
    val fraudData = dataFrameMap("fraud_data")

    val settlementsData = MyDataTransformation.calculateTaxRate(transactionsData, sellersData, fraudData)
    rowValuesMap += ("settlements" -> settlementsData.collect())
    settlementsData.show(truncate = false)
  }
  Then("""The {double} and {double} calculated to settle the transactions for each {string} with the {string} should match as the following:""") { (taxRate: Double, taxAmount: Double,
                                                                                                                                                    sellerId: String, taxCondition: String) =>
    // Write code here that turns the phrase above into concrete actions
    val rows: Array[Row] = rowValuesMap("settlements")
    val row: Row = rows.filter(r => r.getAs[String]("Seller_Id") == sellerId).head

    println("OK")
    assert(row.getAs[Double]("Tax_Rate") == taxRate, "Tax_Rate")
    assert(row.getAs[Double]("Tax_Amount") == taxAmount, "Tax_Amount")
    assert(row.getAs[String]("Tax_Condition") == taxCondition, "Tax_Condition")
  }


}

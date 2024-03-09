package org.example.test.steps.silver

import io.cucumber.scala.{EN, ScalaDsl}
import org.apache.spark.sql.DataFrame
import org.example.MyDataTransformation
import org.example.test.common.{GherkinTableConverter, SparkTestSuite, TestContextFixture}



class SilverTableSteps extends SparkTestSuite with ScalaDsl with EN with TestContextFixture {

  Before {
    // setup any pre-requirements
    dataFrameMap.clear()
  }

  After {
    // cleaning resources
    println("Cleanup")
  }

  When("""I receive a streaming of data from my bronze table like the following:""") { (dataTable: io.cucumber.datatable.DataTable) =>
    // Write code here that turns the phrase above into concrete actions
    val df = GherkinTableConverter.asDataFrame(spark, dataTable)
    dataFrameMap += ("source_data" -> df)
    df.show(truncate = false)

  }
  When("""I trigger the data transformation for silver table""") { () =>
    // Write code here that turns the phrase above into concrete actions
    val sourceData: DataFrame = dataFrameMap("source_data")
    val (validData, recycleData) = MyDataTransformation.myDataTransformationFunction(sourceData)
    dataFrameMap += ("valid_data" -> validData, "recycle_data" -> recycleData)
  }
  Then("""I should have in silver table only {int} distinct rows""") { (expectedCount: Int) =>
    // Write code here that turns the phrase above into concrete actions
    val validData: DataFrame = dataFrameMap("valid_data")
    assert(expectedCount == validData.count(), "Silver Count")
  }

  Then("""I should have in silver table only the {int} rows with positive amount values""") { (expectedCount: Int) =>
    // Write code here that turns the phrase above into concrete actions
    val validData: DataFrame = dataFrameMap("valid_data")
      .filter("Amount > 0")
    assert(expectedCount == validData.count(), "Positive Amount Row Count")
  }
  Then("""the {int} rows violating the constraints should be moved into recycle""") { (expectedCount: Int) =>
    // Write code here that turns the phrase above into concrete actions
    val recycleData: DataFrame = dataFrameMap("recycle_data")
    assert(expectedCount == recycleData.count(), "Recycle Expected Count")
  }

}

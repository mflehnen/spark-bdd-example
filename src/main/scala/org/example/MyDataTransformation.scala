package org.example

import org.apache.spark.sql.functions.expr
import org.apache.spark.sql.{DataFrame, functions}

object MyDataTransformation {


  /**
   * A Very simple transformation example
   * */
  def myDataTransformationFunction(df: DataFrame): (DataFrame, DataFrame) = {
    val deduplicatedData = df.dropDuplicates()

    val validData: DataFrame = deduplicatedData.filter("amount > 0")
    val recycleData: DataFrame = deduplicatedData.filter("amount <= 0")

    (validData, recycleData)
  }

  /**
   * A more complex transformation example
   * */
  def calculateTaxRate(transactionsData: DataFrame, sellersData: DataFrame, fraudData: DataFrame): DataFrame = {

    val spark = transactionsData.sparkSession
    val taxData = spark.createDataFrame(Seq(
      ("DEFAULT", 1.5),
      ("SPECIAL", 0.7),
      ("FRAUD", 0.0),
      ("BLOCKED", 0.0))).toDF("Tax_Condition", "Tax_Rate")

    val aggregatedData = transactionsData.groupBy("Seller_Id")
      .agg(functions.sum("Amount").alias("Amount"))

    val transformedData = aggregatedData.alias("a")
      .join(sellersData.alias("s"), usingColumn = "Seller_Id", joinType = "left")
      .join(fraudData.alias("f"), usingColumn = "Seller_Id", joinType = "left")
      .withColumn("Tax_Condition", expr(
        """
            CASE
                WHEN f.Seller_Id IS NOT NULL THEN 'FRAUD'
                WHEN s.Status = 'Blocked' THEN 'BLOCKED'
                WHEN s.Seller_Id = 'S2' THEN 'SPECIAL'
                ELSE 'DEFAULT'
            END """))
      .join(taxData.alias("t"), usingColumn = "Tax_Condition")
      .selectExpr("a.Seller_Id", "a.Amount as Base_Amount", "t.Tax_Condition", "t.Tax_Rate", "round(a.Amount * t.Tax_Rate / 100, 1) as Tax_Amount")

    transformedData
  }


}

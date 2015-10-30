/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

// scalastyle:off println
package org.apache.spark.examples.streaming

import org.apache.spark.mllib.linalg.Vectors
import org.apache.spark.mllib.regression.{LabeledPoint, LinearRegressionWithSGD}
import org.apache.spark.{SparkConf, SparkContext}

/**
 * Counts words in new text files created in the given directory
 * Usage: HdfsWordCount <directory>
 *   <directory> is the directory that Spark Streaming will use to find and read new text files.
 *
 * To run this on your local machine on directory `localdir`, run this example
 *    $ bin/run-example \
 *       org.apache.spark.examples.streaming.HdfsWordCount localdir
 *
 * Then create a text file in `localdir` and the words in the file will get counted.
 */




object TemperatureRegLin {

  def main(args: Array[String]) {
//    if (args.length < 1) {
//      System.err.println("Usage: HdfsWordCount <directory>")
//      System.exit(1)
//    }
//
//
//    val customSchema = StructType(Array(
//      StructField("Station Number", IntegerType, true),
//      StructField("WBAN", StringType, true),
//      StructField("Day", StringType, true),
//      StructField("Mean", StringType, true)
//    )
//    )
//
//    ,,, ,Mean Sea Level Pressure,Mean Station Pressure,Mean Visibility,Mean Wind Speed,Maximum Sustained Wind Speed,Maximum Wind Gust,Maximum Temperature,Minimum Temperature,Precipitation Amount,Snow Depth,Fog,Rain or Drizzle,Snow or Ice Pellet,Hail,Thunder,Tornado,Temperature
//

    val conf = new SparkConf()
      .setMaster("local[2]")
      .setAppName("CountingSheep")

    val sc = new SparkContext(conf)
    sc.setLogLevel("WARN")
    val sqlContext = new org.apache.spark.sql.SQLContext(sc)


    // this is used to implicitly convert an RDD to a DataFrame.

    val df = sqlContext.read
    .format("com.databricks.spark.csv")
    .option("header", "true") // Use first line of all files as header
    .option("inferSchema", "true") // Automatically infer data types
    .load("temperature.csv")



    val selectedData = df.select("Day","Temperature", "Mean Station Pressure", "Mean Visibility")
//
//    val parsedData = selectedData.map { line =>
//      val parts = line.split(',')
//      LabeledPoint(parts(0).toDouble, Vectors.dense(parts(1).split(' ').map(_.toDouble)))
//    }.cache()



    val data  =   selectedData.map { row =>
      val features = Vectors.dense(row(1).asInstanceOf[Double])
     LabeledPoint(row(0).asInstanceOf[Int].toDouble, features)
    }.cache()

    val splits = data.randomSplit(Array(0.8, 0.2), seed = 11L)



    val training = splits(0) cache
    val test = splits(1) cache
//
//    val dv = selectedData.foreach(row => Vectors.dense(row.get(0).toI,row.get(1)))


    selectedData.show()

    val algorithm = new LinearRegressionWithSGD()
    var regression = new LinearRegressionWithSGD().setIntercept(true)
    regression.optimizer.setStepSize(0.1)

    val model = regression run training


    // Evaluate model on training examples and compute training error
    val prediction = model predict(test map(_ features))


//    prediction.foreach((result) => println(s"predicted label: ${result._1}, actual label: ${result}"))
//    val valuesAndPreds = data.map { point =>

//      val prediction = model predict(test map(_ features))
//      (point.label, prediction)
//    }

    val predictionAndLabel = prediction zip(test map(_ label))

    predictionAndLabel.foreach((result) => println(s"predicted label: ${result._1.asInstanceOf[Double].toInt}, actual label: ${result._2.asInstanceOf[Double].toInt}"))

//
//    val MSE = valuesAndPreds.map{case(v, p) => math.pow((v - p), 2)}.mean()
//    println("training Mean Squared Error = " + MSE)
//
//    predictionAndLabel.foreach((result) => println(s"predicted label: ${result._1}, actual label: ${result._2}"))
//

    //    selectedData.write
//      .format("com.databricks.spark.csv")
//      .option("header", "true")
//      .save("newcars.csv")
    }
}
// scalastyle:on println
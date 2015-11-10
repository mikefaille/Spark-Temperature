import com.github.nscala_time.time.StaticDateTimeFormat
import org.apache.spark.mllib.linalg.Vectors
import org.apache.spark.mllib.regression.{LabeledPoint, LinearRegressionWithSGD}
import org.apache.spark.{SparkConf, SparkContext}
import org.joda.time.format.DateTimeFormat
import org.joda.time.{DateTime => JodaDateTime}





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
      .setAppName("GSOD_ML")

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


    val fmt = DateTimeFormat.forPattern("yyyyMMdd");


    val data  =   selectedData.map { row =>

      val unixTime = StaticDateTimeFormat.forPattern("yyyyMMdd").parseDateTime(row(0).asInstanceOf[Int].toString).getMillis
      val features = Vectors.dense(row(1).asInstanceOf[Double])
     LabeledPoint(unixTime, features)
    }.cache()

    val splits = data.randomSplit(Array(0.8, 0.2), seed = 11L)



    val training = splits(0) cache
    val test = splits(1) cache
//
//    val dv = selectedData.foreach(row => Vectors.dense(row.get(0).toI,row.get(1)))


    selectedData.show()

    val algorithm = new LinearRegressionWithSGD()
    var regression = new LinearRegressionWithSGD().setIntercept(true)
    regression.optimizer.setStepSize(0.001)

    val model = regression run training


    // Evaluate model on training examples and compute training error
    val prediction = model predict(test map(_ features))


//    prediction.foreach((result) => println(s"predicted label: ${result._1}, actual label: ${result}"))
//    val valuesAndPreds = data.map { point =>

//      val prediction = model predict(test map(_ features))
//      (point.label, prediction)
//    }

    val predictionAndLabel = prediction zip(test map(_ label))

    predictionAndLabel.foreach((result) => println(s"predicted label: ${result._1}, actual label: ${result._2}"))

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

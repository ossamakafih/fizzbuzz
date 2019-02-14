package soat.fizzbuzz
import soat.fizzbuzz.Model.Log
import org.apache.spark.rdd.RDD

import org.apache.spark.{SparkConf, SparkContext}
import org.apache.spark.sql.types._
import org.apache.spark.sql.SparkSession

object LogProcessor {

  def process(spark: SparkSession): Unit= {


    val sc = spark.sparkContext

    val data = sc.textFile("tornik-map-20171006.10000.tsv")

    //Map the line of log to case class
    val logRDD: RDD[Log] = data.filter(log => log.contains("map")).map{ logs =>
      val log = logs.split("\t").map(_.trim)
      val url = log(1).split("/").map(_.trim)
      Log(log(0), log(1), url(4), url(6).toDouble)
    }.cache()


    val viewModeCount: RDD[(String, Int)]  = logRDD.map(log => (log.viewmode, 1)).reduceByKey(_ + _)
    val zoomRDD: RDD[(String, List[Double])] = logRDD.map(log => (log.viewmode, log.zoom)).groupByKey().map(l => (l._1, l._2.toList.distinct))

    //create the final RDD with the 3 fields
    val joinedRDD = viewModeCount.join(zoomRDD).map(log => (log._1, log._2._1, log._2._2 mkString ",")).cache()


    import spark.implicits._

    val colNames = Seq("viewmode", "count", "zoom")

    //create dataframe with final result
    val df = joinedRDD.toDF(colNames : _*)
    df.show()


    val finalResult = joinedRDD.collect().toList

    //format the final result to desired printing
    finalResult.foreach(item => println("%s \t %d \t %s".format(item._1, item._2, item._3)))
  }


  def main(args: Array[String]): Unit = {
    val conf = new SparkConf().setAppName("FizzBuzz Mappy").setMaster("local[*]")
    val spark = SparkSession.builder().config(conf).getOrCreate()
    process(spark)
  }

}

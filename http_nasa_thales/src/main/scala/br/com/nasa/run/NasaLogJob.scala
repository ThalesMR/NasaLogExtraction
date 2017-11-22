package br.com.nasa.run


import br.com.nasa.entity.extraction.Log

import org.apache.spark.{SparkConf, SparkContext}
import org.apache.spark.sql.SQLContext
import org.apache.spark.sql.functions._


/**

  * Created by Thales on 06/08/17.

  */

object NasaLogJob{
  def main(args: Array[String]): Unit = {

    val homeHDFS = args(0)
    val JOB_NAME = "NasaLogJob"
    // INITIAL CONFIGURATION
    val sconf = new SparkConf().setAppName(JOB_NAME)
    val sc = new SparkContext(sconf)
    val sqlContext = new SQLContext(sc)

    // LOAD THE DATASETS
    val LogDF = Log.load(homeHDFS,sc,sqlContext).cache()

    // DATASET PROCESSING
    import sqlContext.implicits._

//    val hostDistinctCount = LogDF.select("host").where( $"host" !== ""  ).distinct.count

    val dHost = LogDF.select("host").where( $"host" !== ""  ).distinct.count
    val count404 = LogDF.filter(($"host"!=="") && ($"status"===lit("404"))).count()
    val firstFiveUrlError = LogDF.filter(!($"status"===lit("404"))).map(row =>
      (row.getString(2), 1)).reduceByKey(_ + _).takeOrdered(5)(Ordering[Int].reverse.on(x=>x._2))
    val five = firstFiveUrlError.map(_._1)
    val codebyDay = LogDF.filter(($"host"!=="") && ($"status"===lit("404"))).map(x =>
      (x.getString(1),1)).reduceByKey(_ + _).collect()
    val totalbytes = LogDF.filter(($"bytes".isNotNull) && ($"bytes"!=="-") && ($"bytes"!==""))
      .map(row => (1,row.getString(4).toInt)).reduceByKey(_ + _).collect()

    import java.io._
    val file = new File(System.getProperty("user.dir")+"/respostas.txt")
    file.getParentFile.mkdirs
    file.createNewFile()

    val unicHost = "O número de host únicos é: "+dHost.toString
    val countThe404 = "\nO Total de erros 404 foi: "+count404.toString
    val urls404 = "\nAs URL's que mais causam erros 404 foram: "
    import java.io._
    val pw = new PrintWriter(file)
    pw.println(unicHost)
    pw.println(countThe404)
    pw.println(urls404)
    five.map(x=>pw.println(x.toString))
    codebyDay.map(x=>pw.println("\nNo dia "+x._1+" teve "+x._2.toString+" codigos 404"))
    totalbytes.map(x=>pw.println("\nO total de Bytes foi : "+x._2.toString))
    pw.close
    LogDF.unpersist()
    sc.stop()
  }
}

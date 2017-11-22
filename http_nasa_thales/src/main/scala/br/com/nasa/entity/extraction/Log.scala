package br.com.nasa.entity.extraction

/**
  * Created by thales on 06/08/17.
  */

import java.util.regex.Pattern
import org.apache.spark.SparkContext
import org.apache.spark.sql.{DataFrame, Row, SQLContext}
import org.apache.spark.sql.types.{StringType, StructField, StructType}


object Log {
  val regex = "(([^\\s]+) (\\S+) (\\S+) (\\[[^\\]]+\\]) \"([^\"]+)\" (\\d{3}) (\\S+))"

  val structType = new StructType(
    Array(
      StructField("host", StringType, nullable = true),
      StructField("timestamp", StringType, nullable = true),
      StructField("operation", StringType, nullable = true),
      StructField("status", StringType, nullable = true),
      StructField("bytes", StringType, nullable = true)
    )
  )
  def load(homeHDFS: String, sc: SparkContext, sqlContext: SQLContext): DataFrame = {
    val p = Pattern.compile(regex)
    sqlContext.createDataFrame(sc.textFile(homeHDFS + "/log/")
      .map(fields => p.matcher(fields)
      ).map(matcher =>
        if (matcher.find()) {
          Row(matcher.group(2) // host
            , matcher.group(5).substring(1,12) // timestamp
            , matcher.group(6) // operation
            , matcher.group(7) // status
            , matcher.group(8) // bytes
          )
        } else {
        Row(""
          , ""
          , ""
          , ""
          , ""
        )
    }
    ), structType)
  }
}




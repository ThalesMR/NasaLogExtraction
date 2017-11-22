organization := "br.com.nasa"
name := "http_nasa_thales"
version := "1.0.0"
scalaVersion := "2.10.6"

resolvers += "OSS Sonatype" at "https://repo1.maven.org/maven2/"

libraryDependencies ++= Seq(
    "org.apache.spark" %% "spark-core" % "1.6.1",
    "org.apache.spark" %% "spark-sql" % "1.6.1"
)

name := "Simple Project"

version := "1.0"

scalaVersion := "2.11.7"

resolvers += Resolver.sonatypeRepo("public")

libraryDependencies ++= Seq(
  "org.apache.spark" %% "spark-core" % "1.5.1",
  "org.apache.spark" %% "spark-mllib" % "1.5.1",
  "com.databricks" %% "spark-csv" % "1.2.0"
)

libraryDependencies += "com.github.scopt" % "scopt_2.10" % "3.3.0"


resolvers += Resolver.sonatypeRepo("public")

resolvers += "mvnrepository" at "http://central.maven.org/maven2"



resolvers += Resolver.url("jb-bintray", url("http://dl.bintray.com/jetbrains/sbt-plugins"))(Resolver.ivyStylePatterns)

libraryDependencies += "org.jetbrains" %% "sbt-structure-core" % "4.1.0" // or later version

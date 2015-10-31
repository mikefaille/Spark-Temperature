
name := "temperature"

version := "1.0"

scalaVersion := "2.11.7"

resolvers += Resolver.sonatypeRepo("public")

libraryDependencies ++= Seq(
  "org.apache.spark" %% "spark-core" % "1.5.1"  % "provided" ,
  "org.apache.spark" %% "spark-mllib" % "1.5.1"  % "provided",
  "com.databricks" %% "spark-csv" % "1.2.0"
)

libraryDependencies += "com.github.scopt" % "scopt_2.10" % "3.3.0"


resolvers += Resolver.sonatypeRepo("public")

resolvers += "mvnrepository" at "http://central.maven.org/maven2"




resolvers += Resolver.url("jb-bintray", url("http://dl.bintray.com/jetbrains/sbt-plugins"))(Resolver.ivyStylePatterns)



run in Compile <<= Defaults.runTask(fullClasspath in Compile, mainClass in (Compile, run), runner in (Compile, run))


//
//// This statement includes the assembly plug-in capabilities
//assemblySettings

// Configure JAR used with the assembly plug-in
assemblyJarName := name.value + "-" + version.value + "-assembly.jar"

// A special option to exclude Scala itself form our assembly JAR, since Spark
// already bundles Scala.
assemblyOption in assembly :=
  (assemblyOption in assembly).value.copy(includeScala = false)

assemblyMergeStrategy in assembly := {
  case PathList(ps @ _*) if ps.last endsWith "pom.properties" => MergeStrategy.discard
  case PathList(ps @ _*) if ps.last endsWith "pom.xml" => MergeStrategy.discard
  case x =>
    val oldStrategy = (assemblyMergeStrategy in assembly).value
    oldStrategy(x)
}
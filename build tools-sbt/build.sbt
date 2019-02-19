name := "first"

version := "1.0"

// https://mvnrepository.com/artifact/gov.nist.math/jama
libraryDependencies += "gov.nist.math" % "jama" % "1.0.3"

libraryDependencies ++= Seq(
  "junit" % "junit" % "4.12" % Test,
  "com.novocode" % "junit-interface" % "0.11" % Test exclude("junit", "junit-dep")
)

// https://mvnrepository.com/artifact/org.jfree/jfreechart
libraryDependencies += "org.jfree" % "jfreechart" % "1.0.14"

mainClass in(Compile, run) := Some("MultipleLinearRegression")
mainClass in(Compile, run) := Some("PredictPrice")
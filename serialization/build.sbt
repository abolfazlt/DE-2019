import sbt.ExclusionRule

name := "serialization"

version := "0.1"

scalaVersion := "2.11.8"

assemblyMergeStrategy in assembly := {
  case PathList("META-INF", xs @ _*) => MergeStrategy.discard
  case _ => MergeStrategy.first
}

resourceDirectory := baseDirectory.value / "src/main/resources"

// https://mvnrepository.com/artifact/log4j/log4j
libraryDependencies += "log4j" % "log4j" % "1.2.14"
// https://mvnrepository.com/artifact/com.fasterxml.jackson.core/jackson-databind
libraryDependencies += "com.fasterxml.jackson.core" % "jackson-databind" % "2.9.8"

// https://mvnrepository.com/artifact/com.fasterxml.jackson.module/jackson-module-scala
libraryDependencies += "com.fasterxml.jackson.module" %% "jackson-module-scala" % "2.9.8"


PB.targets in Compile := Seq(
  scalapb.gen() -> (sourceManaged in Compile).value
)

libraryDependencies += ("com.trueaccord.scalapb" %% "scalapb-runtime" % com.trueaccord.scalapb.compiler.Version.scalapbVersion % "protobuf").excludeAll(
  ExclusionRule(organization = "io.netty")
)

libraryDependencies += ("io.grpc" % "grpc-netty" % "1.0.3").excludeAll(
  ExclusionRule(organization = "io.netty")
)

libraryDependencies += ("io.grpc" % "grpc-protobuf" % "1.0.3").excludeAll(
  ExclusionRule(organization = "io.netty")
)
libraryDependencies += ("io.grpc" % "grpc-stub" % "1.0.3").excludeAll(
  ExclusionRule(organization = "io.netty")
)

libraryDependencies += ("com.trueaccord.scalapb" %% "scalapb-runtime-grpc" % com.trueaccord.scalapb.compiler.Version.scalapbVersion).excludeAll(
  ExclusionRule(organization = "io.netty")
)

libraryDependencies += "io.netty" % "netty-all" % "4.1.6.Final"

fork in run := true

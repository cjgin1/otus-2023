scalaVersion := "2.13.11"

scalacOptions ++= Seq(
  "-target:8",
  "-encoding", "utf8",
  "-feature",
  "-language:implicitConversions",
  "-language:higherKinds",
  "-language:existentials",
  "-unchecked",
)

compileOrder  := CompileOrder.JavaThenScala

val dogsBackend = project in file("dogs-backend") settings(
  name := "dogs-backend",
  version := "1.0",
  libraryDependencies ++= Seq(
    "dev.zio" %% "zio" % "2.0.13",
    "dev.zio" %% "zio-json" % "0.5.0",
    "dev.zio" %% "zio-config-typesafe" % "3.0.7",
    "dev.zio" %% "zio-http" % "3.0.0-RC2",
    "dev.zio" %% "zio-logging-slf4j2" % "2.1.12",
    "io.getquill" %% "quill-jdbc-zio" % "4.6.0",
    "org.liquibase" % "liquibase-core" % "4.20.0",
    "org.postgresql" % "postgresql" % "42.5.4"
  )
)

val dogsClient = project in file("dogs-client") settings(
  name := "dogs-client",
  version := "1.0"
)


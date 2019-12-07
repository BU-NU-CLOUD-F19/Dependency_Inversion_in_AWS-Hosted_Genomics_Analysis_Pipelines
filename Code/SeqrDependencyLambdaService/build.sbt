name := "aws-lambda-scala"

scalaVersion := "2.11.8"

scalacOptions in ThisBuild ++= Seq("-unchecked", "-deprecation")

libraryDependencies += "com.amazonaws" % "aws-lambda-java-core" % "1.2.0"

libraryDependencies += "com.lihaoyi" %% "requests" % "0.1.9"

libraryDependencies += "io.lemonlabs" %% "scala-uri" % "1.4.9"

libraryDependencies += "org.scala-lang.modules" %% "scala-parser-combinators" % "1.0.2"

libraryDependencies += "com.amazonaws" % "aws-lambda-java-events" % "2.2.2"

libraryDependencies += "org.elasticsearch.client" % "elasticsearch-rest-client" % "7.4.2"

libraryDependencies += "com.amazonaws" % "aws-java-sdk-secretsmanager" % "1.11.442"

libraryDependencies += "com.typesafe.play" % "play-json_2.11" % "2.5.5"

//unmanagedResourceDirectories in Compile += baseDirectory.value / "resources"
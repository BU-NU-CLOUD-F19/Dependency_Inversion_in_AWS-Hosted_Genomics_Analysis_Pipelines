name := "aws-lambda-scala"

scalaVersion := "2.13.0"

scalacOptions in ThisBuild ++= Seq("-unchecked", "-deprecation")


libraryDependencies += "com.amazonaws" % "aws-lambda-java-core" % "1.2.0"

libraryDependencies += "com.amazonaws" % "aws-lambda-java-events" % "2.2.2"

name := "cloudwork_akka"

version := "0.1"

scalaVersion := "2.12.7"

val scalaTestVersion = "2.2.4"


libraryDependencies += "com.typesafe.akka" %% "akka-actor" % "2.5.18"
libraryDependencies += "com.ning" % "async-http-client" % "1.7.0"
libraryDependencies += "org.jsoup" % "jsoup" % "1.8.3"
libraryDependencies += "ch.qos.logback" % "logback-classic" % "1.1.3"
libraryDependencies += "org.scalatest" % "scalatest_2.12" % "3.0.5" % "test"
libraryDependencies += "mysql" % "mysql-connector-java" % "5.1.24"
libraryDependencies += "commons-validator" % "commons-validator" % "1.6"

resolvers += "Java.net Maven2 Repository" at "http://download.java.net/maven/2/"
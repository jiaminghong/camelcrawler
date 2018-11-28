name := "cloudwork_akka"

version := "0.1"

scalaVersion := "2.12.7"


libraryDependencies += "com.typesafe.akka" %% "akka-actor" % "2.5.18"
libraryDependencies += "com.ning" % "async-http-client" % "1.7.0"
libraryDependencies += "org.jsoup" % "jsoup" % "1.8.3"

resolvers += "Java.net Maven2 Repository" at "http://download.java.net/maven/2/"
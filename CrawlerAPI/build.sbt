name := "CrawlerAPI"
 
version := "1.0"
      
lazy val `crawlerapi` = (project in file(".")).enablePlugins(PlayScala)

resolvers += "scalaz-bintray" at "https://dl.bintray.com/scalaz/releases"
      
resolvers += "Akka Snapshot Repository" at "http://repo.akka.io/snapshots/"
      
scalaVersion := "2.12.7"

libraryDependencies ++= Seq( jdbc , ehcache , ws , specs2 % Test , guice )
libraryDependencies += "com.chiradip.rediscl" % "redisclient_2.10" % "0.8"

resolvers += "Java.net Maven2 Repository" at "http://download.java.net/maven/2/"

unmanagedResourceDirectories in Test <+=  baseDirectory ( _ /"target/web/public/test" )

libraryDependencies += "com.github.etaty" %% "rediscala" % "1.8.0"

// https://mvnrepository.com/artifact/org.apache.spark/spark-mllib
libraryDependencies += "org.apache.spark" %% "spark-mllib" % "2.4.0"

libraryDependencies += "org.apache.spark" %% "spark-core" % "2.4.0"

dependencyOverrides += "com.fasterxml.jackson.core" % "jackson-core" % "2.7.9"
dependencyOverrides += "com.fasterxml.jackson.core" % "jackson-databind" % "2.6.7"
dependencyOverrides += "com.fasterxml.jackson.module" % "jackson-module-scala_2.12" % "2.6.7.1"
libraryDependencies += "org.apache.hadoop" % "hadoop-client" % "2.7.2"
libraryDependencies += "org.apache.hadoop" % "hadoop-mapreduce-client-core" % "2.7.2"
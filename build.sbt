name := """Project"""

version := "1.0-SNAPSHOT"

lazy val root = (project in file(".")).enablePlugins(PlayScala)

scalaVersion := "2.11.6"

libraryDependencies ++= Seq(
  //jdbc,
  cache,
  "joda-time" % "joda-time" % "2.3",
  "org.xerial" % "sqlite-jdbc" % "3.8.6"
   //, "com.typesafe.play" %% "play-slick" % "2.0.0"
  ,"com.typesafe.play" %% "play-slick" % "1.1.0"
 //, "com.typesafe.play" %% "play-slick-evolutions" % "2.0.0",
  ,"com.typesafe.play" %% "play-slick-evolutions" % "1.1.1",
  "org.scalatestplus" %% "play" % "1.2.0" % "test"

)


//libraryDependencies += evolutions


resolvers += "Typesafe repository" at "http://repo.typesafe.com/typesafe/releases/"

resolvers += "scalaz-bintray" at "http://dl.bintray.com/scalaz/releases"

resolvers += "Sonatype OSS Snapshots" at "https://oss.sonatype.org/content/repositories/snapshots"


// Play provides two styles of routers, one expects its actions to be injected, the
// other, legacy style, accesses its actions statically.
routesGenerator := InjectedRoutesGenerator



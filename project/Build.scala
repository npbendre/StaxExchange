import sbt._
import Keys._
import play.Project._

object ApplicationBuild extends Build {

  val appName         = "StaxExchange"
  val appVersion      = "1.0-SNAPSHOT"

  val appDependencies = Seq(
    // Add your project dependencies here,
    javaCore,
    javaJdbc,
    javaEbean,
    "mysql" % "mysql-connector-java" % "5.1.3",
    "org.hibernate" % "hibernate-entitymanager" % "3.6.9.Final"
    
  )

  val main = play.Project(appName, appVersion, appDependencies).settings(
    // Add your own project settings here      
  )

}

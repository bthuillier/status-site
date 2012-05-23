import sbt._
import Keys._
import PlayProject._

object ApplicationBuild extends Build {

    val appName         = "site-status"
    val appVersion      = "1.0-SNAPSHOT"

    val appDependencies = Seq(
  	"com.mongodb.casbah" %% "casbah" % "2.1.5-1",
  	"com.novus" %% "salat-core" % "0.0.8-SNAPSHOT"    
	)

    val main = PlayProject(appName, appVersion, appDependencies, mainLang = SCALA).settings(
   resolvers ++= Seq(
     "repo.novus snaps" at "http://repo.novus.com/snapshots/")    
)

}

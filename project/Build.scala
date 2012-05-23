import sbt._
import Keys._
import PlayProject._

object ApplicationBuild extends Build {

    val appName         = "site-status"
    val appVersion      = "1.0-SNAPSHOT"

    val appDependencies = Seq(
        "se.radley" %% "play-plugins-salat" % "1.0.4-SNAPSHOT" 
	)

    val main = PlayProject(appName, appVersion, appDependencies, mainLang = SCALA).settings(
       routesImport += "se.radley.plugin.salat.Binders._",
       templatesImport += "org.bson.types.ObjectId"
)

}

import sbt._
import play.Project._

object ApplicationBuild extends Build {

  val appName         = "math-render"
  val appVersion      = "1.0-SNAPSHOT"

  val jLatexMath = "org.scilab.forge" % "jlatexmath" % "0.9.6"

  val appDependencies = Seq(
    // Add your project dependencies here,
    jdbc,
    anorm,
    jLatexMath
  )


  val main = play.Project(appName, appVersion, appDependencies).settings(
    // Add your own project settings here
  )

}

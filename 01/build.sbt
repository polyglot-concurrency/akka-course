val AkkaVersion = "2.8.2"
val LogbackVersion = "1.2.3"
val ScalaVersion = "3.3.0"
val JacksonVersion = "2.11.4"

lazy val root = project
  .in(file("."))
  .settings(
    scalaVersion := ScalaVersion,
    // scalafmtOnCompile := true,
    libraryDependencies ++= Seq(
      "com.typesafe.akka" %% "akka-actor-typed"         % AkkaVersion,
      "ch.qos.logback"     % "logback-classic"          % LogbackVersion,
      "com.typesafe.akka" %% "akka-actor-testkit-typed" % AkkaVersion % Test,
      "com.lihaoyi"       %% "requests"                 % "0.8.0",
      "org.json4s"        %% "json4s-native"            % "4.0.6",
    )
  )

ThisBuild / scalacOptions ++=
  Seq(
    "-explain",
    "-deprecation",
    // "-Yexplicit-nulls",
    // "-Ysafe-init",
    // "-Wunused:all",
  ) // ++ Seq("-new-syntax", "-rewrite") //++ Seq("-rewrite", "-indent")// ++ Seq("-source", "future-migration")

ThisBuild / watchTriggeredMessage := Watch.clearScreenOnTrigger

addCommandAlias("c", "compile")
addCommandAlias("ca", "Test / compile")
addCommandAlias("t", "test")
addCommandAlias("r", "run")
addCommandAlias("styleCheck", "scalafmtSbtCheck; scalafmtCheckAll")
addCommandAlias("styleFix", "scalafmtSbt; scalafmtAll")
addCommandAlias("ud", "reload plugins; update; reload return")

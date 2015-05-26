name := "ChocoIDE"

version := "1.0"

lazy val `chocoide` = (project in file(".")).enablePlugins(PlayScala)

scalaVersion := "2.11.1"

libraryDependencies ++= Seq(jdbc, anorm, cache, ws)

<<<<<<< HEAD
libraryDependencies += "com.fasterxml.jackson.core" % "jackson-core" % "2.5.3"
=======
libraryDependencies += "org.jetbrains" % "annotations" % "13.0"

libraryDependencies += "com.fasterxml.jackson.core"%"jackson-core"%"2.5.3"
>>>>>>> 28eea71158d4df051390d5768b814bfeefb74c91

libraryDependencies += "org.choco-solver" % "choco-solver" % "3.3.1"

<<<<<<< HEAD
libraryDependencies += "com.typesafe.play" %% "play-mailer" % "2.4.1"

unmanagedResourceDirectories in Test <+= baseDirectory(_ / "target/web/public/test")
=======
// adding the tools.jar to the unmanaged-jars seq
unmanagedJars in Compile ~= {uj =>
  Seq(Attributed.blank(file(System.getProperty("java.home").dropRight(3)+"lib/tools.jar"))) ++ uj
}

unmanagedResourceDirectories in Test <+=  baseDirectory ( _ /"target/web/public/test" )
>>>>>>> 28eea71158d4df051390d5768b814bfeefb74c91

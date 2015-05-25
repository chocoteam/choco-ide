name := "ChocoIDE"

version := "1.0"

lazy val `chocoide` = (project in file(".")).enablePlugins(PlayScala)

scalaVersion := "2.11.1"

libraryDependencies ++= Seq( jdbc , anorm , cache , ws )

libraryDependencies += "org.jetbrains" % "annotations" % "13.0"

libraryDependencies += "com.fasterxml.jackson.core"%"jackson-core"%"2.5.3"

libraryDependencies += "org.choco-solver"%"choco-solver"%"3.3.1"

// adding the tools.jar to the unmanaged-jars seq
unmanagedJars in Compile ~= {uj =>
  Seq(Attributed.blank(file(System.getProperty("java.home").dropRight(3)+"lib/tools.jar"))) ++ uj
}

unmanagedResourceDirectories in Test <+=  baseDirectory ( _ /"target/web/public/test" )

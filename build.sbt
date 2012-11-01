organization := "met.lessis"

name := "dispatch-meetup"

version := "0.1.0-SNAPSHOT"

// until https://github.com/dispatch/reboot/issues/31 gets in a release use 0.9.2
libraryDependencies += "net.databinder.dispatch" %% "dispatch-lift-json" % "0.9.2"

libraryDependencies += "org.slf4j" % "slf4j-jdk14" % "1.6.2"

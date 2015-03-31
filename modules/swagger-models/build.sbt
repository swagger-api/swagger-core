organization := "com.wordnik"

name := "swagger-core"

version := "1.5.1-SNAPSHOT"

libraryDependencies ++= Seq(
  "com.fasterxml.jackson.module"     % "jackson-module-jsonSchema"     % "2.4.1",
  "junit"                            % "junit"                         % "4.11"   % "test",
  "org.scalatest"                    %% "scalatest"                    % "2.1.7"  % "test"
)

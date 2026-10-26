#!/bin/bash

CUR=$(pwd)

export SC_VERSION=`./mvnw -q -Dexec.executable="echo" -Dexec.args='${parsedVersion.majorVersion}.${parsedVersion.minorVersion}.${parsedVersion.incrementalVersion}' --non-recursive build-helper:parse-version org.codehaus.mojo:exec-maven-plugin:1.3.1:exec`
export SC_NEXT_VERSION=`./mvnw -q -Dexec.executable="echo" -Dexec.args='${parsedVersion.majorVersion}.${parsedVersion.minorVersion}.${parsedVersion.nextIncrementalVersion}' --non-recursive build-helper:parse-version org.codehaus.mojo:exec-maven-plugin:1.3.1:exec`
SC_LAST_RELEASE=`python $CUR/CI/lastRelease.py`
export SC_LAST_RELEASE

SC_RELEASE_TITLE="Swagger-core $SC_VERSION released!"
SC_RELEASE_TAG="v$SC_VERSION"

#####################
### draft release Notes with next release after last release, with tag
#####################
python $CUR/CI/releaseNotes.py "$SC_LAST_RELEASE" "$SC_RELEASE_TITLE" "$SC_RELEASE_TAG"

#####################
### update the version to release in maven project with set version
#####################
./mvnw versions:set -DnewVersion=$SC_VERSION
./mvnw versions:commit

#####################
### update the "build from source" version in readme to the next snapshot ###
#####################
sc_find="currently $SC_VERSION-SNAPSHOT"
sc_replace="currently $SC_NEXT_VERSION-SNAPSHOT"
sed -i -e "s/$sc_find/$sc_replace/g" $CUR/README.md

#####################
### build and test maven ###
#####################
./mvnw --no-transfer-progress -B install --file pom.xml

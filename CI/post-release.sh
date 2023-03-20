#!/bin/bash

CUR=$(pwd)
TMPDIR="$(dirname -- "${0}")"

SC_RELEASE_TAG="v$SC_VERSION"

#####################
### deploy gradle plugin release
#####################
cd modules/swagger-gradle-plugin
./gradlew publishPlugins -Pgradle.publish.key="${GRADLE_PUBLISH_KEY}" -Pgradle.publish.secret="${GRADLE_PUBLISH_SECRET}" --info
cd ../..

#####################
### publish pre-prepared release (tag is created)
#####################
python $CUR/CI/publishRelease.py "$SC_RELEASE_TAG"

#####################
### update the version to next snapshot in maven project with set version
#####################
./mvnw versions:set -DnewVersion="${SC_NEXT_VERSION}-SNAPSHOT"
./mvnw versions:commit

cd modules/swagger-project-jakarta
../../mvnw versions:set -DnewVersion="${SC_NEXT_VERSION}-SNAPSHOT"
../../mvnw versions:commit
cd ../..

#####################
### update all other versions in files around to the next snapshot or new release, including readme and gradle ###
#####################

sc_find="version=$SC_VERSION"
sc_replace="version=$SC_NEXT_VERSION-SNAPSHOT"
sed -i -e "s/$sc_find/$sc_replace/g" $CUR/modules/swagger-gradle-plugin/gradle.properties

sc_find="io.swagger.core.v3:swagger-jaxrs2:$SC_VERSION"
sc_replace="io.swagger.core.v3:swagger-jaxrs2:$SC_NEXT_VERSION-SNAPSHOT"
sed -i -e "s/$sc_find/$sc_replace/g" $CUR/modules/swagger-gradle-plugin/src/main/java/io/swagger/v3/plugins/gradle/SwaggerPlugin.java

sc_find="io.swagger.core.v3:swagger-jaxrs2:$SC_VERSION"
sc_replace="io.swagger.core.v3:swagger-jaxrs2:$SC_NEXT_VERSION-SNAPSHOT"
sed -i -e "s/$sc_find/$sc_replace/g" $CUR/modules/swagger-gradle-plugin/src/test/java/io/swagger/v3/plugins/gradle/SwaggerResolveTest.java


#####################
### Copy scripts to temp folder, as they are not available when checking out different branch or repo
#####################
cp -a $CUR/CI/update-v1-readme.sh $TMPDIR/update-v1-readme.sh
cp -a $CUR/CI/update-wiki.sh $TMPDIR/update-wiki.sh

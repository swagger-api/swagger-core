#!/bin/bash
set -e

CUR=$(pwd)
RELEASE_VERSION="${RELEASE_VERSION:?}"

# Configure git
git config user.email "action@github.com"
git config user.name "GitHub Action"

# Update Maven versions
./mvnw versions:set -DnewVersion="${RELEASE_VERSION}"
./mvnw versions:commit

cd modules/swagger-bom
../../mvnw versions:set -DnewVersion="${RELEASE_VERSION}"
../../mvnw versions:commit
cd ../..

cd modules/swagger-project-jakarta
../../mvnw versions:set -DnewVersion="${RELEASE_VERSION}"
../../mvnw versions:commit
cd ../..

# Update gradle.properties
sed -i "s/version=.*/version=${RELEASE_VERSION}/" modules/swagger-gradle-plugin/gradle.properties

# Update Java files with version reference (preserve rest of the line)
# For double-quoted strings (SwaggerPlugin.java)
sed -i "s/\(io.github.vpelikh:swagger-jaxrs2:\)[^\"]*\(.*\)/\1${RELEASE_VERSION}\2/" \
  modules/swagger-gradle-plugin/src/main/java/io/swagger/v3/plugins/gradle/SwaggerPlugin.java

# For single-quoted strings (SwaggerResolveTest.java)
sed -i "s/\('io.github.vpelikh:swagger-jaxrs2:\)[^']*\(.*\)/\1${RELEASE_VERSION}\2/" \
  modules/swagger-gradle-plugin/src/test/java/io/swagger/v3/plugins/gradle/SwaggerResolveTest.java

# Update swagger-maven-plugin README
sed -i "s/<version>.*<\/version>/<version>${RELEASE_VERSION}<\/version>/" modules/swagger-maven-plugin/README.md

# Stage all changes and commit (detached)
git add -A
git commit -m "Release version ${RELEASE_VERSION}"

echo "RELEASE_COMMIT=$(git rev-parse HEAD)" >> $GITHUB_ENV
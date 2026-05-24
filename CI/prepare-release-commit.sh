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

# Update Java files with version reference
sed -i "s/io.github.vpelikh:swagger-jaxrs2:.*/io.github.vpelikh:swagger-jaxrs2:${RELEASE_VERSION}/" \
  modules/swagger-gradle-plugin/src/main/java/io/swagger/v3/plugins/gradle/SwaggerPlugin.java

sed -i "s/io.github.vpelikh:swagger-jaxrs2:.*/io.github.vpelikh:swagger-jaxrs2:${RELEASE_VERSION}/" \
  modules/swagger-gradle-plugin/src/test/java/io/swagger/v3/plugins/gradle/SwaggerResolveTest.java

# Update swagger-maven-plugin README
sed -i "s/<version>.*<\/version>/<version>${RELEASE_VERSION}<\/version>/" modules/swagger-maven-plugin/README.md

# Generate release notes (draft)
SC_LAST_RELEASE=$(python CI/lastRelease.py)
RELEASE_TITLE="Swagger-core ${RELEASE_VERSION} released!"
if [ "${IS_PRERELEASE}" = "true" ]; then
  if [[ "${RELEASE_VERSION}" =~ -M ]]; then
    RELEASE_TITLE="Swagger-core ${RELEASE_VERSION} (Milestone) released!"
  elif [[ "${RELEASE_VERSION}" =~ -RC ]]; then
    RELEASE_TITLE="Swagger-core ${RELEASE_VERSION} (Release Candidate) released!"
  fi
fi
python CI/releaseNotes.py "$SC_LAST_RELEASE" "$RELEASE_TITLE" "v${RELEASE_VERSION}" "${IS_PRERELEASE}"

# Stage all changes and commit (detached)
git add -A
git commit -m "Release version ${RELEASE_VERSION}"

echo "RELEASE_COMMIT=$(git rev-parse HEAD)" >> $GITHUB_ENV
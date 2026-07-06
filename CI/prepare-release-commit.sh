#!/bin/bash
set -e

RELEASE_VERSION="${RELEASE_VERSION:?}"

# Configure git
git config user.email "action@github.com"
git config user.name "GitHub Action"

# Update all Maven POMs in one go
./mvnw versions:set -DnewVersion="${RELEASE_VERSION}" -DgenerateBackupPoms=false

# Update swagger-bom version (it has its own <version>, not inherited from parent)
./mvnw versions:set -DnewVersion="${RELEASE_VERSION}" -DgenerateBackupPoms=false --file modules/swagger-bom/pom.xml

# Update gradle.properties
sed -i "s/version=.*/version=${RELEASE_VERSION}/" modules/swagger-gradle-plugin/gradle.properties

# Update swagger-maven-plugin README
sed -i "s/<version>.*<\/version>/<version>${RELEASE_VERSION}<\/version>/" modules/swagger-maven-plugin/README.md

# Stage all changes and commit (detached)
git add -A
git commit -m "Release version ${RELEASE_VERSION}"

echo "RELEASE_COMMIT=$(git rev-parse HEAD)" >> $GITHUB_ENV
#!/bin/bash
set -e

# This script bumps the version on master to the next SNAPSHOT after a final release.
# It assumes we are on a detached HEAD after the release tag.

RELEASE_VERSION="${RELEASE_VERSION:?}"
CURRENT_BRANCH=$(git branch --show-current)
if [ "$CURRENT_BRANCH" != "master" ]; then
  git checkout master
  git pull origin master
fi

# Compute next snapshot version: increment patch, e.g., 3.0.0 -> 3.0.1-SNAPSHOT
if [[ $RELEASE_VERSION =~ ^([0-9]+)\.([0-9]+)\.([0-9]+)$ ]]; then
  MAJOR=${BASH_REMATCH[1]}
  MINOR=${BASH_REMATCH[2]}
  PATCH=${BASH_REMATCH[3]}
  NEXT_PATCH=$((PATCH + 1))
  NEXT_SNAPSHOT="${MAJOR}.${MINOR}.${NEXT_PATCH}-SNAPSHOT"
else
  echo "ERROR: Release version '$RELEASE_VERSION' does not match semantic versioning (X.Y.Z)"
  exit 1
fi

echo "Bumping version on master to $NEXT_SNAPSHOT"

# Update Maven versions
./mvnw versions:set -DnewVersion="${NEXT_SNAPSHOT}"
./mvnw versions:commit

cd modules/swagger-bom
../../mvnw versions:set -DnewVersion="${NEXT_SNAPSHOT}"
../../mvnw versions:commit
cd ../..

cd modules/swagger-project-jakarta
../../mvnw versions:set -DnewVersion="${NEXT_SNAPSHOT}"
../../mvnw versions:commit
cd ../..

# Update gradle.properties
sed -i "s/version=.*/version=${NEXT_SNAPSHOT}/" modules/swagger-gradle-plugin/gradle.properties

# Commit and push
git config user.email "action@github.com"
git config user.name "GitHub Action"
git add -A
git commit -m "Bump version to ${NEXT_SNAPSHOT}"
git push origin master
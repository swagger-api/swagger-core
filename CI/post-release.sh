#!/bin/bash

CUR=$(pwd)

SC_RELEASE_TAG="v$SC_VERSION"

#####################
### publish pre-prepared release (tag is created)
#####################
python $CUR/CI/publishRelease.py "$SC_RELEASE_TAG"

#####################
### update the version to next snapshot in maven project with set version
#####################
./mvnw versions:set -DnewVersion="${SC_NEXT_VERSION}-SNAPSHOT"
./mvnw versions:commit

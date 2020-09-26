#!/bin/bash

CUR=$(pwd)
TMPDIR="$(dirname -- "${0}")"

SC_RELEASE_TAG="v$SC_VERSION"

#####################
### publish javadocs
#####################

mkdir -p $CUR/swagger-core/${SC_RELEASE_TAG}
cp -aR $TMPDIR/apidocs $CUR/swagger-core/${SC_RELEASE_TAG}
git add -A
git commit -m "apidocs for release ${SC_RELEASE_TAG}"
git push -u origin gh-pages

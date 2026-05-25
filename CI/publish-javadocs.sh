#!/bin/bash

CUR=$(pwd)
TMPDIR="$(dirname -- "${0}")"

SC_RELEASE_TAG="v${RELEASE_VERSION}"

#####################
### publish javadocs
#####################

# Publish versioned javadocs
mkdir -p $CUR/swagger-core/${SC_RELEASE_TAG}
cp -aR $TMPDIR/apidocs $CUR/swagger-core/${SC_RELEASE_TAG}

# Publish to "latest" (always points to the most recent release)
rm -rf $CUR/swagger-core/latest
mkdir -p $CUR/swagger-core/latest
cp -aR $TMPDIR/apidocs $CUR/swagger-core/latest

git add -A
git commit -m "apidocs for release ${SC_RELEASE_TAG}"
git push -u origin gh-pages
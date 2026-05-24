#!/bin/bash

CUR=$(pwd)
TMPDIR="$(dirname -- "${0}")"

SC_RELEASE_TAG="v${RELEASE_VERSION}"

#####################
### publish javadocs
#####################

cp -aR $CUR/modules/swagger-annotations/target/javadocprep/swagger-core/${RELEASE_VERSION}/apidocs $TMPDIR
cp -a $CUR/CI/publish-javadocs.sh $TMPDIR/publish-javadocs.sh
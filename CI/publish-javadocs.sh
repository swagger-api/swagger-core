#!/bin/bash

CUR=$(pwd)
TMPDIR="$(dirname -- "${0}")"

SC_RELEASE_TAG="v$SC_VERSION"

#####################
### publish javadocs
#####################

mkdir -p $CUR/swagger-core/${SC_RELEASE_TAG}
cp -aR $TMPDIR/apidocs $CUR/swagger-core/${SC_RELEASE_TAG}

#####################
### remove previous releases of the same major.minor line (e.g. 2.2.56 removes v2.2.55)
#####################
SC_LINE_PREFIX="v${SC_VERSION%.*}."
for d in "$CUR"/swagger-core/${SC_LINE_PREFIX}*/; do
  d="${d%/}"
  name="$(basename "$d")"
  if [ "$name" != "$SC_RELEASE_TAG" ] && [[ "$name" =~ ^v[0-9]+\.[0-9]+\.[0-9]+ ]]; then
    echo "removing previous javadocs ${name}"
    git rm -rq "$d"
  fi
done

git add -A
git commit -m "apidocs for release ${SC_RELEASE_TAG}"
git push -u origin gh-pages

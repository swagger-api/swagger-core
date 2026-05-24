#!/bin/bash

CUR=$(pwd)

#####################
### update Wiki
#####################
cd wiki
sc_find="${LAST_STABLE_RELEASE}\/"
sc_replace="${RELEASE_VERSION}\/"
sed -i -e "s/$sc_find/$sc_replace/g" $CUR/wiki/Swagger-2.X---Annotations.md
git add -A
git commit -m "update javadocs links to ${RELEASE_VERSION}"
git push -u origin master
cd ..
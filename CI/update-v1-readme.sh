#!/bin/bash
SC_LAST_RELEASE="$1"
SC_VERSION="$2"

CUR=$(pwd)

#####################
### update v2 versions in readme
#####################
sc_find="$SC_LAST_RELEASE (\*\*current stable\*\*)"
sc_replace="$SC_LAST_RELEASE                     "
sed -i -e "s/$sc_find/$sc_replace/g" $CUR/README.md

# update readme with a line for the new release replacing the previous
CURDATE=$(date +"%Y-%m-%d")
sc_find="------------------------- | ------------ | -------------------------- | ----- | ----"
sc_add="$SC_VERSION (**current stable**)| $CURDATE   | 3.x           | [tag v$SC_VERSION](https:\/\/github.com\/swagger-api\/swagger-core\/tree\/v$SC_VERSION) | Supported"
sc_replace="$sc_find\n$sc_add"
sed -i -e "s/$sc_find/$sc_replace/g" $CUR/README.md

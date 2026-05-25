#!/bin/bash

CUR=$(pwd)
TMPDIR="$(dirname -- "${0}")"

# Copy javadocs from swagger-annotations module
cp -aR "$CUR/modules/swagger-annotations/target/apidocs" "$TMPDIR/apidocs"

# Copy publish script for later checkout
cp -a "$CUR/CI/publish-javadocs.sh" "$TMPDIR/publish-javadocs.sh"
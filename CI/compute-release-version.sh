#!/bin/bash
set -e

RELEASE_TYPE="${1:-release}"

# Verify we are on a SNAPSHOT
CURRENT_VERSION=$(./mvnw -q -DforceStdout help:evaluate -Dexpression=project.version --non-recursive)

if [[ ! "$CURRENT_VERSION" =~ ^.*-SNAPSHOT$ ]]; then
  echo "ERROR: Current version is not a SNAPSHOT ($CURRENT_VERSION). Release can only start from a SNAPSHOT."
  exit 1
fi

BASE_VERSION="${CURRENT_VERSION%-SNAPSHOT}"

# Determine target release version
case "$RELEASE_TYPE" in
  milestone)
    # Look for existing vX.Y.Z-M* tags
    LAST_M=$(git tag -l "v${BASE_VERSION}-M*" | sed -E 's/.*-M([0-9]+)$/\1/' | sort -n | tail -1)
    NEXT_M=$(( ${LAST_M:-0} + 1 ))
    RELEASE_VERSION="${BASE_VERSION}-M${NEXT_M}"
    IS_PRERELEASE="true"
    ;;
  rc)
    LAST_RC=$(git tag -l "v${BASE_VERSION}-RC*" | sed -E 's/.*-RC([0-9]+)$/\1/' | sort -n | tail -1)
    NEXT_RC=$(( ${LAST_RC:-0} + 1 ))
    RELEASE_VERSION="${BASE_VERSION}-RC${NEXT_RC}"
    IS_PRERELEASE="true"
    ;;
  release)
    # Final release: base version without suffix
    RELEASE_VERSION="${BASE_VERSION}"
    IS_PRERELEASE="false"
    ;;
  *)
    echo "ERROR: Unknown release type '$RELEASE_TYPE'"
    exit 1
    ;;
esac

# Prevent re-releasing an already existing tag
if git rev-parse "v${RELEASE_VERSION}" >/dev/null 2>&1; then
  echo "ERROR: Tag v${RELEASE_VERSION} already exists. This version has already been released."
  exit 1
fi

# Export variables
echo "RELEASE_VERSION=${RELEASE_VERSION}" >> $GITHUB_ENV
echo "IS_PRERELEASE=${IS_PRERELEASE}" >> $GITHUB_ENV

echo "Current SNAPSHOT: $CURRENT_VERSION"
echo "Release version:  $RELEASE_VERSION"
echo "Pre-release:      $IS_PRERELEASE"
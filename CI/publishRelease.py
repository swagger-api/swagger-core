#!/usr/bin/python

import sys
import ghApiClient

def lastDraftReleaseId(tag):
    content = ghApiClient.readUrl('repos/vpelikh/swagger-core/releases')
    for release in content:
        if release["draft"] and release["tag_name"] == tag:
            return release["id"]
    return None

def publishRelease(tag):
    release_id = lastDraftReleaseId(tag)
    if release_id is None:
        print("No draft release found for tag " + tag)
        sys.exit(1)
    payload = {
        "tag_name": tag,
        "draft": False,
        "target_commitish": "master"
    }
    ghApiClient.postUrl('repos/vpelikh/swagger-core/releases/' + str(release_id), json.dumps(payload))

def main(tag):
    publishRelease(tag)

if __name__ == "__main__":
    main(sys.argv[1])
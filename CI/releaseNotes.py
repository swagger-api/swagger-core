#!/usr/bin/python

import sys
import json
from datetime import datetime
import ghApiClient

def allPulls(releaseDate):
    result = ""
    baseurl = "https://api.github.com/repos/vpelikh/swagger-core/pulls/"
    content = ghApiClient.readUrl('repos/vpelikh/swagger-core/pulls?state=closed&base=master&per_page=100')
    for l in content:
        stripped = l["url"][len(baseurl):]
        mergedAt = l["merged_at"]
        if mergedAt is not None:
            if datetime.strptime(mergedAt, '%Y-%m-%dT%H:%M:%SZ') > releaseDate:
                if not l['title'].startswith("bump snap"):
                    result += '\n'
                    result += "* " + l['title'] + " (#" + stripped + ")"
    return result

def lastReleaseDate(tag):
    content = ghApiClient.readUrl('repos/vpelikh/swagger-core/releases/tags/' + tag)
    publishedAt = content["published_at"]
    return datetime.strptime(publishedAt, '%Y-%m-%dT%H:%M:%SZ')

def addRelease(release_title, tag, content, prerelease):
    payload = {
        "tag_name": tag,
        "name": release_title,
        "body": content,
        "draft": True,
        "prerelease": prerelease,
        "target_commitish": "master"
    }
    ghApiClient.postUrl('repos/vpelikh/swagger-core/releases', json.dumps(payload))

def main(last_release, release_title, tag, prerelease_str):
    prerelease = prerelease_str.lower() == 'true'
    result = allPulls(lastReleaseDate('v' + last_release))
    addRelease(release_title, tag, result, prerelease)

if __name__ == "__main__":
    main(sys.argv[1], sys.argv[2], sys.argv[3], sys.argv[4])
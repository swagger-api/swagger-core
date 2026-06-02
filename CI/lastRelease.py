#!/usr/bin/python

import sys
import ghApiClient

def getLastReleaseTag(include_prereleases=True):
    content = ghApiClient.readUrl('repos/vpelikh/swagger-core/releases')
    for release in content:
        if release["draft"]:
            continue
        if not include_prereleases and release["prerelease"]:
            continue
        tag = release["tag_name"]
        return tag[1:]  # remove leading 'v'
    return "0.0.0"

def main():
    include_all = True
    if len(sys.argv) > 1 and sys.argv[1] == 'stable':
        include_all = False
    result = getLastReleaseTag(include_all)
    print(result)

if __name__ == "__main__":
    main()
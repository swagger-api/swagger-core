#!/usr/bin/python

import ghApiClient

def getLastStableReleaseTag():
    content = ghApiClient.readUrl('repos/vpelikh/swagger-core/releases')
    for release in content:
        if not release["draft"] and not release["prerelease"]:
            tag = release["tag_name"]
            return tag[1:]  # remove 'v'
    return "0.0.0"

def main():
    result = getLastStableReleaseTag()
    print(result)

if __name__ == "__main__":
    main()
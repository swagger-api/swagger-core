#!/usr/bin/python

import sys
import ghApiClient

def getLastReleaseTag(prefix):
    content = ghApiClient.readUrl('repos/swagger-api/swagger-core/releases')
    for l in content:
        draft = l["draft"]
        tag = l["tag_name"]
        if str(draft) != 'True' and tag.startswith(prefix):
            return tag[1:]

# main
def main():
    result = getLastReleaseTag("v3")
    if result is None:
        # first 3.x release: no v3 tag published yet, use the last 2.x release as baseline
        result = getLastReleaseTag("v2")
        print("WARNING: no published v3 release found, falling back to last v2 release " + str(result), file=sys.stderr)
    print (result)

# here start main
main()

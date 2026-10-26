#!/usr/bin/python

import ghApiClient

PER_PAGE = 100

def getLastReleaseTag(prefix):
    # releases are returned newest first; v1 releases are far behind the v2/v3 ones, so page through them
    page = 1
    while True:
        content = ghApiClient.readUrl('repos/swagger-api/swagger-core/releases?per_page=%d&page=%d' % (PER_PAGE, page))
        for l in content:
            draft = l["draft"]
            tag = l["tag_name"]
            if str(draft) != 'True' and tag.startswith(prefix):
                return tag[1:]
        if len(content) < PER_PAGE:
            return None
        page += 1

# main
def main():
    result = getLastReleaseTag("v1.")
    if result is None:
        raise SystemExit("ERROR: no published v1 release found")
    print (result)

# here start main
main()

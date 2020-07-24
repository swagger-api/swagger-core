#!/usr/bin/python

import os
import sys
import time
import urllib2
import httplib
import base64
import json
from datetime import datetime

pid = -1
timestamp = None
datastring = ""

GH_BASE_URL = "https://api.github.com/"

GH_USER = os.environ['GH_USER']
GH_TOKEN = os.environ['GH_TOKEN']


def readUrl(name):
    global GH_BASE_URL

    try:
        base64string = base64.b64encode('%s:%s' % (GH_USER, GH_TOKEN))
        request = urllib2.Request(GH_BASE_URL + name)
        request.add_header("Authorization", "Basic %s" % base64string)
        content = urllib2.urlopen(request).read()
        jcont = json.loads(content)
        # clear file with exception count
        return jcont;
    except urllib2.HTTPError, e:
        print 'HTTPError = ' + str(e.code)
        raise e
    except urllib2.URLError, e:
        print 'URLError = ' + str(e.reason)
        raise e
    except httplib.HTTPException, e:
        print 'HTTPException = ' + str(e)
        raise e
    except Exception:
        import traceback
        print 'generic exception: ' + traceback.format_exc()
        raise IOError

def postUrl(name, body):
    global GH_BASE_URL
    try:
        time.sleep(0.05)
        base64string = base64.b64encode('%s:%s' % (GH_USER, GH_TOKEN))
        request = urllib2.Request(GH_BASE_URL + name)
        request.add_header("Authorization", "Basic %s" % base64string)
        request.add_header("Accept", "application/vnd.github.v3+json")
        content = urllib2.urlopen(request, body).read()
        jcont = json.loads(content)
        # clear file with exception count
        return jcont;
    except urllib2.HTTPError, e:
        print 'HTTPError = ' + str(e.code)
        print str(e)
        raise e
    except urllib2.URLError, e:
        print 'URLError = ' + str(e.reason)
        raise e
    except httplib.HTTPException, e:
        print 'HTTPException = ' + str(e)
        raise e
    except Exception:
        import traceback
        print 'generic exception: ' + traceback.format_exc()
        raise IOError

def invoke(name, body, method):
    global GH_BASE_URL

    try:
        base64string = base64.b64encode('%s:%s' % (GH_USER, GH_TOKEN))
        request = urllib2.Request(GH_BASE_URL + name)
        request.add_header("Authorization", "Basic %s" % base64string)
        content = urllib2.urlopen(request).read()
        # clear file with exception count
        return content;
    except urllib2.HTTPError, e:
        print 'HTTPError = ' + str(e.code)
        raise e
    except urllib2.URLError, e:
        print 'URLError = ' + str(e.reason)
        raise e
    except httplib.HTTPException, e:
        print 'HTTPException = ' + str(e)
        raise e
    except Exception:
        import traceback
        print 'generic exception: ' + traceback.format_exc()
        raise IOError


# pretty
def pretty(jcont):
    return json.dumps(jcont, sort_keys=True, indent=4, separators=(',', ': '))


def allPulls(releaseDate):

    result = ""

    baseurl = "https://api.github.com/repos/swagger-api/swagger-core/pulls/"
    content = readUrl('repos/swagger-api/swagger-core/pulls?state=closed&base=master&per_page=100')
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
    content = readUrl('repos/swagger-api/swagger-core/releases/tags/' + tag)
    publishedAt = content["published_at"]
    return datetime.strptime(publishedAt, '%Y-%m-%dT%H:%M:%SZ')


def addRelease(release_title, tag, content):
    payload = "{\"tag_name\":\"" + tag + "\", "
    payload += "\"name\":" + json.dumps(release_title) + ", "
    payload += "\"body\":" + json.dumps(content) + ", "
    payload += "\"draft\":" + "true" + ", "
    payload += "\"prerelease\":" + "false" + ", "
    payload += "\"target_commitish\":\"" + "master" + "\"}"
    content = postUrl('repos/swagger-api/swagger-core/releases', payload)
    return content

def getReleases():
    content = readUrl('repos/swagger-api/swagger-core/releases')
    return content


# main
def main(last_release, release_title, tag):

    result = allPulls(lastReleaseDate('v' + last_release))
    addRelease (release_title, tag, result)

# here start main
main(sys.argv[1], sys.argv[2], sys.argv[3])

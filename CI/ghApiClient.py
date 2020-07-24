#!/usr/bin/python

import os
import time
import urllib2
import httplib
import json

GH_BASE_URL = "https://api.github.com/"

GH_TOKEN = os.environ['GH_TOKEN']
GH_AUTH = "Bearer %s" % GH_TOKEN

def readUrl(name):
    try:
        request = urllib2.Request(GH_BASE_URL + name)
        request.add_header("Authorization", GH_AUTH)
        content = urllib2.urlopen(request).read()
        jcont = json.loads(content)
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
        request = urllib2.Request(GH_BASE_URL + name)
        request.add_header("Authorization", GH_AUTH)
        request.add_header("Accept", "application/vnd.github.v3+json")
        content = urllib2.urlopen(request, body).read()
        jcont = json.loads(content)
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

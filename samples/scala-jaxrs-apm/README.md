# Swagger Sample App

## Overview
This is a scala project to build a stand-alone server which implements the Swagger spec.  It also demonstrates
a utility from [wordnik-oss](https://github.com/wordnik/wordnik-oss) which allows easy monitoring of performance metrics on your API.  The
metrics are exposed via swagger (of course!).

The profiling itself is based on a [com.wordnik.util.perf.Profile](https://github.com/wordnik/wordnik-oss/blob/master/modules/common-utils/src/main/scala/com/wordnik/util/perf/Profile.scala) class
which wraps function calls into a counter.  In addition to keeping track of counts and timing, you can add 
triggers to perform certain actions based on the measurement.  An example would be sending an alert if a particular
operation takes too long, or an error condition is met.  Or even sending an email if you have a new client sign up!  That's
up to the developer.

The sample app exposes a health endpoint:

<pre>
http://localhost:8002/api/health.json
</pre>

which has a profile operation:

<pre>
http://localhost:8002/api/health.json/profile
</pre>

You can see how the various API calls are calling the Profile utility and wrapping the response.  See [here]() for an example.

Sample output:
<pre>
[
  {
    key: "/pet/*",
    count: 10,
    avgRate: 2500,
    avgDuration: 0.4,
    maxDuration: 4,
    minDuration: 0,
    totalDuration: 4
  }
]
</pre>

### To run (with Maven)
To run the server, run this task:
<pre>
mvn package -Dlog4j.configuration=file:./conf/log4j.properties jetty:run
</pre>

This will start Jetty embedded on port 8002 and apply the logging configuration from conf/log4j.properties

### Testing the server
Once started, you can navigate to http://localhost:8002/api/resources.json to view the Swagger Resource Listing.
This tells you that the server is up and ready to demonstrate Swagger.

### Using the UI
There is an HTML5-based API tool available in a separate project.  This lets you inspect the API using an 
intuitive UI.  You can pull this code from here:  https://github.com/wordnik/swagger-ui

You can then open the src/main/html/index.html file in any HTML5-enabled browser.  Open opening, enter the
URL of your server in the top-centered input box (default is http://localhost:8002/api).  Click the "Explore" 
button and you should see the resources available on the server.

### Applying an API key
The sample app has an implementation of the Swagger ApiAuthorizationFilter.  This restricts access to resources
based on api-key.  There are two keys defined in the sample app:

<li>- default-key</li>

<li>- special-key</li>

When no key is applied, the "default-key" is applied to all operations.  If the "special-key" is entered, a
number of other resources are shown in the UI, including sample CRUD operations.  Note this behavior is similar
to that on http://developer.wordnik.com/docs but the behavior is entirely up to the implementor.
# Swagger Sample App

## Overview
This is an example of swagger integration with the [RESTEasy](http://www.jboss.org/resteasy) JAX-RS framework from
[JBoss](http://www.jboss.org/).  It's a simple, modified example from the RESTEasy 2.3.5 release.

### To run (with Maven)
To run the server, run this task:
<pre>
mvn package -Dlog4j.configuration=file:./log4j.properties jetty:run
</pre>

This will start Jetty embedded on port 8080 and apply the logging configuration from ./log4j.properties

### Testing the server
Once started, you can navigate to http://localhost:9095/resteasy/api-docs.json to view the Swagger Resource Listing.
This tells you that the server is up and ready to demonstrate Swagger.

### Using the UI
There is an HTML5-based API tool available in a separate project.  This lets you inspect the API using an 
intuitive UI.  You can pull this code from here:  https://github.com/wordnik/swagger-ui

You can then open the dist/index.html file in any HTML5-enabled browser.  Open opening, enter the
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

# Swagger Sample App

## Overview
This is a scala project to build a stand-alone server which implements the Swagger spec.  You can find out 
more about both the spec and the framework at http://swagger.wordnik.com.  For more information 
about Wordnik's APIs, please visit http://developer.wordnik.com.

### To build from source
Please follow instructions to build the top-level [swagger-core project](https://github.com/swagger-api/swagger-core)


### To run (with Maven)
To run the server, run this task:
<pre>
mvn package -Dlog4j.configuration=file:./conf/log4j.properties jetty:run
</pre>

This will start Jetty embedded on port 8002 and apply the logging configuration from conf/log4j.properties

### Testing the server
Once started, you can navigate to http://localhost:8002/api-docs to view the Swagger Resource Listing.
This tells you that the server is up and ready to demonstrate Swagger.

### Using the UI
There is an HTML5-based API tool available in a separate project.  This lets you inspect the API using an 
intuitive UI.  You can pull this code from here:  https://github.com/swagger-api/swagger-ui

You can then open the dist/index.html file in any HTML5-enabled browser.  Upen opening, enter the
URL of your server in the top-centered input box (default is http://localhost:8002/api-docs).  Click the "Explore" 
button and you should see the resources available on the server.

### How it works
The Swagger annotations are applied to a [simple servlet](https://github.com/swagger-api/swagger-core/blob/develop-1.3/samples/scala-servlet/src/main/scala/com/wordnik/swagger/sample/servlet/SampleServlet.scala#L14).  As opposed to the JAX-RS implementations, the inputs to the servlet are simply `request` and `response`, so the actual inputs & outputs need to be specified explicitly.  This is done thorough the `@ApiParamsImplicit` annotations.  It's manual, but required for using simple servlets with a metadata specification like Swagger.

Of course, there are limitations to this approach.  If you consume multiple paths in a single servlet method, it's impossible to decorate the method with Swagger.  You can easily add more methods to handle different paths consumed by the servlet--each of those can be decorated with Swagger annotations.

The [Resource Listing](https://github.com/swagger-api/swagger-core/wiki/Resource-Listing) is extracted from the `servlet path`, or where the listing servlet is mounted.  You can see the [example here](https://github.com/swagger-api/swagger-core/blob/develop-1.3/samples/scala-servlet/src/main/webapp/WEB-INF/web.xml#L35):

```xml
<!-- swagger api declaration -->
<servlet>
  <servlet-name>ApiDeclarationServlet</servlet-name>
  <servlet-class>com.wordnik.swagger.servlet.listing.ApiDeclarationServlet</servlet-class>
</servlet>
<servlet-mapping>
  <servlet-name>ApiDeclarationServlet</servlet-name>
  <url-pattern>/api-docs/*</url-pattern>
</servlet-mapping>
```

Each [Api Declaration](https://github.com/swagger-api/swagger-core/wiki/API-Declaration) has a `resourcePath` extracted from the `@Api` path, so the path `/sample/users` will use the `resourcePath` of `/sample`.  Multiple operations under the same resource path will be merged together, so you can have a servlet with `@Api(value = "/sample/users")` and another with `@Api(value = "/sample/houses")`.  This will produce an Api Declaration like such:

```json
{
  "apiVersion": "1.0.0",
  "swaggerVersion": "1.2",
  "basePath": "http://localhost:8002",
  "resourcePath": "/sample",
  "apis": [
    {
      "path": "/sample/users",
      "description": "more about users",
      "operations": [
      ...
    }, 
    {
      "path": "/sample/houses",
      "description": "more about houses",
      "operations": [
      ...
    }
    ...
```

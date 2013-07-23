# Swagger CXF Sample App

## Overview
This is a java project to build a stand-alone server which implements the Swagger spec.  You can find out 
more about both the spec and the framework at http://swagger.wordnik.com.  For more information 
about Wordnik's APIs, please visit http://developer.wordnik.com.  There is an online version of this
server at http://petstore.swagger.wordnik.com/api/api-docs.json

This sample creates an `application` context through the `applicationContext.xml`, allowing the `JAXRSServerFactoryBean` to reflect over property packages to discover swagger-enabled resources.  This was originally contributed by [chadhahn](https://github.com/chadhahn) and adapted by [rvullriede](https://github.com/rvullriede).  Thank you for your contributions!

### To build from source
Please follow instructions to build the top-level [swagger-core project](https://github.com/wordnik/swagger-core)

### To run (with Maven)
To run the server, run this task:
<pre>
mvn package tomcat6:run
</pre>

This will start Tomcat 6 embedded on port 8002.

### Testing the server
Once started, you can navigate to http://localhost:8002/api/api-docs.json to view the Swagger Resource Listing.
This tells you that the server is up and ready to demonstrate Swagger.

### Using the UI
There is an HTML5-based API tool available in a separate project.  This lets you inspect the API using an 
intuitive UI.  You can pull this code from here:  https://github.com/wordnik/swagger-ui

You can then open the dist/index.html file in any HTML5-enabled browser.  Upen opening, enter the
URL of your server in the top-centered input box (default is http://localhost:8002/api/api-docs.json).  Click the "Explore" 
button and you should see the resources available on the server.

### Applying an API key
The sample app has an implementation of the Swagger ApiAuthorizationFilter.  This restricts access to resources
based on api-key.  There are two keys defined in the sample app:

<li>- default-key</li>

<li>- special-key</li>

When no key is applied, the "default-key" is applied to all operations.  If the "special-key" is entered, a
number of other resources are shown in the UI, including sample CRUD operations.  Note this behavior is similar
to that on http://developer.wordnik.com/docs but the behavior is entirely up to the implementor.

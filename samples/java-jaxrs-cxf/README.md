# Swagger CXF Sample App

## Overview
This is a java project to build a stand-alone server which implements the Swagger spec.  You can find out 
more about both the spec and the framework at http://swagger.io.  For more information 
about Wordnik's APIs, please visit http://developer.wordnik.com.  There is an online version of this
server at http://petstore.swagger.io/v2/swagger.json

This sample creates an `application` context through the `applicationContext.xml`, allowing the `JAXRSServerFactoryBean` to reflect over property packages to discover swagger-enabled resources.  This was originally contributed by [chadhahn](https://github.com/chadhahn) and adapted by [rvullriede](https://github.com/rvullriede).  Thank you for your contributions!

### To build from source
Please follow instructions to build the top-level [swagger-core project](https://github.com/swagger-api/swagger-core)

### To run (with Maven)
To run the server, run this task:
<pre>
mvn package tomcat6:run
</pre>

This will start Tomcat 6 embedded on port 8002.

### Testing the server
Once started, you can navigate to http://localhost:8002/api/swagger.json to view the Swagger Resource Listing.
This tells you that the server is up and ready to demonstrate Swagger.

### Using the UI
There is an HTML5-based API tool available in a separate project.  This lets you inspect the API using an 
intuitive UI.  You can pull this code from here:  https://github.com/swagger-api/swagger-ui

As part of the build process, swagger-ui is copied to the webapp, and is available at http://localhost:8002

### Applying an API key
The sample app has an implementation of the Swagger ApiAuthorizationFilter.  This restricts access to resources
based on api-key.  There are two keys defined in the sample app:

<li>- default-key</li>

<li>- special-key</li>

When no key is applied, the "default-key" is applied to all operations.  If the "special-key" is entered, a
number of other resources are shown in the UI, including sample CRUD operations.  Note this behavior is similar
to that on http://developer.wordnik.com/docs but the behavior is entirely up to the implementor.

# Swagger Sample App

## Overview
This is a scala project to build a stand-alone server which implements the Swagger spec.  It demonstrates both
removal of the .{format} in the path as well as placement of specific resource listings under an alternate path.

The default ApiListingResource lives in swagger-jaxrs--it is included by adding the following to the web.xml:

<pre>
<init-param>
  <param-name>com.sun.jersey.config.property.packages</param-name>
  <param-value>com.wordnik.swagger.sample.resource;com.wordnik.swagger.jaxrs;</param-value>
</init-param>
</pre>

Note the <pre>com.wordnik.swagger.jaxrs</pre> contains the default [resource listing](https://github.com/wordnik/swagger-core/blob/master/modules/swagger-jaxrs/src/main/scala/com/wordnik/swagger/jaxrs/ApiListing.scala).
When overriding the default resource listing, simply provide that functionality in a new resource and add it to
the packages.  For this sample, the new resource listing class is [here](https://github.com/wordnik/swagger-core/blob/master/samples/scala-alt-resource-listing/src/main/scala/com/wordnik/swagger/sample/resource/ApiListingResource.scala).
One other requirement is setting the JaxrsApiReader format string to an empty string in a [bootstrap servlet](https://github.com/wordnik/swagger-core/blob/master/samples/scala-alt-resource-listing/src/main/scala/com/wordnik/swagger/sample/Bootstrap.scala)

<pre>
class Bootstrap extends HttpServlet {
  JaxrsApiReader.setFormatString("")
}
</pre>

The api resource paths are also modified to live in the /resources/{api} path.  This is done by declaring both a 
listing path and listing class to two resources:

The actual class implementing the API:

<pre>
@Path("/pet")
@Api(value = "/pet",
  description = "Operations about pets",
  listingPath = "/resources/pet")
@Singleton
@Produces(Array("application/json", "application/xml"))
class PetResourceJSONXML extends PetResource
</pre>

And the class creating the resource listing:

@Path("/resources/pet")
@Api(value = "/pet",
  description = "Operations about pets",
  listingPath = "/resources/pet",
  listingClass = "com.wordnik.swagger.sample.resource.PetResourceJSONXML")
@Singleton
@Produces(Array("application/json", "application/xml"))
class PetResourceListingJSON extends Help

Note in the above, the "listingClass" points to the implementing class.  It also implements the Help trait.

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
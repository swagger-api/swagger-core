# Swagger Sample App

## Overview
This is a java project to build a mule application which implements the Swagger spec.  You can find out more about both the spec and the framework at http://swagger.io.  

For more information about Wordnik's APIs, please visit http://developer.wordnik.com.  There is an online version of this server at http://petstore.swagger.wordnik.com/api/api-docs

### To create a Mule package

Run the following maven command:

```
mvn package
```

### To run 

To run the application, please copy the generated zip file from the target directory to Mule's /apps directory.
Start Mule and it will deploy automatically.

This sample application is available on port 7001.

### Testing the server
Once started, you can navigate to http://localhost:7001/api-docs to view the Swagger Resource Listing.
This tells you that the server is up and ready to demonstrate Swagger.

### Using the UI
There is an HTML5-based API tool available in a separate project.  This lets you inspect the API using an 
intuitive UI.  You can pull this code from here:  https://github.com/swagger-api/swagger-ui

You can then open the dist/index.html file in any HTML5-enabled browser.  Upen opening, enter the
URL of your server in the top-centered input box (default is http://localhost:7001/api-docs).  Click the "Explore"
button and you should see the resources available on the server.

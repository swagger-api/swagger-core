# Swagger Playframework Sample App

## Overview
This is a scala project to build a stand-alone server which implements the Swagger spec.  You can find out 
more about both the spec and the framework at http://swagger.wordnik.com.  For more information 
about Wordnik's APIs, please visit http://developer.wordnik.com.  There is an online version of this
server at http://petstore.swagger.wordnik.com/api/resources.json

### To build from source
Please follow instructions to build the top-level [swagger-core project](https://github.com/wordnik/swagger-core)


### To run
You currently need to build and publish the [swagger-play2](https://github.com/ayush/swagger-play2) module from Ayush Gupta.
This will be merged into swagger-core shortly.  In the meantime:

````
git clone git@github.com:ayush/swagger-play2.git

play publish-local
````

then you can build the sample app:

````
play run
````

The application will listen on port 9000 and respond to `http://localhost:9000/resources.json`

### Limitations
Note the following limitations:

<li>- only `/pet` is available</li>

<li>- api_key filtering is not implemented</li>

### Applying an API key
The sample app has an implementation of the Swagger ApiAuthorizationFilter.  This restricts access to resources
based on api-key.  There are two keys defined in the sample app:

<li>- default-key</li>

<li>- special-key</li>

When no key is applied, the "default-key" is applied to all operations.  If the "special-key" is entered, a
number of other resources are shown in the UI, including sample CRUD operations.  Note this behavior is similar
to that on http://developer.wordnik.com/docs but the behavior is entirely up to the implementor.
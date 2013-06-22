# Swagger oAuth 2.0 Authorization server

## Overview
This is a scala project to build a stand-alone server which implements the Swagger spec and the oAuth 2.0 library
from apache, OLTU.  It contains both a sample authorization server, as well as a sample resource which is backed
by tokens provided by the authorization service.

The authorization service supports both `authorization_code` and `implicit` grant types.  You can view the endpoints
in a [swagger-ui](https://github.com/wordnik/swagger-ui) browser by entering the address `http://localhost:8002/api-docs`.

### To build from source
Please follow instructions to build the top-level [swagger-core project](https://github.com/wordnik/swagger-core)


### To run (with Maven)
To run the server, run this task:
<pre>
mvn package -Dlog4j.configuration=file:./conf/log4j.properties jetty:run
</pre>

This will start Jetty embedded on port 8002 and apply the logging configuration from conf/log4j.properties

You can see the implicit flow by entering a url like this in a browser:

http://localhost:8002/oauth/dialog?redirect_uri=http://localhost:8002/index.html&client_id=someclientid&scope=email

Which will call index.html with an access_token, which can be used to make service calls.

### Extending this sample
While a full support for oAuth 2.0 will become first-class in the [Swagger](http://swagger.wordnik.com) project, you can use
this project as a guideline to start your own.

#### Where to start
Look in the `src/main/scala/com/wordnik/swagger/sample/service` folder for the logic to perform the oAuth operations.  The
client validation logic lives in `ClientValidator.scala`.  The resources protected by the tokens offered from this authorization server live under the `src/main/scala/com/wordnik/swagger/sample/resource` folder.
# Swagger Play2 Utils Module

## Overview
This is a module to support the extensions to swagger for play2. It supports endpoints which give information about the health of your server: memory and threads.
 
### To build from source
The play2-utils module is currently not available in a maven repo or the playframework modules.  To use it, you'll have to
build it from source, which is done by following these steps:

````
cd modules/swagger-play2-utils

play publish-local
````

That will put the swagger-play2-utils module in your local ivy repository for use in your play2 application.  Now you just need to
include it in your application like this:

````
val appDependencies: Seq[sbt.ModuleID] = Seq("swagger-play2-utils" %% "swagger-play2-utils" % "1.1-SNAPSHOT")

````

You can then add support to your app.

### Adding Swagger to your Play2 app

There are just a single step to integrate this plugin's features app with swagger.

Add the resource listing to your routes file (you can read more about the resource listing [here](https://github.com/wordnik/swagger-core/wiki/Resource-Listing)

````

GET     /admin.json/health			        controllers.HealthController.getHealth()
GET     /admin.xml/health			        controllers.HealthController.getHealth()
GET     /admin.json/ping			        controllers.HealthController.ping()
GET     /admin.xml/ping			            controllers.HealthController.ping()

```` 

### Sample Application

Please take a look [here](https://github.com/wordnik/swagger-core/tree/master/samples/scala-play2) for a full sample application using the Swagger Play2 utils module.
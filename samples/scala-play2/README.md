# Swagger Playframework Sample App

## Overview
This is a scala project to build a stand-alone server which implements the Swagger spec.  You can find out 
more about both the spec and the framework at http://swagger.wordnik.com.  For more information 
about Wordnik's APIs, please visit http://developer.wordnik.com.  There is an online version of this
server at http://petstore.swagger.wordnik.com/api/api-docs.json

## Version compatibility
=======
This version is compatible with Play 2.2.0 and Swagger 1.3.10-SNAPSHOT

### To build Swagger from source (optional)
Please follow instructions to build the top-level [swagger-core project](https://github.com/wordnik/swagger-core)

### To run
The swagger-play2 module lives in maven central:

```scala
val appDependencies: Seq[sbt.ModuleID] = Seq(
  /* your other dependencies */
  "com.wordnik" %% "swagger-play2" % "1.3.10-SNAPSHOT"
)
```

You can run the sample app as such:

````
play run
````

The application will listen on port 9000 and respond to `http://localhost:9000/api-docs`

## Using Swagger UI

[Swagger UI](SwaggerUiController) dynamically generates beautiful documentation from the JSON returned by `api-docs`.

To see Swagger UI in action, run the application and point your browser at `http://localhost:9000/api`.

Follow the steps below to set up Swagger UI in your own Play2 project.

### Static assets

The `dist` folder of [swagger-ui](https://github.com/swagger-api/swagger-ui) has been copied into `public/swagger-ui`.
(Make sure to check out the relevant stable tag in the project README instead of using master.)

Then edit `public/swagger-ui/index.html` to specify our `api-docs` resource by default:

```javascript
    $(function () {
      window.swaggerUi = new SwaggerUi({
      url: "http://localhost:9000/api-docs",
      dom_id: "swagger-ui-container",
```

### Controller

Use a controller to redirect requests for `/api` to the Swagger UI static assets - in the sample project, look at `SwaggerUiController`:

```scala
  def api = Action {
    Redirect("/assets/swagger-ui/index.html")
  }
```

### Routes

Add entries for the controller and the static assets to `routes`:

    # Swagger UI
    GET     /api                    controllers.SwaggerUiController.api
    GET     /assets/*file           controllers.Assets.at(path="/public", file)



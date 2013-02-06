# Swagger Play2 Module

## Overview
This is a module to support the play2 framework from [playframework](http://www.playframework.org).  It is written in scala but can be used with either java or scala-based play2 applications.

## Version History

* Version 1.2.1 and greater support scala 2.10 and play 2.1.  If you need to use scala 2.9.x and play 2.0, please use 1.2.0.
 
Usage
-----

You can depend on pre-built libraries in maven central by adding the following dependency:

```
val appDependencies: Seq[sbt.ModuleID] = Seq(
  "com.wordnik" %% "swagger-play2" % "1.2.1-SNAPSHOT"
)
```

Or you can build from source.

```
cd modules/swagger-play2

play
publish-local
```

### Adding Swagger to your Play2 app

There are just a couple steps to integrate your Play2 app with swagger.

1.  Add the resource listing to your routes file (you can read more about the resource listing [here](https://github.com/wordnik/swagger-core/wiki/Resource-Listing)

```

GET     /api-docs.json        controllers.ApiHelpController.getResources

``` 

2.  Annotate your REST endpoints with Swagger annotations.  This allows the Swagger framework to create the [api-declaration](https://github.com/wordnik/swagger-core/wiki/API-Declaration) automatically!

In your controller for, say your "pet" resource:

```scala
@Api(value = "/pet", listingPath = "/api-docs.{format}/pet", description = "Operations about pets")
object PetApiController extends Controller {

  @ApiOperation(value = "Find pet by ID", notes = "Returns a pet", responseClass = "Pet", httpMethod = "GET")
  @ApiErrors(Array(
    new ApiError(code = 400, reason = "Invalid ID supplied"),
    new ApiError(code = 404, reason = "Pet not found")))
  def getPetById(
    @ApiParam(value = "ID of the pet to fetch")@PathParam("id") id: String) = Action { implicit request =>
    petData.getPetbyId(getLong(0, 100000, 0, id)) match {
      case Some(pet) => JsonResponse(pet)
      case _ => JsonResponse(new value.ApiResponse(404, "Pet not found"), 404)
    }
  }

  ...

```

What this does is the following:

* Tells swagger that the methods in this controller should be described under the `/api-docs.json/pet` path

* The Routes file tells swagger that this API listens to `/{id}`

* Describes the operation as a `GET` with the documentation `Find pet by Id` with more detailed notes `Returns a pet ....`

* Takes the param `id`, which is a datatype `string` and a `path` param

* Returns error codes 400 and 404, with the messages provided

In the routes file, you then wire this api as follows:

```
GET     /api-docs.json/pet            controllers.ApiHelpController.getResource(path = "/pet")

GET     /pet.json/:id                 controllers.PetApiController.getPetById(id)
```

This will "attach" the /api-docs.json/pet api to the swagger resource listing, and the method to the `getPetById` method above

#### The ApiParam annotation

Swagger for play has two types of `ApiParam`s--they are `ApiParam` and `ApiParamImplicit`.  The distinction is that some
paramaters (variables) are passed to the method implicitly by the framework.  ALL body parameters need to be described
with `ApiParamImplicit` annotations.  If they are `queryParam`s or `pathParam`s, you can use `ApiParam` annotations.

### Sample Application

Please take a look [here](https://github.com/wordnik/swagger-core/tree/master/samples/scala-play2) for a full sample application using the Swagger Play2 module. 

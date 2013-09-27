package test

import play.modules.swagger._

import org.specs2.mutable._
import org.specs2.mock.Mockito

import test.testdata.{DogController, CatController}
import com.wordnik.swagger.core.{SwaggerSpec, SwaggerContext}
import play.core.Router.Routes
import play.api.Configuration
import com.wordnik.swagger.config.{SwaggerConfig, ConfigFactory}

import org.mockito.Mockito._

import testdata.DogController
import com.wordnik.swagger.model.{Parameter, Operation}
import java.lang.reflect.Method

class PlayApiReaderSpec extends Specification with Mockito {

  "PlayApiReader.SwaggerUtils" should {
    "convert a simple play route comment" in {
      val path = "/pet.json/$id<[^/]+>/test/$nothing<[^/]+>"
      SwaggerUtils.convertPathString(path) must be_==("/pet.json/{id}/test/{nothing}")
    }
    "convert a play route comment with numeric matchers" in {
      val path = """/pet.json/$id<[0-9]+>/test/$nothing<[^/]+>"""
      SwaggerUtils.convertPathString(path) must be_==("/pet.json/{id}/test/{nothing}")
    }
    "convert a play route comment with string matchers" in {
      val path = "/pet.json/$id<[A-Z]+>/test/$nothing<[^/]+>"
      SwaggerUtils.convertPathString(path) must be_==("/pet.json/{id}/test/{nothing}")
    }
    "convert a play route comment with complex matchers" in {
      val path = "/pet.json/$id<[A-Z0-9]+>/test/$nothing<[^/]+>"
      SwaggerUtils.convertPathString(path) must be_==("/pet.json/{id}/test/{nothing}")
    }
  }

  // set up mock for Play Router
  val mockRoutes = mock[play.core.Router.Routes]
  val routesDocumentation: Seq[(String, String, String)] = Seq(
    ("GET", "/api/dog", "test.testdata.DogController.list"),
    ("GET", "/api/dog/:id", "test.testdata.DogController.get1(id: Long)"),
    ("GET", "/api/dog/:dogId", "test.testdata.DogController.get2(id: Long)"),
    ("PUT", "/api/dog", "test.testdata.DogController.add1"),
    ("POST", "/api/dog", "test.testdata.DogController.update"),

    ("GET", "/api/cat", "test.testdata.CatController.list"),

    ("GET", "/api/fly", "test.testdata.FlyController.list")
  )
  mockRoutes.documentation returns routesDocumentation


  "PlayApiReader" should {

    val basePath = "http://localhost/api"
    val swaggerConfig = new SwaggerConfig("1.2.3", SwaggerSpec.version, basePath, "")

    // set up mock for Play Router
    val mockConfig = mock[play.api.Configuration]
    when(mockConfig.getString("swagger.api.format.string")).thenReturn(Some(".json"))

    val reader = new PlayApiReader(Some(mockRoutes), mockConfig)
    val dogControllerClass: Class[_] = DogController.getClass

    val get1Method = dogControllerClass.getMethod("get1", classOf[Long])
    val get2Method = dogControllerClass.getMethod("get2", classOf[Long])
    val get3Method = dogControllerClass.getMethod("get3", classOf[Long])

    def dogMethod(name: String) = {
      try
        Some(dogControllerClass.getMethod(name))
      catch {
        case ex: Exception => None
      }
    }

    "get full name for method" in {
      reader.getFullMethodName(dogControllerClass, dogMethod("add1").get) must beEqualTo("test.testdata.DogController$.add1")
    }

    "get request path for a method" in {
      reader.getPath(dogControllerClass, dogMethod("add1").get).getOrElse("") must beEqualTo("/api/dog")
    }

    "get request path for a method that has no route defined" in {
      reader.getPath(dogControllerClass, dogMethod("no_route").get) must beEqualTo(None)
    }

    "get request path for a method that has path params" in {
      reader.getPath(dogControllerClass, get1Method).getOrElse("") must beEqualTo("/api/dog/:id")
    }

    // todo add some more complex routes - multiple params etc..

    val mockOperation1 = mock[Operation]
    val mockOperation2 = mock[Operation]

    "appendOperation" should {

      "appendOperation should add operation to an empty Map" in {
        val operationsMap: Map[String, List[Operation]] = Map.empty
        val newOperationsMap = reader.appendOperation("key1", mockOperation1, operationsMap)

        newOperationsMap.get("key1").get.length must beEqualTo(1)
        newOperationsMap.get("key1").get.head must beEqualTo(mockOperation1)
      }

      "appendOperation should add operation to new key" in {
        val operationsMap: Map[String, List[Operation]] = Map(("key1", List(mockOperation1)))
        val newOperationsMap = reader.appendOperation("key2", mockOperation2, operationsMap)
        newOperationsMap.get("key1").get.length must beEqualTo(1)
        newOperationsMap.get("key2").get.length must beEqualTo(1)
        newOperationsMap.get("key1").get.head must beEqualTo(mockOperation1)
        newOperationsMap.get("key2").get.head must beEqualTo(mockOperation2)
      }

      "appendOperation should add operation to existing key" in {
        val operationsMap: Map[String, List[Operation]] = Map(("key1", List(mockOperation1)))
        val newOperationsMap = reader.appendOperation("key1", mockOperation2, operationsMap)
        newOperationsMap.get("key1").get.length must beEqualTo(2)
        newOperationsMap.get("key1").get.contains(mockOperation1) must beTrue
        newOperationsMap.get("key1").get.contains(mockOperation2) must beTrue
      }
    }

    "readMethod" should {

      "create Operation for annotated method" in {
        val maybeOperation: Option[Operation] = reader.readMethod(dogMethod("list").get)
        maybeOperation.nonEmpty must beTrue
      }

      "does not creates Operation when no annotation" in {
        val maybeOperation: Option[Operation] = reader.readMethod(dogMethod("no_annotations").get)
        "an operation should not have been returned" ! maybeOperation.isEmpty
      }

      "adds empty 'consumes' when not defined" in {
        val operation: Operation = reader.readMethod(dogMethod("add1").get).get
        operation.consumes must beEqualTo(List.empty)
      }

      "adds 'consumes' when defined and trims" in {
        val operation: Operation = reader.readMethod(dogMethod("add2").get).get
        operation.consumes.length must beEqualTo(1)
        operation.consumes.head must beEqualTo("application/json")
      }

      "adds mulitple 'consumes' when defined and trims" in {
        val operation: Operation = reader.readMethod(dogMethod("add3").get).get
        operation.consumes.length must beEqualTo(2)
        operation.consumes.contains("application/json") must beTrue
        operation.consumes.contains("text/yaml") must beTrue
      }

      "adds empty 'authorizations' when not defined" in {
        val operation: Operation = reader.readMethod(dogMethod("add1").get).get
        operation.authorizations must beEqualTo(List.empty)
      }

      "adds 'authorizations' when defined" in {
        val operation: Operation = reader.readMethod(dogMethod("add2").get).get
        operation.authorizations.length must beEqualTo(1)
        operation.authorizations.head must beEqualTo("vet")
      }

      "adds mulitple 'authorizations' when defined" in {
        val operation: Operation = reader.readMethod(dogMethod("add3").get).get
        operation.authorizations.length must beEqualTo(2)
        operation.authorizations.contains("vet") must beTrue
        operation.authorizations.contains("owner") must beTrue
      }

      "adds empty 'produces' when not defined" in {
        val operation: Operation = reader.readMethod(get1Method).get
        operation.produces must beEqualTo(List.empty)
      }

      "adds 'produces' when defined and trims" in {
        val operation: Operation = reader.readMethod(get2Method).get
        operation.produces.length must beEqualTo(1)
        operation.produces.head must beEqualTo("application/json")
      }

      "adds mulitple 'produces' when defined and trims" in {
        val operation: Operation = reader.readMethod(get3Method).get
        operation.produces.length must beEqualTo(2)
        operation.produces.contains("application/json") must beTrue
        operation.produces.contains("application/xml") must beTrue
      }

      "adds 'protocols' when defined and trims" in {
        val operation: Operation = reader.readMethod(dogMethod("add2").get).get
        operation.protocols.length must beEqualTo(1)
        operation.protocols.head must beEqualTo("http")
      }

      "adds mulitple 'protocols' when defined and trims" in {
        val operation: Operation = reader.readMethod(dogMethod("add3").get).get
        operation.protocols.length must beEqualTo(2)
        operation.protocols.contains("http") must beTrue
        operation.protocols.contains("https") must beTrue
      }

      "adds empty 'protocols' when not defined" in {
        val operation: Operation = reader.readMethod(dogMethod("add1").get).get
        operation.protocols must beEqualTo(List.empty)
      }

      "adds 'deprecated' when defined" in {
        val operation: Operation = reader.readMethod(dogMethod("deprecated").get).get
        operation.deprecated must beEqualTo(Some("true"))
      }

      "does not add 'deprecated' when not defined" in {
        val operation: Operation = reader.readMethod(dogMethod("add1").get).get
        operation.deprecated must beEqualTo(None)
      }

      "adds 'method' when defined" in {
        val operation: Operation = reader.readMethod(dogMethod("add1").get).get
        operation.method must beEqualTo("PUT")
      }

      "adds any-string for 'method' when defined" in {
        val operation: Operation = reader.readMethod(dogMethod("unknown_method").get).get
        operation.method must beEqualTo("UNKNOWN")
      }

      "results in empty String for 'method' when not defined" in {
        val operation: Operation = reader.readMethod(dogMethod("undefined_method").get).get
        operation.method must beEqualTo("")
      }

      "results in empty String for 'nickname' when not defined" in {
        val operation: Operation = reader.readMethod(dogMethod("add1").get).get
        operation.nickname must beEqualTo("")
      }

      "adds 'nickname' when defined" in {
        val operation: Operation = reader.readMethod(dogMethod("add2").get).get
        operation.nickname must beEqualTo("addDog2_nickname")
      }

      "results in empty String for 'notes' when not defined" in {
        val operation: Operation = reader.readMethod(dogMethod("add1").get).get
        operation.notes must beEqualTo("")
      }

      "adds 'notes' when defined" in {
        val operation: Operation = reader.readMethod(dogMethod("add2").get).get
        operation.notes must beEqualTo("Adds a dogs better")
      }


      "adds 'summary' when defined in 'value'" in {
        val operation: Operation = reader.readMethod(dogMethod("add2").get).get
        operation.summary must beEqualTo("addDog2")
      }

      "defaults to 0 for 'position' when not defined" in {
        val operation: Operation = reader.readMethod(dogMethod("add1").get).get
        operation.position must beEqualTo(0)
      }

      "adds 'position' when defined" in {
        val operation: Operation = reader.readMethod(dogMethod("add2").get).get
        operation.position must beEqualTo(2)
      }

      "results in 0 'responseMessages' when not defined" in {
        val operation: Operation = reader.readMethod(dogMethod("add1").get).get
        operation.responseMessages.length must beEqualTo(0)
      }

      "results in 0 'responseMessages' when defined as empty array" in {
        val operation: Operation = reader.readMethod(dogMethod("add2").get).get
        operation.responseMessages.length must beEqualTo(0)
      }

      "adds multiple 'responseMessages' when defined" in {
        val operation: Operation = reader.readMethod(dogMethod("add3").get).get
        operation.responseMessages.length must beEqualTo(2)
        operation.responseMessages.filter(responseMessage => responseMessage.code == 405).head.message must beEqualTo("Invalid input")
        operation.responseMessages.filter(responseMessage => responseMessage.code == 666).head.message must beEqualTo("Big Problem")
      }

      "returns 'void' for 'responseClass' when not defined" in {
        val operation: Operation = reader.readMethod(dogMethod("add1").get).get
        operation.responseClass must beEqualTo("java.lang.Void")
      }

      "adds 'responseClass' when defined" in {
        val operation: Operation = reader.readMethod(get1Method).get
        operation.responseClass must beEqualTo("test.testdata.Dog")
      }

      "adds 'responseClass' correctly when defined as list" in {
        val operation = reader.readMethod(dogMethod("list").get).get
        operation.responseClass must beEqualTo("List[test.testdata.Dog]")
      }

      "results in 0 'parameters' when not defined" in {
        val operation: Operation = reader.readMethod(dogMethod("list").get).get
        operation.parameters must beEqualTo(List.empty)
      }

      "adds 'parameters' when defined by @ApiParam" in {
        val operation: Operation = reader.readMethod(get1Method).get
        operation.parameters.length must beEqualTo(1)
        operation.parameters.head must beEqualTo(Parameter("dogId", Some("ID of dog to fetch"), None, true, false, "long", null, "path", None))
      }

      "adds 'parameters' when defined by @ApiImplicitParams" in {
        val operation: Operation = reader.readMethod(dogMethod("add1").get).get
        operation.parameters.length must beEqualTo(1)
        operation.parameters.head must beEqualTo(Parameter("dog", Some("Dog object to add"), None, true, false, "Dog", null, "body", None))
      }

    }

    "read" should {

      "create APIListing for annotated class" in {
        val maybeAPIListing = reader.read("docroot", dogControllerClass, swaggerConfig)
        maybeAPIListing.nonEmpty must beTrue
      }

      //todo -  more to add...
    }


  }

}
package test

import play.modules.swagger._

import org.specs2.mutable._
import org.specs2.mock.Mockito

import test.testdata.{DogController, CatController}
import com.wordnik.swagger.core.SwaggerContext

class PlayApiReaderSpec extends Specification with Mockito {

  "PlayApiReader.SwaggerUtils" should {
    "convert a simple play route comment" in {
      val path = """/pet.json/$id<[^/]+>/test/$nothing<[^/]+>"""
      SwaggerUtils.convertPathString(path) must be_==( """/pet.json/{id}/test/{nothing}""")
    }
    "convert a play route comment with numeric matchers" in {
      val path = """/pet.json/$id<[0-9]+>/test/$nothing<[^/]+>"""
      SwaggerUtils.convertPathString(path) must be_==( """/pet.json/{id}/test/{nothing}""")
    }
    "convert a play route comment with string matchers" in {
      val path = """/pet.json/$id<[A-Z]+>/test/$nothing<[^/]+>"""
      SwaggerUtils.convertPathString(path) must be_==( """/pet.json/{id}/test/{nothing}""")
    }
    "convert a play route comment with complex matchers" in {
      val path = """/pet.json/$id<[A-Z0-9]+>/test/$nothing<[^/]+>"""
      SwaggerUtils.convertPathString(path) must be_==( """/pet.json/{id}/test/{nothing}""")
    }
  }

  // set up mocks
  val mockRoutes = mock[play.core.Router.Routes]
  val routesDocumentation: Seq[(String, String, String)] = Seq(
    ("GET", "/api/dog", "test.testdata.DogController.list"),
    ("GET", "/api/dog/:id", "test.testdata.DogController.get(id: Long)"),
    ("PUT", "/api/dog", "test.testdata.DogController.addDog"),
    ("POST", "/api/dog", "test.testdata.DogController.update"),

    ("GET", "/api/cat", "test.testdata.CatController.list"),

    ("GET", "/api/fly", "test.testdata.FlyController.list")
  )
  mockRoutes.documentation returns routesDocumentation

  "PlayApiScanner" should {
    "identify API classes from router" in {
      val classes = new PlayApiScanner(Some(mockRoutes)).classes()
      classes.length must beEqualTo(2)
      classes.find(clazz => clazz == SwaggerContext.loadClass("test.testdata.DogController")).nonEmpty must beTrue
      classes.find(clazz => clazz == SwaggerContext.loadClass("test.testdata.CatController")).nonEmpty must beTrue
    }
  }

}
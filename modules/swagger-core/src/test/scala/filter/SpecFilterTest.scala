package filter

import io.swagger.core.filter.SpecFilter
import io.swagger.models.{ModelImpl, Swagger}
import io.swagger.util.Json
import matchers.SerializationMatchers._
import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner
import org.scalatest.{FlatSpec, Matchers}

import scala.collection.JavaConverters._
import scala.io.Source

@RunWith(classOf[JUnitRunner])
class SpecFilterTest extends FlatSpec with Matchers {
  it should "clone everything" in {
    val json = Source.fromFile("src/test/scala/specFiles/petstore.json").mkString
    val swagger = Json.mapper().readValue(json, classOf[Swagger])
    val filtered = new SpecFilter().filter(swagger, new NoOpOperationsFilter(), null, null, null)

    Json.pretty(swagger) should equal(Json.pretty(filtered))
  }

  it should "clone everything from JSON without models" in {
    val json = Source.fromFile("src/test/scala/specFiles/noModels.json").mkString
    val swagger = Json.mapper().readValue(json, classOf[Swagger])
    val filtered = new SpecFilter().filter(swagger, new NoOpOperationsFilter(), null, null, null)

    filtered should serializeToJson(json)
  }

  it should "filter away get operations in a resource" in {
    val json = Source.fromFile("src/test/scala/specFiles/petstore.json").mkString
    val swagger = Json.mapper().readValue(json, classOf[Swagger])
    val filter = new NoGetOperationsFilter()

    val filtered = new SpecFilter().filter(swagger, filter, null, null, null)

    if (filtered.getPaths() != null) {
      for ((path, i) <- filtered.getPaths().asScala) {
        i.getGet() should be(null)
      }
    }
    else
      fail("paths should not be null")
  }

  it should "filter away the store resource" in {
    val json = Source.fromFile("src/test/scala/specFiles/petstore.json").mkString
    val swagger = Json.mapper().readValue(json, classOf[Swagger])
    val filter = new NoUserOperationsFilter()

    val filtered = new SpecFilter().filter(swagger, filter, null, null, null)

    if (filtered.getPaths() != null) {
      for ((path, i) <- filtered.getPaths().asScala) {
        path should not be ("/user")
      }
    }
    else
      fail("paths should not be null")
  }

  it should "filter away secret parameters" in {
    val json = Source.fromFile("src/test/scala/specFiles/sampleSpec.json").mkString
    val swagger = Json.mapper().readValue(json, classOf[Swagger])
    val filter = new RemoveInternalParamsFilter()

    val filtered = new SpecFilter().filter(swagger, filter, null, null, null)

    if (filtered.getPaths() != null) {
      for ((path, i) <- filtered.getPaths().asScala) {
        val get = i.getGet()
        for (param <- get.getParameters().asScala) {
          param.getDescription should not be (null)
          param.getDescription.startsWith("secret") should not be (true)
        }
      }
    }
    else
      fail("paths should not be null")
  }

  it should "filter away internal model properties" in {
    val json = Source.fromFile("src/test/scala/specFiles/sampleSpec.json").mkString
    val swagger = Json.mapper().readValue(json, classOf[Swagger])
    val filter = new InternalModelPropertiesRemoverFilter()

    val filtered = new SpecFilter().filter(swagger, filter, null, null, null)
    for ((key, model) <- filtered.getDefinitions().asScala) {
      for ((propName, prop) <- model.getProperties().asScala) {
        propName.startsWith("_") should be(false)
      }
    }
  }

  it should "filter models where some fields have no properties" in {
    val modelName = "Array"
    val model = new ModelImpl().`type`(ModelImpl.OBJECT).name(modelName)

    val swagger = new Swagger()
    swagger.addDefinition(modelName, model)

    val filtered = new SpecFilter().filterDefinitions(new NoOpOperationsFilter(), swagger.getDefinitions,
      null, null, null)

    if (filtered.size() != 1)
      fail("ModelImpl with no properties failed to filter")
  }
}

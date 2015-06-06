import io.swagger.jaxrs.Reader
import io.swagger.models.parameters.BodyParameter
import io.swagger.models.properties.{RefProperty, StringProperty}
import io.swagger.models.{ArrayModel, Model, Swagger}
import org.junit.runner.RunWith
import org.scalatest.{FlatSpec, Matchers}
import org.scalatest.junit.JUnitRunner
import resources._

@RunWith(classOf[JUnitRunner])
class PostParamTest extends FlatSpec with Matchers {
  it should "find a Post operation with single object" in {
    val swagger = new Reader(new Swagger()).read(classOf[PostParamResource])
    val petPath = swagger.getPaths().get("/pet/singleObject")
    petPath should not be (null)

    petPath.getGet() should be(null)
    val petPost = petPath.getPost()
    petPost should not be (null)
    petPost.getParameters().size() should be(1)
    val petPostBodyParam = (petPost.getParameters().get(0)).asInstanceOf[BodyParameter]
    petPostBodyParam.getName() should equal("body")
    (petPostBodyParam.getSchema().isInstanceOf[Model]) should be(true)
    swagger.getDefinitions.get("Pet").getProperties.get("status").getAccess() should be("public")
  }

  it should "find a Post operation with list of objects" in {
    val swagger = new Reader(new Swagger()).read(classOf[PostParamResource])
    val petPath = swagger.getPaths().get("/pet/listOfObjects")
    petPath should not be (null)
    val petPost = petPath.getPost()
    petPost should not be (null)
    petPost.getParameters().size() should be(1)
    val petPostBodyParam = (petPost.getParameters().get(0)).asInstanceOf[BodyParameter]

    petPostBodyParam.getName() should equal("body")
    val inputModel = petPostBodyParam.getSchema()
    inputModel.isInstanceOf[ArrayModel] should be(true)
    val ap = inputModel.asInstanceOf[ArrayModel]
    val inputSchema = ap.getItems()
    inputSchema.isInstanceOf[RefProperty] should be(true)
    val rm = inputSchema.asInstanceOf[RefProperty]
    rm.getSimpleRef() should be("Pet")
  }

  it should "find a Post operation with collection of objects" in {
    val swagger = new Reader(new Swagger()).read(classOf[PostParamResource])
    val petPath = swagger.getPaths().get("/pet/collectionOfObjects")
    petPath should not be (null)
    val petPost = petPath.getPost()
    petPost should not be (null)
    petPost.getParameters().size() should be(1)
    val petPostBodyParam = (petPost.getParameters().get(0)).asInstanceOf[BodyParameter]

    petPostBodyParam.getName() should equal("body")
    val inputModel = petPostBodyParam.getSchema()
    inputModel.isInstanceOf[ArrayModel] should be(true)
    val ap = inputModel.asInstanceOf[ArrayModel]
    val inputSchema = ap.getItems()
    inputSchema.isInstanceOf[RefProperty] should be(true)
    val rm = inputSchema.asInstanceOf[RefProperty]
  }

  it should "find a Post operation with an array of objects" in {
    val swagger = new Reader(new Swagger()).read(classOf[PostParamResource])
    val petPath = swagger.getPaths().get("/pet/arrayOfObjects")
    petPath should not be (null)
    val petPost = petPath.getPost()
    petPost should not be (null)
    petPost.getParameters().size() should be(1)
    val petPostBodyParam = (petPost.getParameters().get(0)).asInstanceOf[BodyParameter]

    petPostBodyParam.getName() should equal("body")
    val inputModel = petPostBodyParam.getSchema()
    inputModel.isInstanceOf[ArrayModel] should be(true)
    val ap = inputModel.asInstanceOf[ArrayModel]
    val inputSchema = ap.getItems()
    inputSchema.isInstanceOf[RefProperty] should be(true)
    val rm = inputSchema.asInstanceOf[RefProperty]
  }

  it should "find a Post operation with single string" in {
    val swagger = new Reader(new Swagger()).read(classOf[PostParamResource])
    val petPath = swagger.getPaths().get("/pet/singleString")
    petPath should not be (null)

    petPath.getGet() should be(null)
    val petPost = petPath.getPost()
    petPost should not be (null)
    petPost.getParameters().size() should be(1)
    val petPostBodyParam = (petPost.getParameters().get(0)).asInstanceOf[BodyParameter]
    petPostBodyParam.getName() should equal("body")
    (petPostBodyParam.getSchema().isInstanceOf[Model]) should be(true)
  }

  it should "find a Post operation with list of strings" in {
    val swagger = new Reader(new Swagger()).read(classOf[PostParamResource])
    val petPath = swagger.getPaths().get("/pet/listOfStrings")
    petPath should not be (null)
    val petPost = petPath.getPost()
    petPost should not be (null)
    petPost.getParameters().size() should be(1)
    val petPostBodyParam = (petPost.getParameters().get(0)).asInstanceOf[BodyParameter]

    petPostBodyParam.getName() should equal("body")
    val inputModel = petPostBodyParam.getSchema()
    inputModel.isInstanceOf[ArrayModel] should be(true)
    val ap = inputModel.asInstanceOf[ArrayModel]
    val inputSchema = ap.getItems()
    inputSchema.isInstanceOf[StringProperty] should be(true)
    val rm = inputSchema.asInstanceOf[StringProperty]
  }

  it should "find a Post operation with collection of strings" in {
    val swagger = new Reader(new Swagger()).read(classOf[PostParamResource])
    val petPath = swagger.getPaths().get("/pet/collectionOfStrings")
    petPath should not be (null)
    val petPost = petPath.getPost()
    petPost should not be (null)
    petPost.getParameters().size() should be(1)
    val petPostBodyParam = (petPost.getParameters().get(0)).asInstanceOf[BodyParameter]

    petPostBodyParam.getName() should equal("body")
    val inputModel = petPostBodyParam.getSchema()
    inputModel.isInstanceOf[ArrayModel] should be(true)
    val ap = inputModel.asInstanceOf[ArrayModel]
    val inputSchema = ap.getItems()
    inputSchema.isInstanceOf[StringProperty] should be(true)
  }

  it should "find a Post operation with an array of strings" in {
    val swagger = new Reader(new Swagger()).read(classOf[PostParamResource])
    val petPath = swagger.getPaths().get("/pet/arrayOfStrings")
    petPath should not be (null)
    val petPost = petPath.getPost()
    petPost should not be (null)
    petPost.getParameters().size() should be(1)
    val petPostBodyParam = (petPost.getParameters().get(0)).asInstanceOf[BodyParameter]
    petPostBodyParam.getName() should equal("body")
    val inputModel = petPostBodyParam.getSchema()
    inputModel.isInstanceOf[ArrayModel] should be(true)
    val ap = inputModel.asInstanceOf[ArrayModel]
    val inputSchema = ap.getItems()
    inputSchema.isInstanceOf[StringProperty] should be(true)
  }
}
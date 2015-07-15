import io.swagger.jaxrs.Reader
import io.swagger.models._
import io.swagger.models.parameters.{BodyParameter, QueryParameter}
import io.swagger.models.properties._
import models.TestEnum
import org.junit.runner.RunWith
import org.scalatest.{FlatSpec, Matchers}
import org.scalatest.junit.JUnitRunner
import resources.ResourceWithGenerics

import scala.collection.JavaConverters.{asScalaBufferConverter, asScalaSetConverter}

@RunWith(classOf[JUnitRunner])
class GenericsTest extends FlatSpec with Matchers {
  val swagger = new Reader(new Swagger()).read(classOf[ResourceWithGenerics])
  val enumValues = (for (item <- TestEnum.values()) yield item.name()).toSet

  def testEnumCollection(p: QueryParameter, name: String) = {
    testCollection(p, name, "string", null)
    val schema = p.getItems.asInstanceOf[StringProperty]
    schema.getEnum.asScala.toSet should be(enumValues)
  }

  def testCollection(p: QueryParameter, name: String, `type`: String, format: String) = {
    p.getName should be(name)
    p.getType should be("array")
    p.getFormat should be(null)
    p.getCollectionFormat should be("multi")
    p.getItems should not be (null)
    val schema = p.getItems.asInstanceOf[Property]
    schema.getType should be(`type`)
    schema.getFormat should be(format)
  }

  def testScalar(p: QueryParameter, name: String, `type`: String, format: String) = {
    p.getName should be(name)
    p.getType should be(`type`)
    p.getFormat should be(format)
    p.getCollectionFormat should be(null)
    p.getItems should be(null)
  }

  def testGenericType(op: Operation, `type`: String) = {
    getBodyParameter(op, 0).getSchema.asInstanceOf[RefModel].getSimpleRef should be(`type`)
  }

  def getBodyParameter(op: Operation, index: Int) = {
    op.getParameters.get(index).asInstanceOf[BodyParameter]
  }

  def getOperation(name: String) = {
    swagger.getPath("/generics/" + name).getPost
  }

  def getQueryParameter(op: Operation, index: Int) = {
    op.getParameters.get(index).asInstanceOf[QueryParameter]
  }

  def getProperty(`type`: String, name: String) = {
    swagger.getDefinitions.get(`type`).asInstanceOf[ModelImpl].getProperties.get(name)
  }

  it should "check collections of integers" in {
    val op = getOperation("testIntegerContainers")
    op.getParameters().size() should be(8)
    testCollection(getQueryParameter(op, 0), "set", "integer", "int32")
    testCollection(getQueryParameter(op, 1), "list", "integer", "int32")
    testCollection(getQueryParameter(op, 2), "list2D", "string", null)
    testCollection(getQueryParameter(op, 3), "array", "integer", "int32")
    testCollection(getQueryParameter(op, 4), "arrayP", "integer", "int32")
    testScalar(getQueryParameter(op, 5), "scalar", "integer", "int32")
    testScalar(getQueryParameter(op, 6), "scalarP", "integer", "int32")
    testCollection(getQueryParameter(op, 7), "forced", "integer", "int32")
  }

  it should "check collections of strings" in {
    val op = getOperation("testStringContainers")
    op.getParameters().size() should be(5)
    val set = getQueryParameter(op, 0)
    testCollection(set, "set", "string", null)
    set.getItems.asInstanceOf[StringProperty].getEnum.asScala.toList should be(List("1", "2", "3"))
    testCollection(getQueryParameter(op, 1), "list", "string", null)
    testCollection(getQueryParameter(op, 2), "list2D", "string", null)
    testCollection(getQueryParameter(op, 3), "array", "string", null)
    testScalar(getQueryParameter(op, 4), "scalar", "string", null)
  }

  it should "check collections of objects" in {
    val op = getOperation("testObjectContainers")
    op.getParameters().size() should be(5)
    testCollection(getQueryParameter(op, 0), "set", "string", null)
    testCollection(getQueryParameter(op, 1), "list", "string", null)
    testCollection(getQueryParameter(op, 2), "list2D", "string", null)
    testCollection(getQueryParameter(op, 3), "array", "string", null)
    testScalar(getQueryParameter(op, 4), "scalar", "string", null)
  }

  it should "check collections of enumerations" in {
    val op = getOperation("testEnumContainers")
    op.getParameters().size() should be(5)
    testEnumCollection(getQueryParameter(op, 0), "set")
    testEnumCollection(getQueryParameter(op, 1), "list")
    testCollection(getQueryParameter(op, 2), "list2D", "string", null)
    testEnumCollection(getQueryParameter(op, 3), "array")
    val scalar = getQueryParameter(op, 4)
    testScalar(scalar, "scalar", "string", null)
    scalar.getEnum.asScala.toSet should be(enumValues)
  }

  it should "check collection of strings as body parameter" in {
    val op = getOperation("testStringsInBody")
    op.getParameters().size() should be(1)
    val p = getBodyParameter(op, 0)
    val strArray = p.getSchema.asInstanceOf[ArrayModel]
    strArray.getItems.asInstanceOf[Property].getType should be("string")
  }

  it should "check collection of objects as body parameter" in {
    val op = getOperation("testObjectsInBody")
    op.getParameters().size() should be(1)
    val p = getBodyParameter(op, 0)
    val objArray = p.getSchema.asInstanceOf[ArrayModel]
    objArray.getItems.asInstanceOf[RefProperty].getSimpleRef should be("Tag")
  }

  it should "check collection of enumerations as body parameter" in {
    val op = getOperation("testEnumsInBody")
    op.getParameters().size() should be(1)
    val p = getBodyParameter(op, 0)
    val enumArray = p.getSchema.asInstanceOf[ArrayModel]
    enumArray.getItems.asInstanceOf[StringProperty].getEnum.asScala.toSet should be(enumValues)
  }

  it should "check 2D array as body parameter" in {
    val op = getOperation("test2DInBody")
    op.getParameters().size() should be(1)
    val p = getBodyParameter(op, 0)
    val ddArray = p.getSchema.asInstanceOf[ArrayModel]
    ddArray.getItems.asInstanceOf[ArrayProperty].getItems.asInstanceOf[RefProperty].getSimpleRef should be("Tag")
  }

  it should "check parameters of generic types" in {
    val genericTypes = Set("GenericTypeString", "GenericTypeUUID", "GenericTypeGenericTypeString",
      "RenamedGenericTypeString", "RenamedGenericTypeRenamedGenericTypeString")
    (swagger.getDefinitions.keySet.asScala & genericTypes) should be(genericTypes)

    val opString = getOperation("testGenericType")
    testGenericType(opString, "GenericTypeString")
    val strValue = getProperty("GenericTypeString", "value")
    strValue should not be (null)
    strValue.getClass().getName() should be(classOf[StringProperty].getName)

    val opUUID = getOperation("testStringBasedGenericType")
    testGenericType(opUUID, "GenericTypeUUID")
    val uuidValue = getProperty("GenericTypeUUID", "value")
    uuidValue should not be (null)
    uuidValue.getClass().getName() should be(classOf[UUIDProperty].getName)

    val opComplex = getOperation("testComplexGenericType")
    testGenericType(opComplex, "GenericTypeGenericTypeString")
    val complexValue = getProperty("GenericTypeGenericTypeString", "value")
    complexValue should not be (null)
    complexValue.getClass().getName() should be(classOf[RefProperty].getName)
    complexValue.asInstanceOf[RefProperty].getSimpleRef should be("GenericTypeString")

    val opRenamed = getOperation("testRenamedGenericType")
    testGenericType(opRenamed, "RenamedGenericTypeRenamedGenericTypeString")
    val renamedComplexValue = getProperty("RenamedGenericTypeRenamedGenericTypeString", "value")
    renamedComplexValue should not be (null)
    renamedComplexValue.isInstanceOf[RefProperty] should be(true)
    renamedComplexValue.asInstanceOf[RefProperty].getSimpleRef should be("RenamedGenericTypeString")
  }

  it should "check generic result" in {
    val op = swagger.getPath("/generics/testGenericResult").getGet
    val schema = op.getResponses().get("200").getSchema
    schema.getClass.getName should be(classOf[RefProperty].getName)
    schema.asInstanceOf[RefProperty].getSimpleRef should be("GenericListWrapperTag")

    val entries = getProperty("GenericListWrapperTag", "entries")
    entries should not be (null)
    entries.getClass.getName should be(classOf[ArrayProperty].getName)

    val items = entries.asInstanceOf[ArrayProperty].getItems
    items.getClass.getName should be(classOf[RefProperty].getName)
    items.asInstanceOf[RefProperty].getSimpleRef should be("Tag")
  }
}

package com.wordnik.test.swagger.core

import com.wordnik.swagger.core._
import com.wordnik.swagger.core.util._

import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner
import org.scalatest.FlatSpec
import org.scalatest.matchers.ShouldMatchers

import scala.collection.JavaConversions._
import scala.reflect.BeanProperty

@RunWith(classOf[JUnitRunner])
class AllowableValuesSerializationTest extends FlatSpec with ShouldMatchers {
  behavior of "documentation"
  it should "serialize" in {
    val doc = new Documentation("api-12345",
      SwaggerSpec.version,
      "http://www.foo.com/api",
      "/myresource")
    val api = new DocumentationEndPoint("store", "a test endpoint")
    val operation = new DocumentationOperation("GET",
      "gets an item from a store",
      "returns just one")

    val param = new DocumentationParameter(
      "idRange",
      "range of allowable ids",
      "you can only supply id values within this range",
      "query",
      null,
      new DocumentationAllowableListValues(List("cat", "dog", "monkey")),
      true,
      false)

    operation.addParameter(param)

    api.addOperation(operation)
    doc.addApi(api)
    val json = JsonUtil.getJsonMapper.writeValueAsString(doc)
    val um = JsonUtil.getJsonMapper.readValue(json, classOf[Documentation])
    val json2 = JsonUtil.getJsonMapper.writeValueAsString(um)
    assert(json === json2)
    assert(um.swaggerVersion === SwaggerSpec.version)
  }
}
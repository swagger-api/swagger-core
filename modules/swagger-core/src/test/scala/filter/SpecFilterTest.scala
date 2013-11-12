package filter

import com.wordnik.swagger.core.util.ScalaJsonUtil
import com.wordnik.swagger.core.filter._
import com.wordnik.swagger.model._
import com.wordnik.swagger.core.util._

import org.json4s._
import org.json4s.JsonDSL._
import org.json4s.jackson.JsonMethods._
import org.json4s.native.Serialization.{read, write}

import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner
import org.scalatest.FlatSpec
import org.scalatest.matchers.ShouldMatchers

@RunWith(classOf[JUnitRunner])
class SpecFilterTest extends FlatSpec with ShouldMatchers {
  implicit val formats = SwaggerSerializers.formats

  behavior of "SpecFilter"

  it should "filter an api spec and return all models" in {
    val spec = TestSpecs.getSimple
    val p = new SpecFilter().filter(spec, new SimpleFilter, Map(), Map(), Map())
    p.apis.size should be (4)
    (p.models.get.keys.toSet & Set("Pet", "Category", "Tag")).size should be (3)
  }

  it should "filter away all non-get operations" in {
    val spec = TestSpecs.getSimple
    val p = new SpecFilter().filter(spec, new GetOnlyFilter, Map(), Map(), Map())
    p.apis.size should be (3)
    (p.models.get.keys.toSet & Set("Pet", "Category", "Tag")).size should be (3)
  }

  it should "filter away everything" in {
    val spec = TestSpecs.getSimple
    val p = new SpecFilter().filter(spec, new EatEverythingFilter, Map(), Map(), Map())
    p.apis.size should be (0)
    p.models should be (None)
  }

  it should "filter away secret params" in {
    val spec = TestSpecs.getSimple
    val p = new SpecFilter().filter(spec, new SecretParamFilter, Map(), Map(), Map())

    p.apis.foreach(api => {
      if(api.path == "/pet.{format}") {
        api.operations.foreach(op => {
          if(op.method == "POST") {
            op.parameters.size should be (0)
          }
        })
      }
    })
  }

  it should "return models in List properties" in {
    val spec = TestSpecs.getReturnTypeWithList
    val p = new SpecFilter().filter(spec, new SimpleFilter, Map(), Map(), Map())
    p.apis.size should be (1)
    p.models.get.size should be (2)
  }

  it should "ensure order is preserved after filtering" in {
    val spec = TestSpecs.ordered
    val p = new SpecFilter().filter(spec, new SimpleFilter, Map(), Map(), Map())

    p.apis.size should be (1)
    val ops = p.apis(0).operations
    ops.size should be (2)
    ops(0).method should be ("POST")
    ops(1).method should be ("GET")
  }

  it should "maintain declared subTypes" in {
    val spec = TestSpecs.subTypes
    spec.models.get.size should be (3)

    val filtered = new SpecFilter().filter(spec, new SimpleFilter, Map(), Map(), Map())
    filtered.models.get.keys should be (Set("Animal", "WildAnimal", "DomesticAnimal"))
  }
}

class SimpleFilter extends SwaggerSpecFilter {
  override def isOperationAllowed(operation: Operation, api: ApiDescription, params: java.util.Map[String, java.util.List[String]], cookies: java.util.Map[String, String], headers: java.util.Map[String, java.util.List[String]]): Boolean = true
  override def isParamAllowed(parameter: Parameter, operation: Operation, api: ApiDescription, params: java.util.Map[String, java.util.List[String]], cookies: java.util.Map[String, String], headers: java.util.Map[String, java.util.List[String]]): Boolean = true
}

class GetOnlyFilter extends SwaggerSpecFilter {
  override def isOperationAllowed(operation: Operation, api: ApiDescription, params: java.util.Map[String, java.util.List[String]], cookies: java.util.Map[String, String], headers: java.util.Map[String, java.util.List[String]]): Boolean = {
    if(operation.method != "GET") false
    else true
  }
  override def isParamAllowed(parameter: Parameter, operation: Operation, api: ApiDescription, params: java.util.Map[String, java.util.List[String]], cookies: java.util.Map[String, String], headers: java.util.Map[String, java.util.List[String]]): Boolean = true
}

class EatEverythingFilter extends SwaggerSpecFilter {
  override def isOperationAllowed(operation: Operation, api: ApiDescription, params: java.util.Map[String, java.util.List[String]], cookies: java.util.Map[String, String], headers: java.util.Map[String, java.util.List[String]]): Boolean = false
  override def isParamAllowed(parameter: Parameter, operation: Operation, api: ApiDescription, params: java.util.Map[String, java.util.List[String]], cookies: java.util.Map[String, String], headers: java.util.Map[String, java.util.List[String]]): Boolean = true
}

class SecretParamFilter extends SwaggerSpecFilter {
  override def isOperationAllowed(operation: Operation, api: ApiDescription, params: java.util.Map[String, java.util.List[String]], cookies: java.util.Map[String, String], headers: java.util.Map[String, java.util.List[String]]): Boolean = true
  override def isParamAllowed(parameter: Parameter, operation: Operation, api: ApiDescription, params: java.util.Map[String, java.util.List[String]], cookies: java.util.Map[String, String], headers: java.util.Map[String, java.util.List[String]]): Boolean = {
    if(parameter.paramAccess == Some("secret")) false
    else true
  }
}
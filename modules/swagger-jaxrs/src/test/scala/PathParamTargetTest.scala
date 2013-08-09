import testresources._

import com.wordnik.swagger.jaxrs.reader._
import com.wordnik.swagger.core.util._
import com.wordnik.swagger.model._
import com.wordnik.swagger.config._
import com.wordnik.swagger.core.filter._

import java.lang.reflect.Method

import java.util.Date

import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner
import org.scalatest.FlatSpec
import org.scalatest.matchers.ShouldMatchers

import scala.collection.mutable.ListBuffer

@RunWith(classOf[JUnitRunner])
class PathParamTargetTest extends FlatSpec with ShouldMatchers {
  it should "honor a path param target at the class level" in {
    val reader = new DefaultJaxrsApiReader
    val config = new SwaggerConfig()
    val apiResource = reader.read("/api-docs", classOf[PathParamTargetResource], config).getOrElse(fail("should not be None"))

    val apis = apiResource.apis

    apis.size should be (2)

    // verify the order
    apis(0).path should be ("/pathParamTest/{id}/details")
    apis(1).path should be ("/pathParamTest/{id}")

    val rootOps = apis.filter(_.path == "/pathParamTest/{id}").head.operations
    rootOps.size should be (1)
    val op = rootOps.head
    op.parameters.size should be (2)

    val id = op.parameters(0)
    id.name should be ("id")
    id.allowableValues should be (AllowableRangeValues("0.0", "10.0"))
    id.dataType should be ("string")

    val qpt = op.parameters(1)
    qpt.name should be ("qp")
    qpt.allowableValues should be (AllowableListValues(List("a", "b", "c")))
    qpt.dataType should be ("string")

    // verify the 2nd api
    val detailsOps = apis.filter(_.path == "/pathParamTest/{id}/details").head.operations
    val detailOp = detailsOps.head
    detailOp.parameters.size should be (1)

    val detailId = detailOp.parameters(0)
    detailId.name should be ("id")
    detailId.allowableValues should be (AllowableRangeValues("0.0", "10.0"))
    detailId.dataType should be ("string")
  }
}

@RunWith(classOf[JUnitRunner])
class JavaPathParamTargetTest extends FlatSpec with ShouldMatchers {
  it should "honor a path param target at the class level" in {
    val reader = new DefaultJaxrsApiReader
    val config = new SwaggerConfig()
    val apiResource = reader.read("/api-docs", classOf[JavaPathParamTargetResource], config).getOrElse(fail("should not be None"))
    val apis = apiResource.apis

    apis.size should be (2)
    val rootOps = apis.filter(_.path == "/javaPathParamTest/{id}").head.operations
    rootOps.size should be (1)
    val op = rootOps.head
    op.parameters.size should be (3)

    val name = op.parameters(0)
    name.name should be ("name")    
    name.dataType should be ("string")
    
    val id = op.parameters(1)
    id.name should be ("id")
    id.allowableValues should be (AllowableRangeValues("0.0", "10.0"))
    id.dataType should be ("string")

    val qpt = op.parameters(2)
    qpt.name should be ("qp")
    qpt.allowableValues should be (AllowableListValues(List("a", "b", "c")))
    qpt.dataType should be ("string")

    // verify the 2nd api
    val detailsOps = apis.filter(_.path == "/javaPathParamTest/{id}/details").head.operations
    val detailOp = detailsOps.head

    detailOp.parameters.size should be (3)

    val detailName = op.parameters(0)
    detailName.name should be ("name")    
    detailName.dataType should be ("string")
    
    val detailId = detailOp.parameters(1)
    detailId.name should be ("id")
    detailId.allowableValues should be (AllowableRangeValues("0.0", "10.0"))
    detailId.dataType should be ("string")

    val bodyParam = detailOp.parameters(2)
    bodyParam.name should be ("body")
    bodyParam.allowableValues should be (AllowableListValues(List("1","2","3")))

    bodyParam.dataType should be ("Array[int]")
  }
}
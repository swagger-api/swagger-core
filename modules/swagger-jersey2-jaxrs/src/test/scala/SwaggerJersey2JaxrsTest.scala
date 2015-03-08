import javax.ws.rs.BeanParam

import com.wordnik.swagger.converter.ModelConverters
import com.wordnik.swagger.util.Json
import com.wordnik.swagger.jaxrs.ext.SwaggerExtension

import com.wordnik.swagger.jaxrs.DefaultParameterExtension
import com.wordnik.swagger.jaxrs.ext.SwaggerExtensions
import com.wordnik.swagger.jersey.SwaggerJersey2Jaxrs
import com.wordnik.swagger.models.parameters.{QueryParameter, FormParameter, CookieParameter}

import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner
import org.scalatest.{FlatSpec, Matchers}

import params.{BaseBean, ChildBean, RefBean}
import scala.collection.JavaConverters._

@RunWith(classOf[JUnitRunner])
class SwaggerJersey2JaxrsTest extends FlatSpec with Matchers {

  // Here so that we can get the params with the @BeanParam annotation instantiated properly
  def testRoute(@BeanParam baseBean: BaseBean, @BeanParam childBean: ChildBean, @BeanParam refBean: RefBean,
                nonBean: Int) = {}

  /* Unit test for `SwaggerJersey2Jaxrs#shouldIgnoreClass(Class<?>)` */
  it should "return false for all types passed to shouldIgnoreClass" in {
    val ext = new SwaggerJersey2Jaxrs()

    ext.shouldIgnoreClass(classOf[BaseBean]) should be (false)
    ext.shouldIgnoreClass(classOf[ChildBean]) should be (false)
    ext.shouldIgnoreClass(classOf[RefBean]) should be (false)
  }

  /* Unit test for `SwaggerJersey2Jaxrs#processParameter(Annotation[], Class<?>, boolean)` */
  it should "return the proper Parameters based on the call to extractParameters" in {
    val ext = new SwaggerJersey2Jaxrs();
    val method = getClass.getMethod("testRoute", classOf[BaseBean], classOf[ChildBean], classOf[RefBean], classOf[Int])
    val paramAnnotations = method.getParameterAnnotations
    val paramTypes = method.getParameterTypes
    val parameters = (paramTypes,paramAnnotations).zipped
    val classesToSkip = new java.util.HashSet[Class[_]]
    val chain = SwaggerExtensions.chain()

    parameters.foreach{
      (paramType, paramAnnotations) =>
        val swaggerParams = new SwaggerJersey2Jaxrs().extractParameters(paramAnnotations, paramType, false, classesToSkip, chain)
        // Ensure proper number of parameters returned
        if (paramType == classOf[BaseBean]) {
          swaggerParams.size should be (2)
        } else if (paramType == classOf[ChildBean]) {
          swaggerParams.size should be(5)
        } else if (paramType == classOf[RefBean]) {
          swaggerParams.size should be(5)
        } else if (paramType == classOf[Int]) {
          swaggerParams.size should be(0)
        } else {
          fail("This should not happen but just in case")
        }

        // Ensure the proper parameter type and name is returned (The rest is handled by pre-existing logic)
        for (param <- swaggerParams.asScala.toList) {
          param.getName should be (param.getClass.getSimpleName.replace("eter", ""))
        }
    }
  }
}

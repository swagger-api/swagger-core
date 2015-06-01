import java.io.InputStream

import scala.collection.JavaConverters._

import org.glassfish.jersey.media.multipart.{FormDataContentDisposition, FormDataParam}
import org.junit.runner.RunWith
import org.scalatest.FlatSpec
import org.scalatest.Matchers
import org.scalatest.junit.JUnitRunner

import com.wordnik.swagger.jaxrs.ext.SwaggerExtensions
import com.wordnik.swagger.jersey.SwaggerJersey2Jaxrs
import com.wordnik.swagger.models.parameters.{FormParameter, HeaderParameter}

import java.lang.annotation.Annotation
import java.lang.reflect.Type
import javax.ws.rs.BeanParam
import models.TestEnum
import params.BaseBean
import params.ChildBean
import params.EnumBean
import params.RefBean

@RunWith(classOf[JUnitRunner])
class SwaggerJersey2JaxrsTest extends FlatSpec with Matchers {

  // Here so that we can get the params with the @BeanParam annotation instantiated properly
  def testRoute(@BeanParam baseBean: BaseBean, @BeanParam childBean: ChildBean, @BeanParam refBean: RefBean,
                @BeanParam enumBean: EnumBean, nonBean: Int) = {}

  def testFormDataParamRoute(@FormDataParam("file") uploadedInputStream: InputStream,
                             @FormDataParam("file") fileDetail:FormDataContentDisposition) = {}

  it should "not skip all types passed to extension" in {
    for (cls <- List(classOf[BaseBean], classOf[ChildBean], classOf[RefBean])) {
      val typesToSkip = new java.util.HashSet[Type]
      new SwaggerJersey2Jaxrs().extractParameters(List[Annotation]().asJava, cls, typesToSkip, SwaggerExtensions.chain())
      typesToSkip.size should be (0)
    }
  }

  it should "return the proper @BeanParam Parameters based on the call to extractParameters" in {
    val ext = new SwaggerJersey2Jaxrs();
    val method = getClass.getMethod("testRoute", classOf[BaseBean], classOf[ChildBean], classOf[RefBean], classOf[EnumBean], classOf[Int])
    val paramAnnotations = method.getParameterAnnotations
    val paramTypes = method.getGenericParameterTypes
    val parameters = (paramTypes,paramAnnotations).zipped

    parameters.foreach{
      (paramType, paramAnnotations) =>
        val typesToSkip = new java.util.HashSet[Type]
        val chain = SwaggerExtensions.chain()
        val swaggerParams = new SwaggerJersey2Jaxrs().extractParameters(paramAnnotations.toList.asJava, paramType, typesToSkip, chain)
        // Ensure proper number of parameters returned
        if (paramType == classOf[BaseBean]) {
          swaggerParams.size should be (2)
        } else if (paramType == classOf[ChildBean]) {
          swaggerParams.size should be(5)
        } else if (paramType == classOf[RefBean]) {
          swaggerParams.size should be(5)
        } else if (paramType == classOf[EnumBean]) {
          swaggerParams.size should be (1)
          val enumParam = swaggerParams.get(0).asInstanceOf[HeaderParameter]
          enumParam.getType should be ("string")
          enumParam.getEnum().asScala.toSet should be ((for (item <- TestEnum.values()) yield item.name()).toSet)
        } else if (paramType == classOf[Int]) {
          swaggerParams.size should be(0)
        } else {
          fail(s"""Parameter of type ${paramType} was not expected""")
        }

        // Ensure the proper parameter type and name is returned (The rest is handled by pre-existing logic)
        for (param <- swaggerParams.asScala.toList) {
          param.getName should be (param.getClass.getSimpleName.replace("eter", ""))
        }
    }
  }

  it should "return the proper @FormDataParam Parameters based on the call to extractParameters" in {
    val method = getClass.getMethod("testFormDataParamRoute", classOf[java.io.InputStream], classOf[FormDataContentDisposition])
    val paramAnnotations = method.getParameterAnnotations
    val paramTypes = method.getParameterTypes
    val parameters = (paramTypes,paramAnnotations).zipped
    parameters.foreach {
      (paramType, paramAnnotations) =>
        val typesToSkip = new java.util.HashSet[Type]
        val chain = SwaggerExtensions.chain()
        val swaggerParams = new SwaggerJersey2Jaxrs().extractParameters(paramAnnotations.toList.asJava, paramType, typesToSkip, chain)
        //swaggerParams.get(0) shouldBe a [FormParameter]
        if (paramType == classOf[InputStream]) {
          swaggerParams.get(0).asInstanceOf[FormParameter].getType should be("file")
        } else {
          swaggerParams.size should be(0)
        }
    }
  }

}

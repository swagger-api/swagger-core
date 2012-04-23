package controllers

import com.wordnik.swagger.core._
import com.wordnik.swagger.core.util.JsonUtil

import org.slf4j.LoggerFactory

import play.api._
import play.api.mvc._
import play.api.data._
import play.api.data.format.Formats._
import play.api.data.validation.Constraints._
import play.api.Logger
import play.modules.swagger.ApiHelpInventory

import org.codehaus.jackson.map.ObjectMapper
import org.codehaus.jackson.map.SerializationConfig

import javax.xml.bind.JAXBContext
import java.io.StringWriter

import scala.reflect.BeanProperty
import scala.collection.JavaConversions._

/**
 * This controller exposes swagger compatiable help apis.<br/>
 * The routing for the two apis supported by this controller is automatically injected by SwaggerPlugin
 *
 * @author ayush
 * @since 10/9/11 4:37 PM
 *
 */
object ApiHelpController extends SwaggerBaseApiController {
  def getResources() = Action { request =>
	implicit val requestHeader: RequestHeader = request;
    val resources = if (returnXml(request)) ApiHelpInventory.getRootHelpXml() else ApiHelpInventory.getRootHelpJson()
    returnValue(request, resources)
  }

  def getResource(path: String) = Action { request =>
	implicit val requestHeader: RequestHeader = request;
    val help = if (returnXml(request)) ApiHelpInventory.getPathHelpXml(path) else ApiHelpInventory.getPathHelpJson(path)
    returnValue(request, help)
  }
}

class SwaggerBaseApiController extends Controller {
  protected def jaxbContext(): JAXBContext = JAXBContext.newInstance(classOf[String])
  protected def returnXml(request: Request[_]) = request.path.contains(".xml")
  protected val ok = "OK"
  protected val AccessControlAllowOrigin = ("Access-Control-Allow-Origin", "*")

  protected def XmlResponse(o: Any) = {
    val xmlValue = {
      if (o.getClass.equals(classOf[String])) {
        o.asInstanceOf[String]
      } else {
        val stringWriter = new StringWriter()
        jaxbContext.createMarshaller().marshal(o, stringWriter)
        stringWriter.toString
      }
    }
    new SimpleResult[String](header = ResponseHeader(200), body = play.api.libs.iteratee.Enumerator(xmlValue)).as("application/xml")
  }

  protected def returnValue(request: Request[_], obj: Any): Result = {
    val response = if (returnXml(request)) XmlResponse(obj) else JsonResponse(obj)
    response.withHeaders(AccessControlAllowOrigin)
  }

  protected def JsonResponse(data: Any) = {
    val jsonValue: String = {
      if (data.getClass.equals(classOf[String])) {
        data.asInstanceOf[String]
      } else {
        val mapper = new ObjectMapper()
        val w = new StringWriter()

        mapper.getSerializationConfig().disable(SerializationConfig.Feature.AUTO_DETECT_IS_GETTERS)
        mapper.writeValue(w, data)

        w.toString()
      }
    }
    new SimpleResult[String](header = ResponseHeader(200), body = play.api.libs.iteratee.Enumerator(jsonValue)).as("application/json")
  }
}

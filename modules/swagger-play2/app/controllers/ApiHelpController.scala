package controllers

import play.api._
import play.api.mvc._
import play.api.data._
import format.Formats._
import play.api.data.validation.Constraints._
import javax.xml.bind.JAXBContext
import java.io.StringWriter
import reflect.BeanProperty
import org.codehaus.jackson.map.ObjectMapper
import org.codehaus.jackson.map.SerializationConfig
import com.wordnik.swagger.core._
import play.api.Logger
import scala.collection.JavaConversions._
import org.slf4j.LoggerFactory
import play.modules.swagger.ApiHelpInventory
import javax.ws.rs.Path

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
    val resources = if (returnXml(request)) ApiHelpInventory.getRootHelpXml() else ApiHelpInventory.getRootHelpJson()
    returnValue(request, resources)
  }

  def getResource(path: String) = Action { request =>
    val help = if (returnXml(request)) ApiHelpInventory.getPathHelpXml(path) else ApiHelpInventory.getPathHelpJson(path)
    returnValue(request, help)
  }
}

class SwaggerBaseApiController extends Controller {
  protected def jaxbContext(): JAXBContext = JAXBContext.newInstance(classOf[String])
  protected def returnXml(request: Request[_]) = request.path.contains(".xml")
  protected val ok = "OK"

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
    if (returnXml(request)) XmlResponse(obj) else JsonResponse(obj)
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

package controllers

import com.wordnik.swagger.core._
import com.wordnik.swagger.annotations._
import com.wordnik.swagger.core.util.JsonUtil

import org.slf4j.LoggerFactory

import play.api._
import play.api.mvc._
import play.api.data._
import play.api.data.format.Formats._
import play.api.data.validation.Constraints._
import play.api.Logger

import com.wordnik.util.perf.{ HealthSnapshot, Health, Memory }

import javax.xml.bind.JAXBContext
import java.io.StringWriter
import javax.ws.rs.{ Path, QueryParam }

import scala.reflect.BeanProperty
import scala.collection.JavaConversions._

@Api(value = "/admin", description = "Administrative operations")
object HealthController extends SwaggerBaseApiController {
  override protected def jaxbContext(): JAXBContext = JAXBContext.newInstance(classOf[Health])
  val LOGGER = LoggerFactory.getLogger(HealthController.getClass)

  @Path("/health")
  @ApiOperation(value = "Returns health report on this JVM",
    responseClass = "com.wordnik.util.perf.Health",
    httpMethod = "GET")
  def getHealth() = Action { request =>
    try {
      val health: Health = HealthSnapshot.get()
      returnValue(request, health)
    } catch {
      case e: Exception => LOGGER.error("Error occured", e); InternalServerError //Error(e.getMessage)
    }
  }

  @Path("/ping")
  @ApiOperation(value = "Pings service", responseClass = "String", httpMethod = "GET")
  def ping() = Action { request =>
    try {
      returnValue(request, ok)
    } catch {
      case e: Exception => LOGGER.error("Error occured", e); InternalServerError //Error(e.getMessage)
    }
  }
}
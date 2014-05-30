package controllers

import com.wordnik.swagger.annotations._
import com.wordnik.swagger.core.util.ScalaJsonUtil
import com.wordnik.util.perf.{ HealthSnapshot, Health }

import org.slf4j.LoggerFactory

import play.api.mvc._

import javax.ws.rs.Path


@Api(value = "/admin", description = "Administrative operations")
object HealthController extends Controller {
  val LOGGER = LoggerFactory.getLogger(HealthController.getClass)
  val AccessControlAllowOrigin = ("Access-Control-Allow-Origin", "*")

  @Path("/health")
  @ApiOperation(value = "Returns health report on this JVM",
    response = classOf[com.wordnik.util.perf.Health],
    nickname = "Health endpoint",
    httpMethod = "GET")
  def getHealth() = Action { request =>
    try {
      val health: Health = HealthSnapshot.get()

      new SimpleResult(header = ResponseHeader(200), body = play.api.libs.iteratee.Enumerator(ScalaJsonUtil.mapper.writeValueAsBytes(health))).as("application/json")
        .withHeaders(AccessControlAllowOrigin)
    } catch {
      case e: Exception => LOGGER.error("Error occurred", e); InternalServerError //Error(e.getMessage)
    }
  }

  @Path("/ping")
  @ApiOperation(value = "Pings service",
    response = classOf[String],
    nickname = "Ping endpoint",
    produces = "text/plain",
    httpMethod = "GET")
  def ping() = Action { request =>
    try {
      new SimpleResult(header = ResponseHeader(200), body = play.api.libs.iteratee.Enumerator("OK".getBytes)).as("text/plain")
        .withHeaders(AccessControlAllowOrigin)
    } catch {
      case e: Exception => LOGGER.error("Error occurred", e); InternalServerError //Error(e.getMessage)
    }
  }
}
package controllers

import play.api._
import play.api.mvc._
import play.api.data._
import play.api.data.Forms._
import play.api.data.format.Formats._
import play.api.Play.current

import org.codehaus.jackson.map.ObjectMapper
import org.codehaus.jackson.map.SerializationConfig

import value._
import api._
import com.wordnik.swagger.core.util.RestResourceUtil

import java.io.StringWriter

object BaseApiController {
  val mapper = new ObjectMapper()
  mapper.setSerializationConfig(mapper.getSerializationConfig.without(SerializationConfig.Feature.AUTO_DETECT_IS_GETTERS))
}

class BaseApiController extends Controller with RestResourceUtil {
  // APIs
  protected def JsonResponse(data: Object) = {
    val w = new StringWriter()

    BaseApiController.mapper.writeValue(w, data)

    val jsonValue: String = w.toString()
    new SimpleResult[String](header = ResponseHeader(200), body = play.api.libs.iteratee.Enumerator(jsonValue)).as("application-json")
      .withHeaders(("Access-Control-Allow-Origin", "*"))
  }

  protected def JsonResponse(data: Object, code: Int) = {
    val w = new StringWriter()

    BaseApiController.mapper.writeValue(w, data)

    val jsonValue: String = w.toString()
    new SimpleResult[String](header = ResponseHeader(code), body = play.api.libs.iteratee.Enumerator(jsonValue)).as("application-json")
      .withHeaders(("Access-Control-Allow-Origin", "*"))
  }
}

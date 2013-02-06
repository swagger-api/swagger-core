package controllers

import value._
import api._
import com.wordnik.swagger.core.util.{ JsonUtil, RestResourceUtil }

import play.api.mvc._

import java.io.StringWriter

object BaseApiController {
  val mapper = JsonUtil.getJsonMapper
}

class BaseApiController extends Controller with RestResourceUtil {
  // APIs
  protected def JsonResponse(data: Object) = {
    val w = new StringWriter()

    BaseApiController.mapper.writeValue(w, data)

    val jsonValue: String = w.toString()
    new SimpleResult[String](header = ResponseHeader(200), body = play.api.libs.iteratee.Enumerator(jsonValue)).as("application/json")
      .withHeaders(("Access-Control-Allow-Origin", "*"))
  }

  protected def JsonResponse(data: Object, code: Int) = {
    val w = new StringWriter()

    BaseApiController.mapper.writeValue(w, data)

    val jsonValue: String = w.toString()
    new SimpleResult[String](header = ResponseHeader(code), body = play.api.libs.iteratee.Enumerator(jsonValue)).as("application/json")
      .withHeaders(("Access-Control-Allow-Origin", "*"))
  }
}

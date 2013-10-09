package controllers

import util.RestResourceUtil

import value._
import api._
import com.wordnik.swagger.core.util.ScalaJsonUtil

import play.api.mvc._

import java.io.StringWriter

class BaseApiController extends Controller with RestResourceUtil {
  // APIs
  protected def JsonResponse(data: Object) = {
<<<<<<< HEAD
    val w = new StringWriter()

    BaseApiController.mapper.writeValue(w, data)

    val jsonValue: String = w.toString()
=======
    val jsonValue: String = toJsonString(data)
>>>>>>> 2abdda71405c19c69c23807ffe562e945d310299
    new SimpleResult(header = ResponseHeader(200), body = play.api.libs.iteratee.Enumerator(jsonValue.getBytes())).as("application/json")
      .withHeaders(
        ("Access-Control-Allow-Origin", "*"),
        ("Access-Control-Allow-Methods", "GET, POST, DELETE, PUT"),
        ("Access-Control-Allow-Headers", "Content-Type, api_key, Authorization"))
  }

  protected def JsonResponse(data: Object, code: Int) = {
<<<<<<< HEAD
    val w = new StringWriter()

    BaseApiController.mapper.writeValue(w, data)

    val jsonValue: String = w.toString()
=======
    val jsonValue: String = toJsonString(data)
>>>>>>> 2abdda71405c19c69c23807ffe562e945d310299
    new SimpleResult(header = ResponseHeader(code), body = play.api.libs.iteratee.Enumerator(jsonValue.getBytes())).as("application/json")
      .withHeaders(
        ("Access-Control-Allow-Origin", "*"),
        ("Access-Control-Allow-Methods", "GET, POST, DELETE, PUT"),
        ("Access-Control-Allow-Headers", "Content-Type, api_key, Authorization"))
  }

  def toJsonString(data: Any): String = {
    if (data.getClass.equals(classOf[String])) {
      data.asInstanceOf[String]
    } else {
      ScalaJsonUtil.mapper.writeValueAsString(data)
    }
  }
}

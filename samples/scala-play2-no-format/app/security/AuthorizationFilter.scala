package security

import play.modules.swagger.PlayApiReader
import com.wordnik.swagger.play.ApiAuthorizationFilter
import play.Logger
import java.io.File
import java.util.ArrayList
import java.net.URLDecoder

import play.api.mvc.RequestHeader

import scala.collection.mutable.ListBuffer
import scala.collection.JavaConversions._

class AuthorizationFilter extends ApiAuthorizationFilter {

  var isFilterInitialized: Boolean = false
  var methodSecurityAnotations: Map[String, Boolean] = Map[String, Boolean]()
  var classSecurityAnotations: Map[String, Boolean] = Map[String, Boolean]()
  var securekeyId = "special-key"
  var unsecurekeyId = "default-key"

  def authorize(apiPath: String)(implicit requestHeader: RequestHeader): Boolean = {
    Logger.debug("authorizing path " + apiPath)
    if (requestHeader != null) {
      val mName = requestHeader.method.toUpperCase;
      if (isPathSecure(mName + ":" + apiPath, false)) {
        if (apiKey == securekeyId) return true
        else return false
      }
      true
    } else {
      Logger.debug("no header to authroize path " + apiPath)
      false
    }
  }

  def authorizeResource(apiPath: String)(implicit requestHeader: RequestHeader): Boolean = {
    Logger.debug("authorizing resource " + apiPath)
    if (isPathSecure(apiPath, true)) {
      if (apiKey == securekeyId) return true
      else return false
    } else
      true
  }

  private def apiKey()(implicit requestHeader: RequestHeader): String = {
    if (requestHeader != null) {
      requestHeader.queryString.get("api_key") match {
        case Some(keySeq) => keySeq.head
        case None => null
      }
    } else null
  }

  private def isPathSecure(apiPath: String, isResource: Boolean): Boolean = {
    Logger.debug("checking security on path " + apiPath)
    if (!isFilterInitialized.booleanValue()) initialize()
    if (isResource.booleanValue()) {
      if (classSecurityAnotations.contains(apiPath)) {
        classSecurityAnotations.get(apiPath).get
      } else false
    } else {
      if (methodSecurityAnotations.contains(apiPath)) {
        methodSecurityAnotations.get(apiPath).get
      } else false
    }
  }

  private def initialize() = {
    //initialize classes (no .format here)
    classSecurityAnotations += "/user" -> false
    classSecurityAnotations += "/pet" -> false
    classSecurityAnotations += "/store" -> true

    //initialize method security
    methodSecurityAnotations += "GET:/pet" + PlayApiReader.formatString + "/{petId}" -> false
    methodSecurityAnotations += "POST:/pet" + PlayApiReader.formatString + "" -> true
    methodSecurityAnotations += "PUT:/pet" + PlayApiReader.formatString + "" -> true
    methodSecurityAnotations += "GET:/pet" + PlayApiReader.formatString + "/findByStatus" -> false
    methodSecurityAnotations += "GET:/pet" + PlayApiReader.formatString + "/findByTags" -> false
    methodSecurityAnotations += "GET:/store" + PlayApiReader.formatString + "/order/{orderId}" -> true
    methodSecurityAnotations += "DELETE:/store" + PlayApiReader.formatString + "/order/{orderId}" -> true
    methodSecurityAnotations += "POST:/store" + PlayApiReader.formatString + "/order" -> true
    methodSecurityAnotations += "POST:/user" + PlayApiReader.formatString + "" -> false
    methodSecurityAnotations += "POST:/user" + PlayApiReader.formatString + "/createWithArray" -> false
    methodSecurityAnotations += "POST:/user" + PlayApiReader.formatString + "/createWithList" -> false

    methodSecurityAnotations += "PUT:/user" + PlayApiReader.formatString + "/{username}" -> true
    methodSecurityAnotations += "DELETE:/user" + PlayApiReader.formatString + "/{username}" -> true
    methodSecurityAnotations += "GET:/user" + PlayApiReader.formatString + "/{username}" -> false
    methodSecurityAnotations += "GET:/user" + PlayApiReader.formatString + "/login" -> false
    methodSecurityAnotations += "GET:/user" + PlayApiReader.formatString + "/logout" -> false
  }

}

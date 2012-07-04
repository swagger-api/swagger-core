package security

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
	if(requestHeader != null) {
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
	requestHeader.queryString.get("api_key") match {
		case Some(keySeq) => keySeq.head
		case None => null
	}
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
    methodSecurityAnotations += "GET:/pet.{format}/{petId}" -> false
    methodSecurityAnotations += "POST:/pet.{format}" -> true
    methodSecurityAnotations += "PUT:/pet.{format}" -> true
    methodSecurityAnotations += "GET:/pet.{format}/findByStatus" -> false
    methodSecurityAnotations += "GET:/pet.{format}/findByTags" -> false
    methodSecurityAnotations += "GET:/store.{format}/order/{orderId}" -> true
    methodSecurityAnotations += "DELETE:/store.{format}/order/{orderId}" -> true
    methodSecurityAnotations += "POST:/store.{format}/order" -> true
    methodSecurityAnotations += "POST:/user.{format}" -> false
    methodSecurityAnotations += "POST:/user.{format}/createWithArray" -> false
    methodSecurityAnotations += "POST:/user.{format}/createWithList" -> false

    methodSecurityAnotations += "PUT:/user.{format}/{username}" -> true
    methodSecurityAnotations += "DELETE:/user.{format}/{username}" -> true
    methodSecurityAnotations += "GET:/user.{format}/{username}" -> false
    methodSecurityAnotations += "GET:/user.{format}/login" -> false
    methodSecurityAnotations += "GET:/user.{format}/logout" -> false
  }
    
}

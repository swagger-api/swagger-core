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
  val methodSecurityAnotations = Map(
    "/user.{format}" -> false,
    "/pet.{format}" -> false,
    "/store.{format}" -> true)

  val classSecurityAnotations = Map(
    "GET:/pet.{format}/{petId}" -> false,
    "POST:/pet.{format}" -> true,
    "PUT:/pet.{format}" -> true,
    "GET:/pet.{format}/findByStatus" -> false,
    "GET:/pet.{format}/findByTags" -> false,


    "GET:/store.{format}/order/{orderId}" -> true,
    "DELETE:/store.{format}/order/{orderId}" -> true,
    "POST:/store.{format}/order" -> true,

    "POST:/user.{format}" -> false,
    "POST:/user.{format}/createWithArray" -> false,
    "POST:/user.{format}/createWithList" -> false,
    "PUT:/user.{format}/{username}" -> true,
    "DELETE:/user.{format}/{username}" -> true,
    "GET:/user.{format}/{username}" -> false,
    "GET:/user.{format}/login" -> false,
    "GET:/user.{format}/logout" -> false)

  var securekeyId = "special-key"
  var unsecurekeyId = "default-key"

  def authorize(apiPath: String)(implicit requestHeader: RequestHeader): Boolean = {
    Logger.debug("authorizing path " + apiPath)
  	val isAuthorized = if(requestHeader != null) {
	    if (isPathSecure(requestHeader.method.toUpperCase + ":" + apiPath, false)) {
	      if (apiKey == securekeyId) 
          return true
	      else 
          return false
	    }
	    else 
        true
  	} else {
  		Logger.debug("no header to authroize path " + apiPath)
  		false
  	}

    isAuthorized
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
    if(requestHeader == null) null
  	else requestHeader.queryString.get("api_key") match {
  		case Some(keySeq) => keySeq.head
  		case None => null
  	}
  }

  private def isPathSecure(apiPath: String, isResource: Boolean): Boolean = {
    Logger.debug("checking security on path " + apiPath + ", " + isResource)
    isResource match {
      case true => methodSecurityAnotations.getOrElse(apiPath, false)
      case false => classSecurityAnotations.getOrElse(apiPath, false)
    }
  }
}

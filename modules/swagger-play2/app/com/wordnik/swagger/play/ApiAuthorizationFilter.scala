package com.wordnik.swagger.play

import play.api.mvc.RequestHeader 
import com.wordnik.swagger.core._
import play.api.Logger

trait ApiAuthorizationFilter extends AuthorizationFilter {
	def authorize(apiPath: String)(implicit requestHeader: RequestHeader): Boolean
	def authorizeResource(apiPath: String)(implicit requestHeader: RequestHeader): Boolean
}

object ApiAuthorizationFilterLocator {
  private var apiFilterLoadAttempted = false
  private var apiFilter: ApiAuthorizationFilter = null

  def clear() {
	apiFilterLoadAttempted = false
	apiFilter = null
  }

  def get(apiFilterClassName: String) = {
    if (!apiFilterLoadAttempted && null != apiFilterClassName) {
      try {
        apiFilter = SwaggerContext.loadClass(apiFilterClassName).newInstance.asInstanceOf[ApiAuthorizationFilter]
      } catch {
        case e: ClassNotFoundException => Logger.error("Unable to resolve apiFilter class " + apiFilterClassName);
        case e: ClassCastException => Logger.error("Unable to cast to apiFilter class " + apiFilterClassName);
      }

	  apiFilterLoadAttempted = true
    }	

    apiFilter
  }

}
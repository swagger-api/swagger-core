package com.wordnik.swagger.play

import play.api.mvc.RequestHeader 
import com.wordnik.swagger.core._

trait ApiAuthorizationFilter extends AuthorizationFilter {
	def authorize(apiPath: String)(implicit requestHeader: RequestHeader): Boolean
	def authorizeResource(apiPath: String)(implicit requestHeader: RequestHeader): Boolean
}
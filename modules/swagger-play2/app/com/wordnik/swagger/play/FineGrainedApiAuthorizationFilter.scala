package com.wordnik.swagger.play

import play.api.mvc.RequestHeader 
import com.wordnik.swagger.core._

trait FineGrainedApiAuthorizationFilter extends AuthorizationFilter {
	def authorizeOperation(apiPath: String, operation: DocumentationOperation)(implicit requestHeader: RequestHeader): Boolean
	def authorizeResource(apiPath: String, endpoint: DocumentationEndPoint)(implicit requestHeader: RequestHeader): Boolean
}
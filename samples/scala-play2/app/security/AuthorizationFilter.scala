package security

import com.wordnik.swagger.play.ApiAuthorizationFilter
import javax.ws.rs.core.HttpHeaders
import javax.ws.rs.core.UriInfo
import play.Logger

class AuthorizationFilter extends ApiAuthorizationFilter {
	override def authorize(apiPath: String, method: String, headers: HttpHeaders, uriInfo: UriInfo): Boolean = {
		Logger.info("Authorizing " + method + " access to " + apiPath)
		true
	}
	
    override def authorizeResource(apiPath: String, headers: HttpHeaders, uriInfo: UriInfo): Boolean = {
		Logger.info("Authorizing access resource at " + apiPath)
		true
	}
    
}

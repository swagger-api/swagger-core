package controllers

import play.api.mvc.{Action, Controller}

/**
 * Sample controller that redirects a request for /api to the Swagger UI.
 *
 * Uses https://github.com/swagger-api/swagger-ui to parse the JSON returned by /api-docs
 */
object SwaggerUiController extends Controller {


  def api = Action {
    Redirect("/assets/swagger-ui/index.html")
  }

}

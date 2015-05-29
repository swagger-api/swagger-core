package controllers

import play.api.Play
import play.api.libs.json.{JsString, JsValue, Json}
import play.api.mvc._
import play.api.mvc.Controller

import scala.concurrent.Future

object SwaggerController extends Controller {

  def getSwagger = Action {
    request =>
      Ok(views.html.swagger())
  }

}

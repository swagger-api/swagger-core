package test.testdata

import play.api.mvc.{Action, Controller}

object FlyController extends Controller {

  def list = Action {
    request =>
      Ok("test case")
  }

}

case class Fly(id: Long, name: String)


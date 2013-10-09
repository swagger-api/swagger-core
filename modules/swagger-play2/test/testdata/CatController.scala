package test.testdata

import com.wordnik.swagger.annotations._
import com.wordnik.swagger.core.util.JsonSerializer

import java.io.IOException

import play.api.mvc.{Action, Controller}
import play.Logger

import play.api.libs.json.{Json, JsError}

@Api(value = "/apitest/cats", description = "requestToken")
object CatController extends Controller {

  @ApiOperation(value = "List Cats",
    notes = "Returns all cats",
    response = classOf[Cat],
    responseContainer = "List")
  def list = Action {
    request =>
      Ok("test case")
  }

}

case class Cat(id: Long, name: String)

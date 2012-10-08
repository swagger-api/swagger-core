package controllers

import models._
import api._
import com.wordnik.swagger.core._
import com.wordnik.swagger.annotations._

import play.api._
import play.api.mvc._
import play.api.data._
import play.data.validation._
import play.api.data.Forms._
import play.api.data.format.Formats._
import play.api.Play.current
import play.api.data.format.Formats._

import javax.ws.rs._
import java.io.StringWriter

@Api(value = "/store", description = "Operations about store")
object StoreApiController extends BaseApiController {
  var storeData = new StoreData

  @Path("/order/{orderId}")
  @ApiOperation(value = "Find purchase order by ID", notes = "For valid response try integer IDs with value <= 5. " +
    "Anything above 5 or nonintegers will generate API errors", responseClass = "models.Order", httpMethod = "GET")
  @ApiErrors(Array(
    new ApiError(code = 400, reason = "Invalid ID supplied"),
    new ApiError(code = 404, reason = "Order not found")))
  def getOrderById(
    @ApiParam(value = "ID of pet that needs to be fetched", required = true)@PathParam("orderId") orderId: String) = Action { implicit request =>
    storeData.findOrderById(getLong(0, 10000, 0, orderId)) match {
      case Some(order) => JsonResponse(order)
      case _ => JsonResponse(new value.ApiResponse(404, "Order not found"), 404)
    }
  }

  @Path("/order")
  @ApiOperation(value = "Place an order for a pet", responseClass = "void", httpMethod = "POST")
  @ApiErrors(Array(
    new ApiError(code = 400, reason = "Invalid order")))
  @ApiParamsImplicit(Array(
    new ApiParamImplicit(name = "body", value = "order placed for purchasing the pet", required = true, dataType = "Order", paramType = "body")))
  def placeOrder = Action { implicit request =>
    request.body.asJson match {
      case Some(e) => {
        val order = BaseApiController.mapper.readValue(e.toString, classOf[Order]).asInstanceOf[Order]
        storeData.placeOrder(order)
        JsonResponse(order)
      }
      case None => JsonResponse(new value.ApiResponse(400, "Invalid input"))
    }
  }

  @Path("/order/{orderId}")
  @ApiOperation(value = "Delete purchase order by ID", notes = "For valid response try integer IDs with value < 1000. " +
    "Anything above 1000 or nonintegers will generate API errors", httpMethod = "DELETE")
  @ApiErrors(Array(
    new ApiError(code = 400, reason = "Invalid ID supplied"),
    new ApiError(code = 404, reason = "Order not found")))
  def deleteOrder(
    @ApiParam(value = "ID of the order that needs to be deleted", required = true)@PathParam("orderId") orderId: String) = Action {
    implicit request =>
      storeData.deleteOrder(getLong(0, 10000, 0, orderId))
      Ok
  }
}
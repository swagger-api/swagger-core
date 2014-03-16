package controllers

import models.Order
import api._
import com.wordnik.swagger.core._
import com.wordnik.swagger.annotations._
import com.wordnik.swagger.core.util.ScalaJsonUtil

import play.api._
import play.api.mvc._
import play.api.data._
import play.api.data.Forms._
import play.api.data.format.Formats._
import play.api.Play.current
import play.api.data.format.Formats._

import javax.ws.rs.{QueryParam, PathParam}
import java.io.StringWriter
import scala.collection.JavaConverters._

@Api(value = "/store", description = "Operations about store")
object StoreApiController extends BaseApiController {
  var storeData = new StoreData

  @ApiOperation(value = "Find purchase order by ID", notes = "For valid response try integer IDs with value <= 5. " +
    "Anything above 5 or nonintegers will generate API errors", response = classOf[models.Order],
    httpMethod = "GET", nickname = "getOrderById")
  @ApiResponses(value = Array(
    new ApiResponse(code = 400, message = "Invalid ID supplied"),
    new ApiResponse(code = 404, message = "Order not found")))
  def getOrderById(
    @ApiParam(value = "ID of pet that needs to be fetched", required = true)@PathParam("orderId") orderId: String) = Action { implicit request =>
    storeData.findOrderById(getLong(0, 10000, 0, orderId)) match {
      case Some(order) => JsonResponse(order)
      case _ => JsonResponse(new value.ApiResponse(404, "Order not found"), 404)
    }
  }

  @ApiOperation(value = "Gets orders in the system", response = classOf[models.Order], httpMethod = "GET", responseContainer = "List", nickname = "getOrders")
  @ApiResponses(Array(
    new ApiResponse(code = 404, message = "No Orders found")))
  def getOrders(@ApiImplicitParam(value = "Get all orders or only those which are complete", dataType = "Boolean", required = true)@QueryParam("isComplete") isComplete: Boolean) = Action { implicit request =>
    val orders: java.util.List[Order] = storeData.orders.toList.asJava
    JsonResponse(orders)
  }

  @ApiOperation(value = "Place an order for a pet", response = classOf[Void], httpMethod = "POST", nickname = "placeOrder")
  @ApiResponses(Array(
    new ApiResponse(code = 400, message = "Invalid order")))
  @ApiImplicitParams(Array(
    new ApiImplicitParam(name = "body", value = "order placed for purchasing the pet", required = true, dataType = "Order", paramType = "body")))
  def placeOrder = Action { implicit request =>
    request.body.asJson match {
      case Some(e) => {
        val order = ScalaJsonUtil.mapper.readValue(e.toString, classOf[Order]).asInstanceOf[Order]
        storeData.placeOrder(order)
        JsonResponse(order)
      }
      case None => JsonResponse(new value.ApiResponse(400, "Invalid input"))
    }
  }

  @ApiOperation(value = "Delete purchase order by ID", notes = "For valid response try integer IDs with value < 1000. " +
    "Anything above 1000 or nonintegers will generate API errors", httpMethod = "DELETE", nickname = "deleteOrder")
  @ApiResponses(Array(
    new ApiResponse(code = 400, message = "Invalid ID supplied"),
    new ApiResponse(code = 404, message = "Order not found")))
  def deleteOrder(
    @ApiParam(value = "ID of the order that needs to be deleted", required = true)@PathParam("orderId") orderId: String) = Action {
    implicit request =>
      storeData.deleteOrder(getLong(0, 10000, 0, orderId))
      Ok
  }
}
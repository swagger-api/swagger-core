package controllers

import models.Order
import api._
import com.wordnik.swagger.core._
import com.wordnik.swagger.annotations._

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

@Api(value = "/store", listingPath = "/api-docs.json/store", description = "Operations about store")
object StoreApiController extends BaseApiController {
  var storeData = new StoreData

  @ApiOperation(value = "Find purchase order by ID", notes = "For valid response try integer IDs with value <= 5. " +
    "Anything above 5 or nonintegers will generate API errors", responseClass = "models.Order", httpMethod = "GET")
  @ApiResponses(Array(
    new ApiResponse(errors = Array(new ApiError(code = 400, reason = "Invalid ID supplied"))),
    new ApiResponse(errors = Array(new ApiError(code = 404, reason = "Order not found")))))
  def getOrderById(
    @ApiParam(value = "ID of pet that needs to be fetched", required = true)@PathParam("orderId") orderId: String) = Action { implicit request =>
    storeData.findOrderById(getLong(0, 10000, 0, orderId)) match {
      case Some(order) => JsonResponse(order)
      case _ => JsonResponse(new value.ApiResponse(404, "Order not found"), 404)
    }
  }

  @ApiOperation(value = "Gets orders in the system", responseClass = "models.Order", httpMethod = "GET", multiValueResponse = true)
  @ApiResponses(Array(
    new ApiResponse(errors = Array(new ApiError(code = 404, reason = "No Orders found")))))
  def getOrders(@ApiParamImplicit(value = "Get all orders or only those which are complete", dataType = "Boolean", required = true)@QueryParam("isComplete") isComplete: Boolean) = Action { implicit request =>
    val orders: java.util.List[Order] = storeData.orders.toList.asJava
    JsonResponse(orders)
  }

  @ApiOperation(value = "Place an order for a pet", responseClass = "void", httpMethod = "POST")
  @ApiResponses(Array(
    new ApiResponse(errors = Array(new ApiError(code = 400, reason = "Invalid order")))))
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

  @ApiOperation(value = "Delete purchase order by ID", notes = "For valid response try integer IDs with value < 1000. " +
    "Anything above 1000 or nonintegers will generate API errors", httpMethod = "DELETE")
  @ApiResponses(Array(
    new ApiResponse(errors = Array(new ApiError(code = 400, reason = "Invalid ID supplied"))),
    new ApiResponse(errors = Array(new ApiError(code = 404, reason = "Order not found")))))
  def deleteOrder(
    @ApiParam(value = "ID of the order that needs to be deleted", required = true)@PathParam("orderId") orderId: String) = Action {
    implicit request =>
      storeData.deleteOrder(getLong(0, 10000, 0, orderId))
      Ok
  }
}
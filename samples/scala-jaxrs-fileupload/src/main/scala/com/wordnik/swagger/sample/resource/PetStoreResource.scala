/**
 *  Copyright 2014 Reverb Technologies, Inc.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */

package com.wordnik.swagger.sample.resource

import com.wordnik.swagger.core._
import com.wordnik.swagger.annotations._

import com.wordnik.swagger.jaxrs._
import com.wordnik.swagger.sample.model.Order
import com.wordnik.swagger.sample.data.StoreData
import com.wordnik.swagger.sample.exception.NotFoundException
import com.wordnik.swagger.sample.util.RestResourceUtil

import javax.ws.rs.core.Response
import javax.ws.rs._


@Path("/store")
@Api(value="/store" , description = "Operations about store")
@Produces(Array("application/json"))
class PetStoreResource extends RestResourceUtil {
  @GET
  @Path("/order/{orderId}")
  @ApiOperation(value = "Find purchase order by ID",
    notes = "For valid response try integer IDs with value <= 5. Anything above 5 or nonintegers will generate API errors", 
    response = classOf[Order])
  @ApiResponses(Array(
    new ApiResponse(code = 400, message = "Invalid ID supplied"),
    new ApiResponse(code = 404, message = "Order not found")))
  def getOrderById(
      @ApiParam(value="ID of pet that needs to be fetched",required=true)@PathParam("orderId") orderId: String) = {
      var order = StoreData.findOrderById(getLong(0,10000, 0, orderId))
      if (null != order){
        Response.ok.entity(order).build
      }else{
        throw new NotFoundException(404, "Order not found")
      }
  }

  @POST
  @Path("/order")
  @ApiOperation(value = "Place an order for a pet")
  @ApiResponses(Array(
    new ApiResponse(code = 400, message = "Invalid order")))
  def placeOrder(
      @ApiParam(value="order placed for purchasing the pet",required=true)order: Order) = {
      StoreData.placeOrder(order)
      Response.ok.build
  }

  @DELETE
  @Path("/order/{orderId}")
  @ApiOperation(value = "Delete purchase order by ID",
    notes = "For valid response try integer IDs with value < 1000.  Anything above 1000 or nonintegers will generate API errors")
  @ApiResponses(Array(
    new ApiResponse(code = 400, message = "Invalid ID supplied"),
    new ApiResponse(code = 404, message = "Order not found")))
  def deleteOrder(
      @ApiParam(value="ID of the order that needs to be deleted",required=true)@PathParam("orderId") orderId: String) = {
      StoreData.deleteOrder(getLong(0, 10000, 0, orderId))
      Response.ok.build
  }
}

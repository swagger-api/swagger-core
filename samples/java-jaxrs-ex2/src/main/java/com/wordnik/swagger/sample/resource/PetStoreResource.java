/**
 *  Copyright 2012 Wordnik, Inc.
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

package com.wordnik.swagger.sample.resource;

import com.wordnik.swagger.annotations.*;
import com.wordnik.swagger.sample.data.StoreData;
import com.wordnik.swagger.sample.model.Order;
import com.wordnik.swagger.sample.exception.NotFoundException;
import com.wordnik.swagger.jaxrs.JavaHelp;

import javax.ws.rs.core.Response;
import javax.ws.rs.*;

public class PetStoreResource extends JavaHelp {
	static StoreData storeData = new StoreData();
	static JavaRestResourceUtil ru = new JavaRestResourceUtil();

	@GET
	@Path("/order/{orderId}")
	@ApiOperation(value = "Find purchase order by ID", notes = "For valid response try integer IDs with value <= 5 or > 10. "
			+ "Other values will generated exceptions", responseClass = "com.wordnik.swagger.sample.model.Order")
	@ApiErrors(value = { @ApiError(code = 400, reason = "Invalid ID supplied"),
			@ApiError(code = 404, reason = "Order not found") })
	public Response getOrderById(
			@ApiParam(value = "ID of pet that needs to be fetched", allowableValues = "range[1,5]", required = true) @PathParam("orderId") String orderId)
			throws NotFoundException {
		Order order = storeData.findOrderById(ru.getLong(0, 10000, 0, orderId));
		if (null != order) {
			return Response.ok().entity(order).build();
		} else {
			throw new NotFoundException(404, "Order not found");
		}
	}

	@POST
	@Path("/order")
	@ApiOperation(value = "Place an order for a pet", responseClass = "com.wordnik.swagger.sample.model.Order")
	@ApiErrors({ @ApiError(code = 400, reason = "Invalid Order") })
	public Response placeOrder(
			@ApiParam(value = "order placed for purchasing the pet", required = true) Order order) {
		storeData.placeOrder(order);
		return Response.ok().entity("").build();
	}

	@DELETE
	@Path("/order/{orderId}")
	@ApiOperation(value = "Delete purchase order by ID", notes = "For valid response try integer IDs with value < 1000. "
			+ "Anything above 1000 or nonintegers will generate API errors")
	@ApiErrors(value = { @ApiError(code = 400, reason = "Invalid ID supplied"),
			@ApiError(code = 404, reason = "Order not found") })
	public Response deleteOrder(
			@ApiParam(value = "ID of the order that needs to be deleted", allowableValues = "range[1,infinity]", required = true) @PathParam("orderId") String orderId) {
		storeData.deleteOrder(ru.getLong(0, 10000, 0, orderId));
		return Response.ok().entity("").build();
	}
}

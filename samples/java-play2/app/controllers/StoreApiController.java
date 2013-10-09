package controllers;

import java.io.IOException;

import api.*;

import com.wordnik.swagger.annotations.ApiResponse;
import com.wordnik.swagger.core.*;
import com.wordnik.swagger.annotations.*;

import javax.ws.rs.*;

import models.*;
import exception.*;
import play.*;
import play.mvc.*;

import views.html.*;

@Api(value = "/store", description = "Operations about store")
public class StoreApiController extends BaseApiController {
<<<<<<< HEAD
	static StoreData storeData = new StoreData();

	@Path("/order/{orderId}")
	@ApiOperation(value = "Find purchase order by ID", notes = "For valid response try integer IDs with value <= 5. "
			+ "Anything above 5 or nonintegers will generate API errors", responseClass = "models.Order", httpMethod = "GET")
	@ApiErrors({ @ApiError(code = 400, reason = "Invalid ID supplied"),
			@ApiError(code = 404, reason = "Order not found") })
	public static Result getOrderById(
			@ApiParam(value = "ID of order to fetch", required = true) @PathParam("orderId") String orderId) {
		Order order = storeData.findOrderById(ru.getLong(0, 10000, 0, orderId));
		if (null != order) {
			return JsonResponse(order);
		} else {
			return JsonResponse(new models.ApiResponse(404, "Order not found"), 404);
		}
	}

	@Path("/order")
	@ApiOperation(value = "Place an order for a pet", responseClass = "void", httpMethod = "POST")
	@ApiErrors(@ApiError(code = 400, reason = "Invalid order"))
	@ApiParamsImplicit(@ApiParamImplicit(name = "body", value = "order placed for purchasing the pet", required = true, dataType = "Order", paramType = "body"))
	public static Result placeOrder() {
		Object o = request().body().asJson();
		try {
			Order order = (Order) BaseApiController.mapper.readValue(o.toString(), Order.class);
			storeData.placeOrder(order);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return JsonResponse("");
	}

	@Path("/order/{orderId}")
	@ApiOperation(value = "Delete purchase order by ID", notes = "For valid response try integer IDs with value < 1000. "
			+ "Anything above 1000 or nonintegers will generate API errors", responseClass = "void", httpMethod = "DELETE")
	@ApiErrors({ @ApiError(code = 400, reason = "Invalid ID supplied"),
			@ApiError(code = 404, reason = "Order not found") })
	public static Result deleteOrder(
			@ApiParam(value = "ID of the order that needs to be deleted", required = true) @PathParam("orderId") String orderId) {
		storeData.deleteOrder(ru.getLong(0, 10000, 0, orderId));
		return JsonResponse("");
	}
=======
    static StoreData storeData = new StoreData();

    @ApiOperation(value = "Find purchase order by ID",
            notes = "For valid response try integer IDs with value <= 5 or > 10. Other values will generated exceptions",
            response = Order.class, httpMethod = "GET")
    @ApiResponses(value = {@ApiResponse(code = 400, message = "Invalid ID supplied"),
            @ApiResponse(code = 404, message = "Order not found")})
    public static Result getOrderById(
            @ApiParam(value = "ID of order to fetch", required = true) @PathParam("orderId") String orderId) {
        Order order = storeData.findOrderById(ru.getLong(0, 10000, 0, orderId));
        if (null != order) {
            return JsonResponse(order);
        } else {
            return JsonResponse(new models.ApiResponse(404, "Order not found"), 404);
        }
    }

    @ApiOperation(value = "Place an order for a pet",
            response = Order.class, httpMethod = "POST")
    @ApiResponses({@ApiResponse(code = 400, message = "Invalid Order")})
    public static Result placeOrder() {
        Object o = request().body().asJson();
        try {
            Order order = (Order) BaseApiController.mapper.readValue(o.toString(), Order.class);
            storeData.placeOrder(order);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return JsonResponse("");
    }

    @ApiOperation(value = "Delete purchase order by ID",
            notes = "For valid response try integer IDs with value < 1000. Anything above 1000 or nonintegers will generate API errors",
            httpMethod = "DELETE")
    @ApiResponses(value = {@ApiResponse(code = 400, message = "Invalid ID supplied"),
            @ApiResponse(code = 404, message = "Order not found")})
    public static Result deleteOrder(
            @ApiParam(value = "ID of the order that needs to be deleted", required = true) @PathParam("orderId") String orderId) {
        storeData.deleteOrder(ru.getLong(0, 10000, 0, orderId));
        return JsonResponse("");
    }
>>>>>>> 2abdda71405c19c69c23807ffe562e945d310299
}
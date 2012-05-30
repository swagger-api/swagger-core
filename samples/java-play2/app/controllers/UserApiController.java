package controllers;

import api.*;

import com.wordnik.swagger.core.*;

import javax.ws.rs.*;

import models.*;
import exception.*;
import play.*;
import play.mvc.*;
import java.util.List;

import views.html.*;

@Api(value = "/user", description = "Operations about user")
public class UserApiController extends BaseApiController {
	static UserData userData = new UserData();

	@POST
	@ApiOperation(value = "Create user", notes = "This can only be done by the logged in user.")
	@ApiParamsImplicit(@ApiParamImplicit(name = "body", value = "Created user object", required = true, dataType = "User", paramType = "body"))
	public static Result createUser() {
		Object o = request().body().asJson();
		if (o != null) {
			userData.addUser((User) o);
			return JsonResponse(o);
		} else {
			return JsonResponse(new models.ApiResponse(400, "Invalid input"));
		}
	}

	@Path("/createWithArray")
	@ApiOperation(value = "Creates list of users with given input array", responseClass = "void")
	@ApiParamsImplicit(@ApiParamImplicit(name = "body", value = "List of user object", required = true, dataType = "Array[User]", paramType = "body"))
	public static Result createUsersWithArrayInput() {
		Object o = request().body().asJson();
		if (o != null) {
			List<User> users = (List<User>) o;
			for (User user : users)
				userData.addUser(user);
			return JsonResponse(users);
		} else {
			return JsonResponse(new models.ApiResponse(400, "Invalid input"));
		}
	}

	@Path("/createWithList")
	@ApiOperation(value = "Creates list of users with given list input", responseClass = "void")
	@ApiParamsImplicit(@ApiParamImplicit(name = "body", value = "List of user object", required = true, dataType = "List[User]", paramType = "body"))
	public static Result createUsersWithListInput() {
		Object o = request().body().asJson();
		if (o != null) {
			List<User> users = (List<User>) o;
			for (User user : users)
				userData.addUser(user);
			return JsonResponse(users);
		} else {
			return JsonResponse(new models.ApiResponse(400, "Invalid input"));
		}
	}

	@Path("/{username}")
	@ApiOperation(value = "Fetch a user", notes = "This can only be done by the logged in user.")
	@ApiErrors({ @ApiError(code = 400, reason = "Invalid username supplied"),
			@ApiError(code = 404, reason = "User not found") })
	@ApiParamsImplicit(@ApiParamImplicit(name = "body", value = "Updated user object", required = true, dataType = "List[User]", paramType = "body"))
	public static Result updateUser(
			@ApiParam(value = "name that need to be updated", required = true) @PathParam("username") String username) {
		Object o = request().body().asJson();
		if (o != null) {
			userData.addUser((User) o);
			return JsonResponse(o);
		} else {
			return JsonResponse(new models.ApiResponse(400, "Invalid input"));
		}
	}

	@Path("/{username}")
	@ApiOperation(value = "Delete user", notes = "This can only be done by the logged in user.")
	@ApiErrors({ @ApiError(code = 400, reason = "Invalid username supplied"),
			@ApiError(code = 404, reason = "User not found") })
	public static Result deleteUser(
			@ApiParam(value = "The name that needs to be deleted", required = true) String username) {
		userData.removeUser(username);
		return ok();
	}

	@Path("/{username}")
	@ApiOperation(value = "Get user by user name", responseClass = "models.User")
	@ApiErrors({ @ApiError(code = 400, reason = "Invalid username supplied"),
			@ApiError(code = 404, reason = "User not found") })
	public static Result getUserByName(
			@ApiParam(value = "The name that needs to be fetched. Use user1 for testing. ", required = true) String username) {
		User user = userData.findUserByName(username);
		if (user != null)
			return JsonResponse(user);
		else
			return JsonResponse(new models.ApiResponse(400, "Invalid input"));
	}

	@Path("/login")
	@ApiOperation(value = "Logs user into the system", responseClass = "String")
	@ApiErrors(@ApiError(code = 400, reason = "Invalid username and password combination"))
	public static Result loginUser(
			@ApiParam(value = "The user name for login", required = true) String username,
			@ApiParam(value = "The password for login in clear text", required = true) String password) {
		return JsonResponse(new models.ApiResponse(200,
				"logged in user session:" + System.currentTimeMillis()));
	}

	@Path("/logout")
	@ApiOperation(value = "Logs out current logged in user session")
	public static Result logoutUser() {
		return ok();
	}
}
package controllers;

import api.*;

import com.wordnik.swagger.core.*;
import com.wordnik.swagger.annotations.*;

import javax.ws.rs.*;

import exception.*;
import models.User;
import play.*;
import play.mvc.*;

import java.io.IOException;
import java.util.List;

import views.html.*;

@Api(value = "/user", description = "Operations about user")
public class UserApiController extends BaseApiController {

    static UserData userData = new UserData();

    @ApiOperation(nickname = "createUser", value = "Create user", notes = "This can only be done by the logged in user.", httpMethod = "POST")
    @ApiImplicitParams(@ApiImplicitParam(name = "body", value = "Created user object", required = true, dataType = "User", paramType = "body"))
    public static Result createUser() {
        Object o = request().body().asJson();
        try {
            User user = (User) BaseApiController.mapper.readValue(o.toString(),
                    User.class);
            userData.addUser(user);
            return JsonResponse(user);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return JsonResponse(new models.ApiResponse(400, "Invalid input"));
    }

    @ApiOperation(nickname = "createUsersWithArrayInput", value = "Creates list of users with given input array", httpMethod = "POST")
    @ApiImplicitParams(@ApiImplicitParam(name = "body", value = "List of user object", required = true, dataType = "Array[User]", paramType = "body"))
    public static Result createUsersWithArrayInput() {
        Object o = request().body().asJson();
        try {
            User[] users = BaseApiController.mapper.readValue(o.toString(),
                    User[].class);
            for (User user : users) {
                userData.addUser(user);
            }
            return JsonResponse(users);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return JsonResponse(new models.ApiResponse(400, "Invalid input"));
    }

    @ApiOperation(nickname = "createUsersWithListInput", value = "Creates list of users with given input array", httpMethod = "POST")
    @ApiImplicitParams(@ApiImplicitParam(name = "body", value = "List of user object", required = true, dataType = "List[User]", paramType = "body"))
    public static Result createUsersWithListInput() {
        Object o = request().body().asJson();
        try {
            User[] users = BaseApiController.mapper.readValue(o.toString(),
                    User[].class);
            for (User user : users) {
                userData.addUser(user);
            }
            return JsonResponse(users);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return JsonResponse(new models.ApiResponse(400, "Invalid input"));
    }

    @ApiOperation(nickname = "updateUser", value = "Updated user", notes = "This can only be done by the logged in user.",
            httpMethod = "PUT")
    @ApiResponses(value = {
            @ApiResponse(code = 400, message = "Invalid user supplied"),
            @ApiResponse(code = 404, message = "User not found")})
    @ApiImplicitParams({
            @ApiImplicitParam(name = "username", value = "name that need to be updated", required = true, dataType = "string", paramType = "path"),
            @ApiImplicitParam(name = "body", value = "Updated user object", required = true, dataType = "User", paramType = "body")})
    public static Result updateUser(String username) {
        Object o = request().body().asJson();
        try {
            User user = (User) BaseApiController.mapper.readValue(o.toString(),
                    User.class);
            userData.addUser(user);
            return JsonResponse(user);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return JsonResponse(new models.ApiResponse(400, "Invalid input"));
    }

    @ApiOperation(nickname = "deleteUser", value = "Delete user", notes = "This can only be done by the logged in user.",
            httpMethod = "DELETE")
    @ApiResponses(value = {
            @ApiResponse(code = 400, message = "Invalid username supplied"),
            @ApiResponse(code = 404, message = "User not found")})
    public static Result deleteUser(
            @ApiParam(value = "The name that needs to be deleted", required = true) String username) {
        userData.removeUser(username);
        return ok();
    }

    @ApiOperation(nickname = "getUserByName", value = "Get user by user name", response = User.class, httpMethod = "GET")
    @ApiResponses(value = {
            @ApiResponse(code = 400, message = "Invalid username supplied"),
            @ApiResponse(code = 404, message = "User not found")})
    public static Result getUserByName(
            @ApiParam(value = "The name that needs to be fetched. Use user1 for testing. ", required = true) @PathParam("username") String username) {
        User user = userData.findUserByName(username);
        if (user != null)
            return JsonResponse(user);
        else
            return JsonResponse(new models.ApiResponse(400, "Invalid input"));
    }

    @ApiOperation(nickname = "loginUser", value = "Logs user into the system", response = String.class, httpMethod = "GET")
    @ApiResponses(value = {@ApiResponse(code = 400, message = "Invalid username/password supplied")})

    public static Result loginUser(
            @ApiParam(value = "The user name for login", required = true) @QueryParam("username") String username,
            @ApiParam(value = "The password for login in clear text", required = true) @QueryParam("password") String password) {
        return JsonResponse("logged in user session:" + System.currentTimeMillis());
    }

    @GET
    @Path("/logout")
    @ApiOperation(nickname = "logoutUser", value = "Logs out current logged in user session", httpMethod = "GET")
    public static Result logoutUser() {
        return ok();
    }
}
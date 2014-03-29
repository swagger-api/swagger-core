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

package com.wordnik.swagger.sample.resource

import com.wordnik.swagger.annotations._
import com.wordnik.swagger.jaxrs._

import com.wordnik.swagger.sample.model.User
import com.wordnik.swagger.sample.data.UserData
import com.wordnik.swagger.sample.exception.NotFoundException
import com.wordnik.swagger.sample.util.RestResourceUtil

import javax.ws.rs.core.{ Response, MediaType }
import javax.ws.rs._

import scala.collection.JavaConverters._

@Path("/user")
@Api(value = "/user", description = "Operations about user")
@Produces(Array(MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML))
class UserResource extends RestResourceUtil {
  @POST
  @ApiOperation(
    value = "Create user", 
    notes = "This can only be done by the logged in user.")
  def createUser(
    @ApiParam(value = "Created user object", required = true) user: User) = {
    UserData.addUser(user)
    Response.ok.entity("").build
  }

  @POST
  @Path("/createWithArray")
  @ApiOperation(value = "Creates list of users with given input array")
  def createUsersWithArrayInput(@ApiParam(value = "List of user object", required = true) users: Array[User]): Response = {
    for (user <- users) {
      UserData.addUser(user)
    }
    Response.ok.entity("").build
  }

  @POST
  @Path("/createWithList")
  @ApiOperation(value = "Creates list of users with given list input")
  def createUsersWithListInput(@ApiParam(value = "List of user object", required = true) users: java.util.List[User]): Response = {
    for (user <- users.asScala) {
      UserData.addUser(user)
    }
    Response.ok.entity("").build
  }

  @PUT
  @Path("/{username}")
  @ApiOperation(value = "Updated user", notes = "This can only be done by the logged in user.")
  @ApiResponses(Array(
    new ApiResponse(code = 400, message = "Invalid username supplied"),
    new ApiResponse(code = 404, message = "User not found")))
  def updateUser(
    @ApiParam(value = "name that need to be deleted", required = true)@PathParam("username") username: String,
    @ApiParam(value = "Updated user object", required = true) user: User) = {
    UserData.addUser(user)
    Response.ok.entity("").build
  }

  @DELETE
  @Path("/{username}")
  @ApiOperation(value = "Delete user", notes = "This can only be done by the logged in user.")
  @ApiResponses(Array(
    new ApiResponse(code = 400, message = "Invalid username supplied"),
    new ApiResponse(code = 404, message = "User not found")))
  def deleteUser(
    @ApiParam(value = "The name that needs to be deleted", required = true)@PathParam("username") username: String) = {
    UserData.removeUser(username)
    Response.ok.entity("").build
  }

  @GET
  @Path("/{username}")
  @ApiOperation(
    value = "Get user by user name", 
    response = classOf[User],
    produces = "application/json,application/xml")
  @ApiResponses(Array(
    new ApiResponse(code = 400, message = "Invalid username supplied"),
    new ApiResponse(code = 404, message = "User not found")))
  def getUserByName(
    @ApiParam(value = "The name that needs to be fetched. Use user1 for testing. ", required = true)@PathParam("username") username: String) = {
    var user = UserData.findUserByName(username)
    if (null != user) {
      Response.ok.entity(user).build
    } else {
      throw new NotFoundException(404, "User not found")
    }
  }

  @GET
  @Path("/login")
  @ApiOperation(value = "Logs user into the system", 
    response = classOf[String],
    produces = "text/plain")
  @ApiResponses(Array(
    new ApiResponse(code = 400, message = "Invalid username and password combination")))
  def loginUser(
    @ApiParam(value = "The user name for login", required = true)@QueryParam("username") username: String,
    @ApiParam(value = "The password for login in clear text", required = true)@QueryParam("password") password: String) = {
    Response.ok.entity("logged in user session:" + System.currentTimeMillis()).build
  }

  @GET
  @Path("/logout")
  @ApiOperation(value = "Logs out current logged in user session",
    produces = "text/plain")
  def logoutUser() = {
    Response.ok.entity("").build
  }
}
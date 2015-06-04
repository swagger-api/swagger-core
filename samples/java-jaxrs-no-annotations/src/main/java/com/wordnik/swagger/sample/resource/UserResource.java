/**
 *  Copyright 2015 SmartBear Software
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

package io.swagger.sample.resource;

import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Response;

import io.swagger.sample.data.UserData;
import io.swagger.sample.exception.ApiException;
import io.swagger.sample.exception.NotFoundException;
import io.swagger.sample.model.User;

@Path("/user")
@Produces({"application/json", "application/xml"})
public class UserResource {
  static UserData userData = new UserData();

  @POST
  public Response createUser(User user) {
    userData.addUser(user);
    return Response.ok().entity("").build();
  }

  @POST
  @Path("/createWithArray")
  public Response createUsersWithArrayInput(User[] users) {
      for (User user : users) {
          userData.addUser(user);
      }
      return Response.ok().entity("").build();
  }

  @POST
  @Path("/createWithList")
  public Response createUsersWithListInput(java.util.List<User> users) {
      for (User user : users) {
          userData.addUser(user);
      }
      return Response.ok().entity("").build();
  }

  @PUT
  @Path("/{username}")
  public Response updateUser(@PathParam("username") String username, User user) {
    userData.addUser(user);
    return Response.ok().entity("").build();
  }

  @DELETE
  @Path("/{username}")
  public Response deleteUser(String username) {
    userData.removeUser(username);
    return Response.ok().entity("").build();
  }

  @GET
  @Path("/{username}")
  public Response getUserByName(@PathParam("username") String username)
    throws ApiException {
    User user = userData.findUserByName(username);
    if (null != user) {
      return Response.ok().entity(user).build();
    } else {
      throw new NotFoundException(404, "User not found");
    }
  }

  @GET
  @Path("/login")
  public Response loginUser(@QueryParam("username") String username, @QueryParam("password") String password) {
    return Response.ok()
        .entity("logged in user session:" + System.currentTimeMillis())
        .build();
  }

  @GET
  @Path("/logout")
  public Response logoutUser() {
    return Response.ok().entity("").build();
  }
}

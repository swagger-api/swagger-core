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

package com.wordnik.swagger.sample.bean;

import com.wordnik.swagger.annotations.*;
import com.wordnik.swagger.sample.data.UserData;
import com.wordnik.swagger.sample.model.User;
import com.wordnik.swagger.sample.exception.ApiException;
import com.wordnik.swagger.sample.exception.NotFoundException;
import com.wordnik.swagger.sample.resource.UserResource;

import javax.ws.rs.core.Response;
import javax.ws.rs.*;

public class UserResourceBean implements UserResource {
  static UserData userData = new UserData();

  @Override
  public Response createUser(User user) {
    userData.addUser(user);
    return Response.ok().entity("").build();
  }

  @Override
  public Response createUsersWithArrayInput(User[] users) {
      for (User user : users) {
          userData.addUser(user);
      }
      return Response.ok().entity("").build();
  }

  @Override
  public Response createUsersWithListInput(java.util.List<User> users) {
      for (User user : users) {
          userData.addUser(user);
      }
      return Response.ok().entity("").build();
  }

  @Override
  public Response updateUser(String username, User user) {
    userData.addUser(user);
    return Response.ok().entity("").build();
  }

  @Override
  public Response deleteUser(String username) {
    userData.removeUser(username);
    return Response.ok().entity("").build();
  }

  @Override
  public Response getUserByName(String username) throws ApiException {
    User user = userData.findUserByName(username);
    if (null != user) {
      return Response.ok().entity(user).build();
    } else {
      throw new NotFoundException(404, "User not found");
    }
  }

  @Override
  public Response loginUser(String username, String password) {
    return Response.ok()
        .entity("logged in user session:" + System.currentTimeMillis())
        .build();
  }

  @Override
  public Response logoutUser() {
    return Response.ok().entity("").build();
  }
}
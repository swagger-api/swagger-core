package io.swagger.resources.generics;

import io.swagger.annotations.Api;

import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Path("/api/users")
@Api(value = "/users")
@Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
public class UserApiRoute extends ApiCrudRoute<UserEntity> {

}

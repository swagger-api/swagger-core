package io.swagger.resources.generics;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.models.Response;

import javax.ws.rs.POST;
import javax.ws.rs.Path;
import java.util.List;

/**
 * Created on 4/24/17
 *
 * @author Jason Bau (jbau@wavefront.com).
 */
@Path("/api/users2")
@Api(value = "/users2")
public class UserApiRouteImpl implements CrudInterface<UserEntity> {
    protected List<UserEntity> service;

    @POST
    @ApiOperation(value = "Create")
    public Response doCreate(
            @ApiParam(value = "Create object", required = true) UserEntity entity) throws Exception {
        service.add(entity);
        return new Response();
    }
}

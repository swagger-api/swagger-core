package io.swagger.resources.generics;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.models.Response;

import java.util.List;

import javax.ws.rs.POST;

public abstract class ApiCrudRoute<T extends AbstractEntity> {
    protected List<T> service;

    @POST
    @ApiOperation(value = "Create")
    public Response doCreate(
            @ApiParam(value = "Create object", required = true) T entity) throws Exception {
        service.add(entity);
        return new Response();
    }
}

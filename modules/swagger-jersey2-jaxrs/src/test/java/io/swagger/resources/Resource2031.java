package io.swagger.resources;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

import javax.ws.rs.BeanParam;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Api
@Path("/")
public class Resource2031 {
    @GET
    @Path("paged")
    @Produces(MediaType.APPLICATION_JSON)
    @ApiOperation("Get list of the paged pickticket for datatable")
    public Response getRequestData(@BeanParam NonFieldMethodBean object) throws Exception {
        return Response.ok().build();
    }

    public static class NonFieldMethodBean{
        private Integer id;
        private String name;

        public String sayHello() {
            return "Hello";
        }
    }
}

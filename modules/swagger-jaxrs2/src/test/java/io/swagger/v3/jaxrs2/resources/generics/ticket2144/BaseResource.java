package io.swagger.v3.jaxrs2.resources.generics.ticket2144;

import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;

public  abstract class BaseResource<T extends BaseDTO> {

    @GET
    @Path("/{id}")
    public T getById(@PathParam("id") String id) { return null; }


    @GET
    @Path("/nogeneric/{id}")
    public BaseDTO getByIdNoGeneric(@PathParam("id") String id) { return null; }

    @GET
    @Path("/nogenericsamereturn/{id}")
    public BaseDTO getByIdNoGenericSameReturn(@PathParam("id") String id) { return null; }


    @POST
    @Path("/genericparam")
    public BaseDTO genericParam(T id) { return null; }

}


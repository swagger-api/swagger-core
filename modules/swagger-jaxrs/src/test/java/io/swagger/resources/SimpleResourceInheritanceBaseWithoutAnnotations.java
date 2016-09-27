package io.swagger.resources;

import io.swagger.models.Sample;

import javax.ws.rs.*;

public abstract class SimpleResourceInheritanceBaseWithoutAnnotations<R> {
    @GET
    @Path("/{id}")
    public R getTest(
            @DefaultValue("5")
            @PathParam("id") String id,
            @QueryParam("limit") Integer limit
    ) throws WebApplicationException {
        Sample out = new Sample();
        out.setName("foo");
        out.setValue("bar");
        return (R)out;
    }

}
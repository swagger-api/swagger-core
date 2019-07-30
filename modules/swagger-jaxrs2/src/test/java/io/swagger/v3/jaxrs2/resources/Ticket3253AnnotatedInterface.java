package io.swagger.v3.jaxrs2.resources;

import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.Response;

import io.swagger.v3.oas.annotations.tags.Tag;

@Path("resources")
@Tag(name = "resource")
public interface Ticket3253AnnotatedInterface<T extends Number> {
    @POST
    @Path("list")
    Response postResource(List<T> payload);
  
    @POST
    @Path("single")
    Response postResource(T payload);

    @GET
    @Path("/{petId5.1}")
    Response methodFromInterfaceTyped(@PathParam("petId5") T petId);

    @GET
    @Path("/{petId5.2}")
    Response methodFromInterfaceNotTyped(@PathParam("petId5") Number petId);

    @GET
    @Path("/{petId6}")
    Response methodFromInterface(@PathParam("petId6") T petId, String str);

    @GET
    @Path("/deprecated/{petId7}")
    @Deprecated
    Response deprecatedMethodFromInterface(@PathParam("petId7") T petId);

}

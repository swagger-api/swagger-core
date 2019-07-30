package io.swagger.v3.jaxrs2.resources;

import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.Response;

public class Ticket3253Abstract<T extends Number> {
  @GET
  @Path("/{petId1}")
  public Response overriddenMethodWithTypedParam(@PathParam("petId1") T petId) {
      String pet = "dog";
      return Response.ok().entity(pet).build();
  }

  @GET
  @Path("/{petId2}")
  public Response methodWithoutTypedParam(@PathParam("petId2") Number petId) {
      String pet = "dog";
      return Response.ok().entity(pet).build();
  }

  @GET
  @Path("/{petId3}")
  public Response overriddenMethodWithoutTypedParam(@PathParam("petId3") Long petId) {
      String pet = "dog";
      return Response.ok().entity(pet).build();
  }

  @DELETE
  @Path("/{petId1}")
  public Response deletePet(@HeaderParam("api_key") String apiKey,
      @PathParam("petId1") Long petId) {
      return Response.ok().build();
  }

  @PUT
  @Path("put")
  public String simpleMethod(String arg) {
    // TODO Auto-generated method stub
    return null;
  }
}

package com.wordnik.swagger.jaxrs.listing;

import com.wordnik.swagger.models.Swagger;

import javax.servlet.ServletContext;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/")
public class ApiListingResource {
  @Context
  ServletContext context;

  @GET
  @Produces(MediaType.APPLICATION_JSON)
  @Path("/swagger.json")
  public Response getListingJson() {
    Swagger swagger = (Swagger) context.getAttribute("swagger");
    if(swagger != null) {
      return Response.ok().entity(swagger).build();
    }
    return Response.status(401).build();
  }

  @GET
  @Produces("application/yaml")
  @Path("/swagger.yaml")
  public Response getListingYaml() {
    Swagger swagger = (Swagger) context.getAttribute("swagger");
    if(swagger != null) {
      return Response.ok().entity(swagger).build();
    }
    return Response.status(401).build();
  }
}
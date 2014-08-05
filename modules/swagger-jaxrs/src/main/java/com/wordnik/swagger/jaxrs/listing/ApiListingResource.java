package com.wordnik.swagger.jaxrs.listing;

import com.wordnik.swagger.models.Swagger;

import javax.servlet.ServletContext;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;

@Produces({"application/json", "application/yaml"})
@Path("/swagger.json")
public class ApiListingResource {
  @Context
  ServletContext context;

  @GET
  public Response getListing() {
    Swagger swagger = (Swagger) context.getAttribute("swagger");
    if(swagger != null) {
      return Response.ok().entity(swagger).build();
    }
    return Response.status(401).build();
  }
}
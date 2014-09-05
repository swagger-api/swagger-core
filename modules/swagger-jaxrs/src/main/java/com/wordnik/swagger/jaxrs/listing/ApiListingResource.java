package com.wordnik.swagger.jaxrs.listing;

import com.wordnik.swagger.util.Yaml;
import com.wordnik.swagger.util.Json;
import com.wordnik.swagger.models.Swagger;
import com.wordnik.swagger.jaxrs.config.*;
import com.wordnik.swagger.jaxrs.Reader;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.ws.rs.*;
import javax.ws.rs.core.*;

import java.util.*;

@Path("/")
public class ApiListingResource {
  static boolean initialized = false;
  @Context
  ServletContext context;

  protected synchronized void scan (Application app, ServletConfig sc) {
    DefaultJaxrsScanner scanner = (DefaultJaxrsScanner)context.getAttribute("scanner");
    if(scanner != null) {
      Set<Class<?>> classes = scanner.classesFromContext(app, sc);
      Reader reader = new Reader((Swagger)context.getAttribute("swagger"));

      Swagger swagger = reader.read(classes);
      context.setAttribute("swagger", swagger);
      WebXMLReader xmlReader = (WebXMLReader)context.getAttribute("reader");
      if(xmlReader != null) {
        xmlReader.read(swagger);
      }
    }
    initialized = true;
  }

  @GET
  @Produces(MediaType.APPLICATION_JSON)
  @Path("/swagger.json")
  public Response getListingJson(
    @Context Application app,
    @Context ServletConfig sc,
    @Context HttpHeaders headers,
    @Context UriInfo uriInfo) {
    Swagger swagger = (Swagger) context.getAttribute("swagger");
    if(!initialized) 
      scan(app, sc);
    if(swagger != null) 
      return Response.ok().entity(swagger).build();
    else
      return Response.status(404).build();
  }

  @GET
  @Produces("application/yaml")
  @Path("/swagger.yaml")
  public Response getListingYaml(
    @Context Application app,
    @Context ServletConfig sc,
    @Context HttpHeaders headers,
    @Context UriInfo uriInfo) {
    Swagger swagger = (Swagger) context.getAttribute("swagger");
    if(!initialized) 
      scan(app, sc);
    try{
      if(swagger != null) {
        String yaml = Yaml.mapper().writeValueAsString(swagger);
        String[] parts = yaml.split("\n");
        StringBuilder b = new StringBuilder();
        for(String part : parts) {
          int pos = part.indexOf("!<");
          int endPos = part.indexOf(">");
          if(pos >= 0)  // dirty hack for https://github.com/FasterXML/jackson-dataformat-yaml/issues/22
            b.append(part.replace("!<", "in: ").replace(">", ""));
          else
            b.append(part);
          b.append("\n");
        }
        return Response.ok().entity(b.toString()).type("text/plain").build();
      }
    }
    catch (Exception e) {
      e.printStackTrace();
    }
    return Response.status(404).build();
  }
}
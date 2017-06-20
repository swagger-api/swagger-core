package io.swagger.jaxrs2.listing;

import com.fasterxml.jackson.core.JsonProcessingException;
import io.swagger.oas.annotations.Operation;
import org.apache.commons.lang3.StringUtils;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Application;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

@Path("/swagger.{type:json|yaml}")
public class ApiListingResource extends BaseApiListingResource {

    @Context
    ServletContext context;

    @GET
    @Produces({MediaType.APPLICATION_JSON, "application/yaml"})
    @Operation(description = "The OpenAPI definition in either JSON or YAML")
    public Response getListing(
            @Context Application app,
            @Context ServletConfig sc,
            @Context HttpHeaders headers,
            @Context UriInfo uriInfo,
            @PathParam("type") String type) throws JsonProcessingException {
        if (StringUtils.isNotBlank(type) && type.trim().equalsIgnoreCase("yaml")) {
            return getListingYamlResponse(app, context, sc, headers, uriInfo);
        } else {
            return getListingJsonResponse(app, context, sc, headers, uriInfo);
        }
    }

}

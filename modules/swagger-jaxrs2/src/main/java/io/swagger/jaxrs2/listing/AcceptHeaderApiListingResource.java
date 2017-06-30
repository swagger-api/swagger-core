package io.swagger.jaxrs2.listing;

import com.fasterxml.jackson.core.JsonProcessingException;
import io.swagger.oas.annotations.Operation;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Application;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

/**
 * Created by rbolles on 2/15/16.
 */
@Path("/swagger")
public class AcceptHeaderApiListingResource extends BaseApiListingResource {

    @Context
    ServletContext context;

    @GET
    @Produces({MediaType.APPLICATION_JSON})
    @Operation(description = "The OpenAPI definition in JSON")
    public Response getListingJson(
            @Context Application app,
            @Context ServletConfig sc,
            @Context HttpHeaders headers,
            @Context UriInfo uriInfo) throws JsonProcessingException {
        return getListingJsonResponse(app, context, sc, headers, uriInfo);
    }

    @GET
    @Produces("application/yaml")
    @Operation(description = "The OpenAPI definition in YAML")
    public Response getListingYaml(
            @Context Application app,
            @Context ServletConfig sc,
            @Context HttpHeaders headers,
            @Context UriInfo uriInfo) {
        return getListingYamlResponse(app, context, sc, headers, uriInfo);
    }
}

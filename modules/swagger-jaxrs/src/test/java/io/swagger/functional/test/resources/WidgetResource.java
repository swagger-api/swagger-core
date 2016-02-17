package io.swagger.functional.test.resources;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import io.swagger.functional.test.model.Widget;

import javax.ws.rs.*;
import javax.ws.rs.core.Response;

/**
 * Created by rbolles on 2/16/16.
 */
@Path("/widgets")
@Api(tags="widgets", description = "Widget operations")
@Produces("application/json")
@Consumes("application/json")
public class WidgetResource {

    @Path("/{widgetId}")
    @GET
    @ApiOperation(value = "Find pet by ID",
            notes = "Returns a pet when ID <= 10.  ID > 10 or nonintegers will simulate API error conditions",
            response = Widget.class

    )
    @ApiResponses(value = {
            @ApiResponse(code=200, message="Returns widget with matching id")
    })
    public Response getWidget(@PathParam("widgetId") String widgetId) {
        return Response.ok(new Widget().setA("foo").setB("bar")).build();
    }
}

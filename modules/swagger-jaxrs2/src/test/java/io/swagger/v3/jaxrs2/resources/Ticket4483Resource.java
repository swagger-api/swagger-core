package io.swagger.v3.jaxrs2.resources;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.util.Map;

/*

 */
@Tag(
        name = "Dummy",
        description = "Dummy resource for testing setup"
)
@Path("test")
@Consumes({MediaType.APPLICATION_JSON})
@Produces({MediaType.APPLICATION_JSON})
@ApiResponses({
        @ApiResponse(responseCode = "401", description = "Authentication is required", content = @Content(array = @ArraySchema(schema = @Schema(implementation = Ticket4483Resource.LocalizedError.class))))
})
public class Ticket4483Resource {
    @GET
    @Operation(
            description = "Dummy GET"
    )
    @ApiResponse(responseCode = "200", description = "test", useReturnTypeSchema = true)
    public Map<String, Boolean> dummy() {
        Map map = new java.util.HashMap();
        map.put("success", Boolean.TRUE);
        return map;
    }



    @Path("/opresp")
    @GET
    @Operation(
            responses = {
                    @ApiResponse(responseCode = "200", description = "Dummy GET opresp", useReturnTypeSchema = true)})
    public Map<String, Boolean> dummyopresp() {
        Map map = new java.util.HashMap();
        map.put("success", Boolean.TRUE);
        return map;
    }

    @Path("/oprespnodesc")
    @GET
    @Operation(
            responses = {
                    @ApiResponse(responseCode = "200", useReturnTypeSchema = true)})
    public Map<String, Boolean> oprespnodesc() {
        Map map = new java.util.HashMap();
        map.put("success", Boolean.TRUE);
        return map;
    }

    public static class LocalizedError {
        public String code;
        public String message;
    }
}

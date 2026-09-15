package io.swagger.v3.jaxrs2.petstore.responses;

import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import java.util.List;

/**
 * Resource with an Array Response at Method Level
 */
public class MethodArrayResponseResource {

    @GET
    @Path("/arrayresponseinmethod")
    @ApiResponse(content = @Content(
            array =
            @ArraySchema(
                    arraySchema = @io.swagger.v3.oas.annotations.media.Schema(description = "Array description"),
                    schema =
                    @io.swagger.v3.oas.annotations.media.Schema(
                            description = "Item description",
                            implementation = PetInfo.class
                    ))))
    public List<PetInfo> arrayResponseWithTypeImplementation() {
        return null;
    }


    public class PetInfo {
        String name;

        public String getName() {
            return "";
        }
    }
}

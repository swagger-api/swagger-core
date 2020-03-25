package io.swagger.v3.jaxrs2.resources;

import io.swagger.v3.jaxrs2.resources.model.MultiInheritanceSubaBaseBean;
import io.swagger.v3.jaxrs2.resources.model.MultiInheritanceBaseBean;
import io.swagger.v3.jaxrs2.resources.model.MultiInheritanceSub1aBean;
import io.swagger.v3.jaxrs2.resources.model.MultiInheritanceSub1bBean;
import io.swagger.v3.jaxrs2.resources.model.MultiInheritanceSub2aBean;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;

@Consumes("application/json")
@Path("/composite")
@Produces({"application/json"})
public class CompositeBeanResource {
    @GET
    @Path("/listSub1as")
    @Produces("application/json")
    @Operation(summary = "Sub1a",
            description = "Description",
            responses = {
                    @ApiResponse(description = "All sub1a",
                            content = @Content(array = @ArraySchema(schema = @Schema(implementation = MultiInheritanceSub1aBean.class)))
                    )
            })
    public Response listSub1as() {
        return Response.ok().entity("ok").build();
    }

    @GET
    @Path("/list")
    @Produces("application/json")
    @Operation(summary = "List all available beans",
            description = "Description",
            responses = {
                    @ApiResponse(description = "All available beans",
                            content = @Content(array = @ArraySchema(schema = @Schema(implementation = MultiInheritanceBaseBean.class)))
                    )
            })
    public Response list() {
        return Response.ok().entity("ok").build();
    }

    @GET
    @Path("/listSub1bs")
    @Produces("application/json")
    @Operation(summary = "Sub1b",
            description = "Description",
            responses = {
                    @ApiResponse(description = "All sub1b",
                            content = @Content(array = @ArraySchema(schema = @Schema(implementation = MultiInheritanceSub1bBean.class)))
                    )
            })
    public Response listSub1bs() {
        return Response.ok().entity("ok").build();
    }

    @GET
    @Path("/listSubs")
    @Produces("application/json")
    @Operation(summary = "List all available sub-beans",
            description = "Description",
            responses = {
                    @ApiResponse(description = "All available sub-beans",
                            content = @Content(array = @ArraySchema(schema = @Schema(implementation = MultiInheritanceSubaBaseBean.class)))
                    )
            })
    public Response listSubs() {
        return Response.ok().entity("ok").build();
    }

    @GET
    @Path("/listSub2as")
    @Produces("application/json")
    @Operation(summary = "Sub2a",
            description = "Description",
            responses = {
                    @ApiResponse(description = "All sub2a",
                            content = @Content(array = @ArraySchema(schema = @Schema(implementation = MultiInheritanceSub2aBean.class)))
                    )
            })
    public Response listSub2as() {
        return Response.ok().entity("ok").build();
    }

    public static final String YAML = 
        "openapi: 3.0.1\n" +
        "paths:\n" +
        "  /composite/listSub1as:\n" +
        "    get:\n" +
        "      summary: Sub1a\n" +
        "      description: Description\n" +
        "      operationId: listSub1as\n" +
        "      responses:\n" +
        "        default:\n" +
        "          description: All sub1a\n" +
        "          content:\n" +
        "            application/json:\n" +
        "              schema:\n" +
        "                type: array\n" +
        "                items:\n" +
        "                  $ref: '#/components/schemas/MultiInheritanceSub1aBean'\n" +
        "  /composite/listSub1bs:\n" +
        "    get:\n" +
        "      summary: Sub1b\n" +
        "      description: Description\n" +
        "      operationId: listSub1bs\n" +
        "      responses:\n" +
        "        default:\n" +
        "          description: All sub1b\n" +
        "          content:\n" +
        "            application/json:\n" +
        "              schema:\n" +
        "                type: array\n" +
        "                items:\n" +
        "                  $ref: '#/components/schemas/MultiInheritanceSub1bBean'\n" +
        "  /composite/listSubs:\n" +
        "    get:\n" +
        "      summary: List all available sub-beans\n" +
        "      description: Description\n" +
        "      operationId: listSubs\n" +
        "      responses:\n" +
        "        default:\n" +
        "          description: All available sub-beans\n" +
        "          content:\n" +
        "            application/json:\n" +
        "              schema:\n" +
        "                type: array\n" +
        "                items:\n" +
        "                  $ref: '#/components/schemas/MultiInheritanceSubaBaseBean'\n" +
        "  /composite/listSub2as:\n" +
        "    get:\n" +
        "      summary: Sub2a\n" +
        "      description: Description\n" +
        "      operationId: listSub2as\n" +
        "      responses:\n" +
        "        default:\n" +
        "          description: All sub2a\n" +
        "          content:\n" +
        "            application/json:\n" +
        "              schema:\n" +
        "                type: array\n" +
        "                items:\n" +
        "                  $ref: '#/components/schemas/MultiInheritanceSub2aBean'\n" +
        "  /composite/list:\n" +
        "    get:\n" +
        "      summary: List all available beans\n" +
        "      description: Description\n" +
        "      operationId: list\n" +
        "      responses:\n" +
        "        default:\n" +
        "          description: All available beans\n" +
        "          content:\n" +
        "            application/json:\n" +
        "              schema:\n" +
        "                type: array\n" +
        "                items:\n" +
        "                  $ref: '#/components/schemas/MultiInheritanceBaseBean'\n" +
        "components:\n" +
        "  schemas:\n" +
        "    MultiInheritanceSub1aBean:\n" +
        "      type: object\n" +
        "      description: MultiInheritanceSub1aBean\n" +
        "      allOf:\n" +
        "      - $ref: '#/components/schemas/MultiInheritanceSubaBaseBean'\n" +
        "      - type: object\n" +        
        "        properties:\n" +
        "          c:\n" +
        "            type: integer\n" +
        "            format: int32\n" +
        "    MultiInheritanceSub1bBean:\n" +
        "      type: object\n" +
        "      description: MultiInheritanceSub1bBean\n" +
        "      allOf:\n" +
        "      - $ref: '#/components/schemas/MultiInheritanceBaseBean'\n" +
        "      - type: object\n" +        
        "        properties:\n" +
        "          e:\n" +
        "            type: integer\n" +
        "            format: int32\n" +
        "    MultiInheritanceBaseBean:\n" +
        "      type: object\n" +
        "      properties:\n" +
        "        beanType:\n" +
        "          type: string\n" +
        "        aa:\n" +
        "          type: integer\n" +
        "          format: int32\n" +
        "        ab:\n" +
        "          type: string\n" +
        "      description: MultiInheritanceBaseBean\n" +
        "      discriminator:\n" +
        "        propertyName: beanType\n" +
        "    MultiInheritanceSub2aBean:\n" +
        "      type: object\n" +
        "      description: MultiInheritanceSub2aBean\n" +
        "      allOf:\n" +
        "      - $ref: '#/components/schemas/MultiInheritanceSubaBaseBean'\n" +
        "      - type: object\n" +        
        "        properties:\n" +
        "          d:\n" +
        "            type: integer\n" +
        "            format: int32\n" +       
        "    MultiInheritanceSubaBaseBean:\n" +
        "      type: object\n" +
        "      description: MultiInheritanceSubaBaseBean\n" +
        "      discriminator:\n" +
        "        propertyName: beanType\n" +
        "      allOf:\n" +
        "      - $ref: '#/components/schemas/MultiInheritanceBaseBean'\n" +
        "      - type: object\n" +
        "        properties:\n" +
        "          a:\n" +
        "            type: integer\n" +
        "            format: int32\n" +
        "          b:\n" +
        "            type: string\n";
}

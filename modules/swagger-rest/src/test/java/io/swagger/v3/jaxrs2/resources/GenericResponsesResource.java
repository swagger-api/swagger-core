package io.swagger.v3.jaxrs2.resources;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import java.util.ArrayList;
import java.util.List;

public class GenericResponsesResource {

    @GET
    @Path("/")
    @Operation(
            operationId = "getSomethings",
            summary = "Returns a list of somethings",
            responses = {
                    @ApiResponse(content = @Content(schema = @Schema(implementation = SomethingResponse.class)))
            }
    )
    public void getResponses() {
    }

    @GET
    @Path("/overridden")
    @Operation(
            operationId = "getSomethingsOverridden",
            summary = "Returns a list of somethings",
            responses = {
                    @ApiResponse(content = @Content(schema = @Schema(implementation = SomethingDTO.class)))
            }
    )
    public void getResponsesWithOverriddenName() {
    }

    class SomethingResponse extends JsonResponse<SomethingDTO> {
    }

    public class JsonResponse<T> {
        private DataWithCursor<T> data;

        public JsonResponse() {
        }

        public JsonResponse(DataWithCursor<T> data) {
            this.data = data;
        }

        public Data<T> getData() {
            return data;
        }
    }

    public class DataWithCursor<T> extends Data<T> {

        private String previousCursor;
        private String nextCursor;

        public DataWithCursor(List<T> data, String previousCursor, String nextCursor) {
            super(data);
            this.previousCursor = previousCursor;
            this.nextCursor = nextCursor;
        }

        public DataWithCursor(List<T> data) {
            super(data);
        }

        public String getPreviousCursor() {
            return previousCursor;
        }

        public String getNextCursor() {
            return nextCursor;
        }
    }

    public class Data<T> {

        private List<T> items;

        private Data() {
        }

        public Data(List<T> items) {
            this.items = items;
        }

        public Data(T item) {
            items = new ArrayList<>();
            items.add(item);
        }

        public List<T> getItems() {
            return items;
        }
    }

    @Schema(name = "Something")
    public class SomethingDTO {
        private String id;

        public SomethingDTO(SomethingAlt somethingAlt) {
            id = somethingAlt.getId();
        }

        public String getId() {
            return id;
        }
    }

    public class SomethingAlt {
        private String id;

        public String getId() {
            return id;
        }
    }

}

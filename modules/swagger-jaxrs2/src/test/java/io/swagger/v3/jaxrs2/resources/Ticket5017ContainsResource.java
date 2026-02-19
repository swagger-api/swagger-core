package io.swagger.v3.jaxrs2.resources;

import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Schema;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import java.util.List;

@Path("/test")
public class Ticket5017ContainsResource {
    @GET
    public void myMethod(Example request) {}

    public static class Example {
        @ArraySchema(contains = @Schema(implementation = MyEnum.class, enumAsRef = true))
        private List<Object> myList;

        public List<Object> getMyList() {
            return myList;
        }

        public void setMyList(List<Object> myList) {
            this.myList = myList;
        }
    }

    public enum MyEnum {
        FOO, BAR
    }
}

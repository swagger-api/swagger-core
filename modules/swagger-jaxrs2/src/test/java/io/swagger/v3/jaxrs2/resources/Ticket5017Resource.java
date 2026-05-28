package io.swagger.v3.jaxrs2.resources;

import io.swagger.v3.oas.annotations.media.Schema;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import java.util.Map;

@Path("/test")
public class Ticket5017Resource {
    @GET
    public void myMethod(Example request) {}

    public static class Example {
        @Schema(propertyNames = MyEnum.class)
        private Map<MyEnum, String> myMap;

        public Map<MyEnum, String> getMyMap() {
            return myMap;
        }

        public void setMyMap(Map<MyEnum, String> myMap) {
            this.myMap = myMap;
        }
    }

    public enum MyEnum {
        FOO, BAR
    }
}

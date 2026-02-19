package io.swagger.v3.jaxrs2.resources;

import io.swagger.v3.oas.annotations.StringToClassMapItem;
import io.swagger.v3.oas.annotations.media.Schema;

import javax.ws.rs.GET;
import javax.ws.rs.Path;

@Path("/test")
public class Ticket5017DependentSchemasResource {
    @GET
    public void myMethod(Example request) {}

    @Schema(dependentSchemas = {
            @StringToClassMapItem(key = "myKey", value = MyEnum.class)
    })
    public static class Example {
        private String myField;

        public String getMyField() {
            return myField;
        }

        public void setMyField(String myField) {
            this.myField = myField;
        }
    }

    public enum MyEnum {
        FOO, BAR
    }
}

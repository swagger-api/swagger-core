package io.swagger.v3.jaxrs2.resources;

import io.swagger.v3.oas.annotations.media.Schema;

import javax.ws.rs.GET;
import javax.ws.rs.Path;

@Path("/test")
public class Ticket5017UnevaluatedPropertiesResource {
    @GET
    public void myMethod(Example request) {}

    public static class Example {
        @Schema(unevaluatedProperties = MyEnum.class)
        private Object myObject;

        public Object getMyObject() {
            return myObject;
        }

        public void setMyObject(Object myObject) {
            this.myObject = myObject;
        }
    }

    public enum MyEnum {
        FOO, BAR
    }
}

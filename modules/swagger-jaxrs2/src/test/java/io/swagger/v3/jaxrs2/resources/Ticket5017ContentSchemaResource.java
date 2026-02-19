package io.swagger.v3.jaxrs2.resources;

import io.swagger.v3.oas.annotations.media.Schema;

import javax.ws.rs.GET;
import javax.ws.rs.Path;

@Path("/test")
public class Ticket5017ContentSchemaResource {
    @GET
    public void myMethod(Example request) {}

    public static class Example {
        @Schema(contentSchema = MyEnum.class)
        private String myContent;

        public String getMyContent() {
            return myContent;
        }

        public void setMyContent(String myContent) {
            this.myContent = myContent;
        }
    }

    public enum MyEnum {
        FOO, BAR
    }
}

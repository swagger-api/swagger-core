package io.swagger.v3.jaxrs2.resources;

import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import java.util.List;

@Path("/test")
public class Ticket4446Resource {
    @Path("test")
    @GET
    public MyPojo getCart() {
        return null;
    }

    public static class MyPojo {
        public List<String> someStrings;
        public List<MyPojo> morePojos;
    }
}

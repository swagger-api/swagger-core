package io.swagger.v3.jaxrs2.resources;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.QueryParam;

public class SimpleMethods {

    @GET
    @Path("/object")
    public TestBean getTestBean() {
        return new TestBean();
    }

    @GET
    @Path("/int")
    public int getInt() {
        return 0;
    }

    @GET
    @Path("/intArray")
    public int[] getIntArray() {
        return new int[]{0};
    }

    @GET
    @Path("/string")
    public String[] getStringArray() {
        return new String[]{};
    }

    @GET
    @Path("/stringArray")
    public void getWithIntArrayInput(@QueryParam("ids") int[] inputs) {
    }

    static class TestBean {
        public String foo;
        public TestChild testChild;
    }

    static class TestChild {
        public String foo;
    }
}
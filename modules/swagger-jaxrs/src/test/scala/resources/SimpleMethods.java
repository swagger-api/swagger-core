package resources;

import com.wordnik.swagger.annotations.ApiParam;

import javax.ws.rs.*;

public class SimpleMethods {
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
  public void getWithIntArrayInput(@QueryParam("ids") int[] inputs) {}
}
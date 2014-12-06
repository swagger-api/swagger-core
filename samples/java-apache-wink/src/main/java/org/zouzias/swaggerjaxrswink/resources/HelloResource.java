package org.zouzias.swaggerjaxrswink.resources;

import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiOperation;
import com.wordnik.swagger.annotations.ApiResponse;
import com.wordnik.swagger.annotations.ApiResponses;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import org.json.simple.JSONObject;

@Path("/hello")
@Api(value = "/hello", description = "Say Hello!")
public class HelloResource {

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @ApiOperation(value = "Say Hello World",
            notes = "Anything Else?")
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "OK"),
        @ApiResponse(code = 500, message = "Something wrong in Server")})
    public String sayHello() {
        JSONObject json = new JSONObject();
        json.put("firstName", "Anastasios");
        json.put("lastName", "Zouzias");
        json.put("message", "Hello World!");

        return json.toJSONString();
    }
}

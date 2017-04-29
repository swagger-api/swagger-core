package sample.swagger.nowebxml.resource;

import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiOperation;
import com.wordnik.swagger.annotations.ApiParam;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.util.Arrays;
import java.util.List;

@Path("/uppercase")
@Api(value = "/uppercase", description = "Sample Uppercase RESTful service")
public class UppercaseResource
{
   @GET
   @Path("/")
   @Produces(MediaType.APPLICATION_JSON)
   @ApiOperation(value = "Get static uppercase messages")
   public List<String> getUppercaseMessages ()
   {
      return Arrays.asList( "UPPERCASE", "MESSAGES" );
   }

   @GET
   @Path("/{message}")
   @Produces(MediaType.APPLICATION_JSON)
   @ApiOperation(value = "Get uppercase message")
   public String getUppercaseMessage (
      @ApiParam(required = true, name = "message")
      @PathParam("message") String message )
   {
      return message.toUpperCase();
   }
}

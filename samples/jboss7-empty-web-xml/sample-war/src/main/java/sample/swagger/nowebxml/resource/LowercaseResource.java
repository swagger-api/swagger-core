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

@Path("/lowercase")
@Api(value = "/lowercase", description = "Sample Lowercase RESTful service")
public class LowercaseResource
{
   @GET
   @Path("/")
   @Produces(MediaType.APPLICATION_JSON)
   @ApiOperation(value = "Get static lowercase messages")
   public List<String> getLowercaseMessages ()
   {
      return Arrays.asList( "lowercase", "messages" );
   }

   @GET
   @Path("/{message}")
   @Produces(MediaType.APPLICATION_JSON)
   @ApiOperation(value = "Get lowercase message")
   public String getLowercaseMessage (
      @ApiParam(required = true, name = "message")
      @PathParam("message") String message )
   {
      return message.toLowerCase();
   }
}

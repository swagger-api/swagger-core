package io.swagger.v3.jaxrs2.resources;

import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;

@Path("/pet")
@Produces({"application/json", "application/xml"})
public class Misc31Resource {
  @PUT
  public ModelWithOAS31Stuff updatePet(){ return null;}

}

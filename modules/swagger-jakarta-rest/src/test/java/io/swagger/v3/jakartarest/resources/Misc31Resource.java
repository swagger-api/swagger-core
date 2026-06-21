package io.swagger.v3.jakartarest.resources;

import jakarta.ws.rs.PUT;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;

@Path("/pet")
@Produces({"application/json", "application/xml"})
public class Misc31Resource {
  @PUT
  public ModelWithOAS31Stuff updatePet(){ return null;}

}

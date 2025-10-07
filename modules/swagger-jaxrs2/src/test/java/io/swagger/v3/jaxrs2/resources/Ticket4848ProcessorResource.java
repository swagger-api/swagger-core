package io.swagger.v3.jaxrs2.resources;

import io.swagger.v3.jaxrs2.resources.jspecify.Pet;
import io.swagger.v3.jaxrs2.resources.jspecify.nullmarked.Person;

import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;

@Path("/test")
public class Ticket4848ProcessorResource {

    @PUT
    @Path("/updateperson")
    public void putPerson(Person cart) {}

    @POST
    @Path("/createpet")
    public void postPet(Pet cart) {}

}

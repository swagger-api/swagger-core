package io.swagger.v3.jaxrs2.resources.rs;

import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

@Path("token")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class ProcessTokenRestService extends AbstractEntityRestService<ProcessTokenDTO> {


}

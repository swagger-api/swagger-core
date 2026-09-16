package io.swagger.v3.jaxrs2.resources.rs;

import javax.ws.rs.POST;
import javax.ws.rs.Path;

public interface EntityRestService<DTO> {

    @POST
    @Path("/")
    public DTO create(DTO object) throws Exception;


}

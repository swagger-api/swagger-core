package io.swagger.v3.jaxrs2.resources.rs;

import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;

public interface EntityRestService<DTO> {

    @POST
    @Path("/")
    public DTO create(DTO object) throws Exception;


}

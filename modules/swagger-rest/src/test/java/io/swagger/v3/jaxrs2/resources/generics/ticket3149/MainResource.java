package io.swagger.v3.jaxrs2.resources.generics.ticket3149;

import io.swagger.v3.oas.annotations.tags.Tag;

import javax.ws.rs.Path;

@Path("/test")
@Tag(name = "Test inheritance on default implementation in interfaces")
public class MainResource
    implements AggregateEndpoint<SampleDTO, SampleOtherDTO> {
}

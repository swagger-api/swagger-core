package io.swagger.v3.jaxrs2.resources;

import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;

import jakarta.ws.rs.BeanParam;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;


@Path("/upload")
public class UploadResource {

	@POST
	@Consumes(MediaType.MULTIPART_FORM_DATA)
	@Produces(MediaType.APPLICATION_JSON)
	public Response uploadWithBean(@BeanParam UploadRequest personData) {
		return Response.ok().build();
	}

	@Path("/requestbody")
	@POST
	@Consumes(MediaType.MULTIPART_FORM_DATA)
	@Produces(MediaType.APPLICATION_JSON)
	@RequestBody(content = @Content(schema = @Schema(implementation = UploadRequest.class)))
	public Response uploadWithBeanAndRequestBody(@BeanParam UploadRequest personData) {
		return Response.ok().build();
	}
}

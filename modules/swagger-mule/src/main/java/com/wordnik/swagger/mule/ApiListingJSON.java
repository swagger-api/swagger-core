package com.wordnik.swagger.mule;

import java.util.Set;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Application;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.wordnik.swagger.config.Scanner;
import com.wordnik.swagger.config.ScannerFactory;
import com.wordnik.swagger.jaxrs.Reader;
import com.wordnik.swagger.jaxrs.listing.SwaggerSerializers;
import com.wordnik.swagger.models.Swagger;
import com.wordnik.swagger.util.Json;

@Path ("/")
public class ApiListingJSON {
	private static final Logger	LOGGER		= LoggerFactory.getLogger(ApiListingJSON.class);
	static boolean				initialized	= false;
	static Swagger				swagger;

	public static void init(Swagger swagger) {
		ApiListingJSON.swagger = swagger;
	}

	protected synchronized void scan(Application app) {
		Scanner scanner = ScannerFactory.getScanner();
		LOGGER.debug("using scanner " + scanner);
		if (scanner != null) {
			SwaggerSerializers.setPrettyPrint(scanner.getPrettyPrint());
			Set<Class<?>> classes = null;
			classes = scanner.classes();
			if (classes != null) {
				Reader reader = new Reader(swagger);
				swagger = reader.read(classes);
				// This loads the Swagger root information from the mule-swagger-integration.xml instead of
				// the Bootstrap initializer. Should I leave both ways in, or support only one ? The new API doesn't
				// seem work very well in the bean xml syntax
				
				// swagger = ((SwaggerConfig) scanner).configure(swagger);
			}
		}
		initialized = true;
	}

	@GET
	@Produces (MediaType.APPLICATION_JSON)
	@Path ("/swagger.json")
	public Response getListingJson(@Context Application app, @Context HttpHeaders headers, @Context UriInfo uriInfo) {
		// On first use scan the API and initialize Swagger
		if (!initialized)
			scan(app);
		
		if (swagger != null) {
			try {
				return Response.ok().entity(Json.mapper().writeValueAsString(swagger)).build();
			} catch (JsonProcessingException e) {
				e.printStackTrace();
				return Response.status(405).build();
			}
		} else return Response.status(404).build(); // This was a 404 in the example, but it seems more like a 405
	}
}

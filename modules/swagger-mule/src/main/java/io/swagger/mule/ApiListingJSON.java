package io.swagger.mule;

import com.fasterxml.jackson.core.JsonProcessingException;
import io.swagger.config.Scanner;
import io.swagger.config.ScannerFactory;
import io.swagger.config.SwaggerConfig;
import io.swagger.jaxrs.Reader;
import io.swagger.jaxrs.listing.SwaggerSerializers;
import io.swagger.models.Swagger;
import io.swagger.util.Json;
import io.swagger.util.Yaml;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Application;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import java.util.Set;

@Path("/")
public class ApiListingJSON {
    private static final Logger LOGGER = LoggerFactory.getLogger(ApiListingJSON.class);
    static boolean initialized = false;
    static Swagger swagger;

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
                if (scanner instanceof SwaggerConfig) {
                    swagger = ((SwaggerConfig) scanner).configure(swagger);
                }
            }
        }
        initialized = true;
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/swagger.json")
    public Response getListingJson(@Context Application app, @Context HttpHeaders headers, @Context UriInfo uriInfo) {
        // On first use scan the API and initialize Swagger
        if (!initialized) {
            scan(app);
        }

        if (swagger != null) {
            try {
                return Response.ok().entity(Json.mapper().writeValueAsString(swagger)).build();
            } catch (JsonProcessingException e) {
                // This should probably be logged in some project specific way but I couldn't find a standard way it's done
                e.printStackTrace();
                return Response.status(405).build();
            }
        } else {
            return Response.status(404).build(); // This was a 404 in the example, but it seems more like a 405
        }
    }

    @GET
    @Produces("application/yaml")
    @Path("/swagger.yaml")
    public Response getListingYaml(@Context Application app, @Context HttpHeaders headers, @Context UriInfo uriInfo) {
        // On first use scan the API and initialize Swagger
        if (!initialized) {
            scan(app);
        }

        if (swagger != null) {
            try {
                String yaml = Yaml.mapper().writeValueAsString(swagger);
                return Response.ok().entity(yaml).type("text/plain").build();
            } catch (JsonProcessingException e) {
                // This should probably be logged in some project specific way but I couldn't find a standard way it's done
                e.printStackTrace();
                return Response.status(405).build();
            }
        } else {
            return Response.status(404).build(); // This was a 404 in the example, but it seems more like a 405
        }
    }
}

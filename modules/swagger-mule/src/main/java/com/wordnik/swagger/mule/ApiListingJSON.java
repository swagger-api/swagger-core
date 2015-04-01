package com.wordnik.swagger.mule;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.ServletContext;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Application;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Cookie;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.sun.jersey.spi.inject.Inject;
import com.wordnik.swagger.config.FilterFactory;
import com.wordnik.swagger.config.Scanner;
import com.wordnik.swagger.config.ScannerFactory;
import com.wordnik.swagger.config.SwaggerConfig;
import com.wordnik.swagger.core.filter.SpecFilter;
import com.wordnik.swagger.core.filter.SwaggerSpecFilter;
import com.wordnik.swagger.jaxrs.Reader;
import com.wordnik.swagger.jaxrs.config.ReflectiveJaxrsScanner;
import com.wordnik.swagger.jaxrs.listing.SwaggerSerializers;
import com.wordnik.swagger.models.Contact;
import com.wordnik.swagger.models.ExternalDocs;
import com.wordnik.swagger.models.Info;
import com.wordnik.swagger.models.License;
import com.wordnik.swagger.models.Swagger;
import com.wordnik.swagger.models.Tag;
import com.wordnik.swagger.models.auth.ApiKeyAuthDefinition;
import com.wordnik.swagger.models.auth.In;
import com.wordnik.swagger.models.auth.OAuth2Definition;
import com.wordnik.swagger.util.Json;

@Path ("/")
public class ApiListingJSON {
	private static final Logger	LOGGER		= LoggerFactory.getLogger(ApiListingJSON.class);
	static boolean				initialized	= false;

	protected synchronized void scan(Application app) {
		Scanner scanner = ScannerFactory.getScanner();
		LOGGER.debug("using scanner " + scanner);
		if (scanner != null) {
			SwaggerSerializers.setPrettyPrint(scanner.getPrettyPrint());
			Set<Class<?>> classes = null;
			classes = scanner.classes();
			if (classes != null) {
				System.out.println("Debug");
				if (swagger != null)
				for (Tag tag : swagger.getTags()) {
					System.out.println("#####" + tag.getName() + ":" + tag.getDescription() + ":" + tag.getClass());
				}
				Info info = new Info()
			      .title("Swagger Sample App")
			      .description("This is a sample server Petstore server.  You can find out more about Swagger " + 
			        "at <a href=\"http://swagger.io\">http://swagger.io</a> or on irc.freenode.net, #swagger.  For this sample, " + 
			        "you can use the api key \"special-key\" to test the authorization filters")
			      .termsOfService("http://helloreverb.com/terms/")
			      .contact(new Contact()
			        .email("apiteam@swagger.io"))
			      .license(new License()
			        .name("Apache 2.0")
			        .url("http://www.apache.org/licenses/LICENSE-2.0.html"));

			    swagger = new Swagger().info(info);
			    swagger.securityDefinition("api_key", new ApiKeyAuthDefinition("api_key", In.HEADER));
			    swagger.securityDefinition("petstore_auth", 
			      new OAuth2Definition()
			        .implicit("http://petstore.swagger.io/api/oauth/dialog")
			        .scope("read:pets", "read your pets")
			        .scope("write:pets", "modify pets in your account"));
			    swagger.tag(new Tag()
			      .name("pet")
			      .description("Everything about your Pets")
			      .externalDocs(new ExternalDocs("Find out more", "http://swagger.io")));
			    swagger.tag(new Tag()
			      .name("store")
			      .description("Operations about user"));
			    swagger.tag(new Tag()
			      .name("user")
			      .description("Access to Petstore orders")
			      .externalDocs(new ExternalDocs("Find out more about our store", "http://swagger.io")));
				Reader reader = new Reader(swagger);
				swagger = reader.read(classes);
				System.out.println("Debug");
				for (Tag tag : swagger.getTags()) {
					System.out.println("#####" + tag.getName() + ":" + tag.getDescription() + ":" + tag.getClass());
				}
				swagger = ((SwaggerConfig) scanner).configure(swagger);
				System.out.println("Debug");
				for (Tag tag : swagger.getTags()) {
					System.out.println("#####" + tag.getName() + ":" + tag.getDescription() + ":" + tag.getClass());
				}
			}
		}
		initialized = true;
	}

	Swagger	swagger;

	@GET
	@Produces (MediaType.APPLICATION_JSON)
	@Path ("/swagger.json")
	public Response getListingJson(@Context Application app, @Context HttpHeaders headers, @Context UriInfo uriInfo) {
		if (!initialized)
			scan(app);
		if (swagger != null) {
			SwaggerSpecFilter filterImpl = FilterFactory.getFilter();
			if (filterImpl != null) {
				SpecFilter f = new SpecFilter();
				swagger = f.filter(swagger, filterImpl, getQueryParams(uriInfo.getQueryParameters()),
						getCookies(headers), getHeaders(headers));
			}
			System.out.println("###" + swagger.getHost() + " *** " + swagger.getBasePath());
			try {
				return Response.ok().entity(Json.mapper().writeValueAsString(swagger)).build();
			} catch (JsonProcessingException e) {
				e.printStackTrace();
				return Response.status(405).build();
			}
		} else return Response.status(404).build();
	}

	protected Map<String, List<String>> getQueryParams(MultivaluedMap<String, String> params) {
		Map<String, List<String>> output = new HashMap<String, List<String>>();
		if (params != null) {
			for (String key : params.keySet()) {
				List<String> values = params.get(key);
				output.put(key, values);
			}
		}
		return output;
	}

	protected Map<String, String> getCookies(HttpHeaders headers) {
		Map<String, String> output = new HashMap<String, String>();
		if (headers != null) {
			for (String key : headers.getCookies().keySet()) {
				Cookie cookie = headers.getCookies().get(key);
				output.put(key, cookie.getValue());
			}
		}
		return output;
	}

	protected Map<String, List<String>> getHeaders(HttpHeaders headers) {
		Map<String, List<String>> output = new HashMap<String, List<String>>();
		if (headers != null) {
			for (String key : headers.getRequestHeaders().keySet()) {
				List<String> values = headers.getRequestHeaders().get(key);
				output.put(key, values);
			}
		}
		return output;
	}
}

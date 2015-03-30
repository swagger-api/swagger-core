package com.wordnik.swagger.mule;

import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.RequestDispatcher;
import javax.servlet.Servlet;
import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
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

import com.wordnik.swagger.config.FilterFactory;
import com.wordnik.swagger.config.Scanner;
import com.wordnik.swagger.config.ScannerFactory;
import com.wordnik.swagger.config.SwaggerConfig;
import com.wordnik.swagger.core.filter.SpecFilter;
import com.wordnik.swagger.core.filter.SwaggerSpecFilter;
import com.wordnik.swagger.jaxrs.Reader;
import com.wordnik.swagger.jaxrs.config.JaxrsScanner;
import com.wordnik.swagger.jaxrs.listing.SwaggerSerializers;
import com.wordnik.swagger.models.Swagger;

@Path ("/")
public class ApiListingJSON {
	private static final Logger	LOGGER		= LoggerFactory.getLogger(ApiListingJSON.class);
	static boolean				initialized	= false;

	protected synchronized Swagger scan(Application app, ServletConfig sc) {
		Swagger swagger = null;
		Scanner scanner = ScannerFactory.getScanner();
		LOGGER.debug("using scanner " + scanner);
		if (scanner != null) {
			SwaggerSerializers.setPrettyPrint(scanner.getPrettyPrint());
			swagger = swag;
			Set<Class<?>> classes = null;
			if (scanner instanceof JaxrsScanner) {
				JaxrsScanner jaxrsScanner = (JaxrsScanner) scanner;
				classes = jaxrsScanner.classesFromContext(app, sc);
			} else {
				classes = scanner.classes();
			}
			if (classes != null) {
				Reader reader = new Reader(swagger);
				swagger = reader.read(classes);
				if (scanner instanceof SwaggerConfig)
					swagger = ((SwaggerConfig) scanner).configure(swagger);
				else {
				}
				swag = swagger;
			}
		}
		initialized = true;
		return swagger;
	}

	Swagger swag;
	
	@GET
	@Produces (MediaType.APPLICATION_JSON)
	@Path ("/swagger.json")
	public Response getListingJson(@Context Application app, @Context HttpHeaders headers, @Context UriInfo uriInfo) {
		Swagger swagger = swag;
		if (!initialized)
			swagger = scan(app, null);
		if (swagger != null) {
			SwaggerSpecFilter filterImpl = FilterFactory.getFilter();
			if (filterImpl != null) {
				SpecFilter f = new SpecFilter();
				swagger = f.filter(swagger, filterImpl, getQueryParams(uriInfo.getQueryParameters()),
						getCookies(headers), getHeaders(headers));
			}
			return Response.ok().entity(swagger).build();
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

package io.swagger.jaxrs.listing;

import io.swagger.annotations.ApiOperation;
import io.swagger.config.FilterFactory;
import io.swagger.config.Scanner;
import io.swagger.config.ScannerFactory;
import io.swagger.config.SwaggerConfig;
import io.swagger.core.filter.SpecFilter;
import io.swagger.core.filter.SwaggerSpecFilter;
import io.swagger.jaxrs.Reader;
import io.swagger.jaxrs.config.JaxrsScanner;
import io.swagger.jaxrs.config.ReaderConfigUtils;
import io.swagger.models.Swagger;
import io.swagger.util.PathUtils;
import io.swagger.util.Yaml;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Application;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Cookie;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

@Path("/")
public class ApiListingResource {
    static final Map<String, Boolean> INITIALIZED_MAP = new ConcurrentHashMap<String, Boolean>();
    Logger LOGGER = LoggerFactory.getLogger(ApiListingResource.class);
    @Context
    ServletContext context;

    protected synchronized Swagger scan(Application app, ServletConfig sc, PathUtils.HostAndPath hostAndPath) {
        Swagger swagger = null;
        Scanner scanner = ScannerFactory.getScanner(hostAndPath.getHost(), hostAndPath.getBasePath());
        LOGGER.debug("using scanner " + scanner);

        if (scanner != null) {
            SwaggerSerializers.setPrettyPrint(scanner.getPrettyPrint());
            swagger = getSwagger(hostAndPath);

            Set<Class<?>> classes = new HashSet<Class<?>>();
            if (scanner instanceof JaxrsScanner) {
                JaxrsScanner jaxrsScanner = (JaxrsScanner) scanner;
                classes = jaxrsScanner.classesFromContext(app, sc);
            } else {
                classes = scanner.classes();
            }
            if (classes != null) {
                Reader reader = new Reader(swagger, ReaderConfigUtils.getReaderConfig(context));
                swagger = reader.read(classes);
                if (scanner instanceof SwaggerConfig) {
                    swagger = ((SwaggerConfig) scanner).configure(swagger);
                } else {
                    SwaggerConfig configurator = getReader(hostAndPath);
                    if (configurator != null) {
                        LOGGER.debug("configuring swagger with " + configurator);
                        configurator.configure(swagger);
                    } else {
                        LOGGER.debug("no configurator");
                    }
                }
                updateSwagger(hostAndPath, swagger);
            }
            setInitialized(hostAndPath.getHost(), hostAndPath.getBasePath(), true);
        }

        return swagger;
    }

    private synchronized void setInitialized(String host, String basePath, boolean initialized) {
        INITIALIZED_MAP.put(host + basePath, initialized);
    }

    private Boolean isInitialized(String host, String basePath) {
        final Boolean result = INITIALIZED_MAP.get(host + basePath);
        return result != null? result: false;
    }

    private SwaggerConfig getReader(PathUtils.HostAndPath hostAndPath) {
        @SuppressWarnings("unchecked")
        Map<String, SwaggerConfig> readers = (Map<String, SwaggerConfig>) context.getAttribute("readers");
        if (readers != null) {
            SwaggerConfig reader = readers.get(hostAndPath.getHost() + hostAndPath.getBasePath());
            if (reader != null) {
                return reader;
            }
            throw new IllegalStateException("Can't find reader object with host:" + hostAndPath.getHost() + " and path:"
                    + hostAndPath.getBasePath());
        }
        return (SwaggerConfig) context.getAttribute("reader");
    }

    private Swagger getSwagger(PathUtils.HostAndPath hostAndPath) {
        @SuppressWarnings("unchecked")
        Map<String, Swagger> swaggers = (Map<String, Swagger>) context.getAttribute("swaggers");
        if (swaggers != null) {
            Swagger swagger = swaggers.get(hostAndPath.getHost() + hostAndPath.getBasePath());
            if (swagger != null) {
                return swagger;
            }
            throw new IllegalStateException("Can't find swagger object with host:" + hostAndPath.getHost() + " and path:"
                    + hostAndPath.getBasePath());
        }
        return (Swagger) context.getAttribute("swagger");
    }

    private synchronized void updateSwagger(PathUtils.HostAndPath hostAndPath, Swagger swagger) {
        @SuppressWarnings("unchecked")
        Map<String, Swagger> swaggers = (Map<String, Swagger>) context.getAttribute("swaggers");
        if (swaggers == null) {
            swaggers = new HashMap<String, Swagger>();
            context.setAttribute("swaggers", swaggers);
        }
        swaggers.put(hostAndPath.getHost() + hostAndPath.getBasePath(), swagger);
    }

    private Swagger process(
            Application app,
            ServletConfig sc,
            HttpHeaders headers,
            UriInfo uriInfo) {
        PathUtils.HostAndPath hostAndPath = PathUtils.parseSwaggerPath(uriInfo.getAbsolutePath().toString());
        Swagger swagger = getSwagger(hostAndPath);
        if (!isInitialized(hostAndPath.getHost(), hostAndPath.getBasePath())) {
            swagger = scan(app, sc, hostAndPath);
        }
        if (swagger != null) {
            SwaggerSpecFilter filterImpl = FilterFactory.getFilter(swagger.getHost(), swagger.getBasePath());
            if (filterImpl != null) {
                SpecFilter f = new SpecFilter();
                swagger = f.filter(swagger, filterImpl, getQueryParams(uriInfo.getQueryParameters()), getCookies(headers),
                        getHeaders(headers));
            }
        }
        return swagger;
    }

    @GET
    @Produces({MediaType.APPLICATION_JSON, "application/yaml"})
    @ApiOperation(value = "The swagger definition in either JSON or YAML", hidden = true)
    @Path("/swagger.{type:json|yaml}")
    public Response getListing(
            @Context Application app,
            @Context ServletConfig sc,
            @Context HttpHeaders headers,
            @Context UriInfo uriInfo,
            @PathParam("type") String type) {
        if (StringUtils.isNotBlank(type) && type.trim().equalsIgnoreCase("yaml")) {
            return getListingYaml(app, sc, headers, uriInfo);
        } else {
            return getListingJson(app, sc, headers, uriInfo);
        }
    }

    @GET
    @Produces({MediaType.APPLICATION_JSON})
    @Path("/swagger")
    @ApiOperation(value = "The swagger definition in JSON", hidden = true)
    public Response getListingJson(
            @Context Application app,
            @Context ServletConfig sc,
            @Context HttpHeaders headers,
            @Context UriInfo uriInfo) {
        Swagger swagger = process(app, sc, headers, uriInfo);

        if (swagger != null) {
            return Response.ok().entity(swagger).build();
        } else {
            return Response.status(404).build();
        }
    }

    @GET
    @Produces("application/yaml")
    @Path("/swagger")
    @ApiOperation(value = "The swagger definition in YAML", hidden = true)
    public Response getListingYaml(
            @Context Application app,
            @Context ServletConfig sc,
            @Context HttpHeaders headers,
            @Context UriInfo uriInfo) {
        Swagger swagger = process(app, sc, headers, uriInfo);
        try {
            if (swagger != null) {
                String yaml = Yaml.mapper().writeValueAsString(swagger);
                StringBuilder b = new StringBuilder();
                String[] parts = yaml.split("\n");
                for (String part : parts) {
                    b.append(part);
                    b.append("\n");
                }
                return Response.ok().entity(b.toString()).type("application/yaml").build();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return Response.status(404).build();
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
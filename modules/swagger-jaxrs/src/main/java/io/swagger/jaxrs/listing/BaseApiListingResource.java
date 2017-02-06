package io.swagger.jaxrs.listing;

import com.fasterxml.jackson.core.JsonProcessingException;
import io.swagger.config.FilterFactory;
import io.swagger.config.Scanner;
import io.swagger.config.SwaggerConfig;
import io.swagger.core.filter.SpecFilter;
import io.swagger.core.filter.SwaggerSpecFilter;
import io.swagger.jaxrs.Reader;
import io.swagger.jaxrs.config.JaxrsScanner;
import io.swagger.jaxrs.config.ReaderConfigUtils;
import io.swagger.jaxrs.config.SwaggerContextService;
import io.swagger.models.Swagger;
import io.swagger.util.Json;
import io.swagger.util.Yaml;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.ws.rs.core.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * Created by rbolles on 2/15/16.
 */
public abstract class BaseApiListingResource {

    private static volatile boolean initialized = false;

    private static volatile ConcurrentMap<String, Boolean> initializedScanner = new ConcurrentHashMap<String, Boolean>();
    private static volatile ConcurrentMap<String, Boolean> initializedConfig = new ConcurrentHashMap<String, Boolean>();

    private static Logger LOGGER = LoggerFactory.getLogger(BaseApiListingResource.class);


    private static synchronized Swagger scan(Application app, ServletContext context, ServletConfig sc, UriInfo uriInfo) {
        Swagger swagger = null;

        SwaggerContextService ctxService = new SwaggerContextService()
            .withServletConfig(sc)
            .withBasePath(getBasePath(uriInfo));

        Scanner scanner = ctxService.getScanner();
        if (scanner != null) {
            SwaggerSerializers.setPrettyPrint(scanner.getPrettyPrint());
            swagger = new SwaggerContextService()
                .withServletConfig(sc)
                .withBasePath(getBasePath(uriInfo))
                .getSwagger();
            Set<Class<?>> classes;
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
                    SwaggerConfig swaggerConfig = ctxService.getConfig();
                    if (swaggerConfig != null) {
                        LOGGER.debug("configuring swagger with " + swaggerConfig);
                        swaggerConfig.configure(swagger);
                    } else {
                        LOGGER.debug("no configurator");
                    }
                }
                new SwaggerContextService()
                    .withServletConfig(sc)
                    .withBasePath(getBasePath(uriInfo))
                    .updateSwagger(swagger);
            }
        }
        if (SwaggerContextService.isScannerIdInitParamDefined(sc)) {
            initializedScanner.put(sc.getServletName() + "_" + SwaggerContextService.getScannerIdFromInitParam(sc), true);
        } else if (SwaggerContextService.isConfigIdInitParamDefined(sc)) {
            initializedConfig.put(sc.getServletName() + "_" + SwaggerContextService.getConfigIdFromInitParam(sc), true);
        } else if (SwaggerContextService.isUsePathBasedConfigInitParamDefined(sc)) {
            initializedConfig.put(sc.getServletName() + "_" + ctxService.getBasePath(), true);
        } else {
            initialized = true;
        }

        return swagger;
    }

    protected Swagger process(
            Application app,
            ServletContext servletContext,
            ServletConfig sc,
            HttpHeaders headers,
            UriInfo uriInfo) {
        SwaggerContextService ctxService = new SwaggerContextService()
            .withServletConfig(sc)
            .withBasePath(getBasePath(uriInfo));
        
        Swagger swagger = ctxService.getSwagger();
        synchronized (ApiListingResource.class) {
            if (SwaggerContextService.isScannerIdInitParamDefined(sc)) {
                if (!initializedScanner.containsKey(sc.getServletName() + "_" + SwaggerContextService.getScannerIdFromInitParam(sc))) {
                    swagger = scan(app, servletContext, sc, uriInfo);
                }
            } else {
                if (SwaggerContextService.isConfigIdInitParamDefined(sc)) {
                    if (!initializedConfig.containsKey(sc.getServletName() + "_" + SwaggerContextService.getConfigIdFromInitParam(sc))) {
                        swagger = scan(app, servletContext, sc, uriInfo);
                    }
                } else if (SwaggerContextService.isUsePathBasedConfigInitParamDefined(sc)) {
                    if (!initializedConfig.containsKey(sc.getServletName() + "_" + ctxService.getBasePath())) {
                        swagger = scan(app, servletContext, sc, uriInfo);
                    }
                } else if (!initialized) {
                    swagger = scan(app, servletContext, sc, uriInfo);
                }
            }
        }
        if (swagger != null) {
            SwaggerSpecFilter filterImpl = FilterFactory.getFilter();
            if (filterImpl != null) {
                SpecFilter f = new SpecFilter();
                swagger = f.filter(swagger, filterImpl, getQueryParams(uriInfo.getQueryParameters()), getCookies(headers),
                        getHeaders(headers));
            }
        }
        return swagger;
    }

    protected Response getListingYamlResponse(
            Application app,
            ServletContext servletContext,
            ServletConfig servletConfig,
            HttpHeaders headers,
            UriInfo uriInfo) {
        Swagger swagger = process(app, servletContext, servletConfig, headers, uriInfo);
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

    protected Response getListingJsonResponse(
            Application app,
            ServletContext servletContext,
            ServletConfig servletConfig,
            HttpHeaders headers,
            UriInfo uriInfo) throws JsonProcessingException {
        Swagger swagger = process(app, servletContext, servletConfig, headers, uriInfo);

        if (swagger != null) {
            return Response.ok().entity(Json.mapper().writeValueAsString(swagger)).type(MediaType.APPLICATION_JSON_TYPE).build();
        } else {
            return Response.status(404).build();
        }
    }

    private static Map<String, List<String>> getQueryParams(MultivaluedMap<String, String> params) {
        Map<String, List<String>> output = new HashMap<String, List<String>>();
        if (params != null) {
            for (String key : params.keySet()) {
                List<String> values = params.get(key);
                output.put(key, values);
            }
        }
        return output;
    }

    private static Map<String, String> getCookies(HttpHeaders headers) {
        Map<String, String> output = new HashMap<String, String>();
        if (headers != null) {
            for (String key : headers.getCookies().keySet()) {
                Cookie cookie = headers.getCookies().get(key);
                output.put(key, cookie.getValue());
            }
        }
        return output;
    }

    private static Map<String, List<String>> getHeaders(HttpHeaders headers) {
        Map<String, List<String>> output = new HashMap<String, List<String>>();
        if (headers != null) {
            for (String key : headers.getRequestHeaders().keySet()) {
                List<String> values = headers.getRequestHeaders().get(key);
                output.put(key, values);
            }
        }
        return output;
    }

    private static String getBasePath(UriInfo uriInfo) {
        if (uriInfo != null) {
            return uriInfo.getBaseUri().getPath();
        } else {
            return "/";
        }
    }

}

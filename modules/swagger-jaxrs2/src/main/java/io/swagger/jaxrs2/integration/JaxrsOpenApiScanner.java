package io.swagger.jaxrs2.integration;

import io.swagger.oas.web.OpenApiScanner;

import javax.ws.rs.core.Application;
import java.util.Map;

public interface JaxrsOpenApiScanner extends OpenApiScanner{


    void setApplication (Application application);

    Map<String, Object> resources();
}

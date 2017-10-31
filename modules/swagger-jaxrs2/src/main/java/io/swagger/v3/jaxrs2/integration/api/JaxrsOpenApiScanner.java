package io.swagger.v3.jaxrs2.integration.api;

import io.swagger.v3.oas.integration.api.OpenApiScanner;

import javax.ws.rs.core.Application;
import java.util.Map;

public interface JaxrsOpenApiScanner extends OpenApiScanner{


    void setApplication (Application application);

    Map<String, Object> resources();
}

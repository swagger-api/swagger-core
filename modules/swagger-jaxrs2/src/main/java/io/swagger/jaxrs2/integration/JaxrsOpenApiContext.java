package io.swagger.jaxrs2.integration;

import io.swagger.jaxrs2.Reader;
import io.swagger.oas.integration.GenericOpenApiContext;
import io.swagger.oas.integration.OpenApiConfiguration;
import io.swagger.oas.integration.OpenApiContext;
import io.swagger.oas.web.OpenApiReader;
import io.swagger.oas.web.OpenApiScanner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.core.Application;

public class JaxrsOpenApiContext<T extends JaxrsOpenApiContext> extends GenericOpenApiContext<JaxrsOpenApiContext> implements OpenApiContext {
    Logger LOGGER = LoggerFactory.getLogger(JaxrsOpenApiContext.class);

    private Application app;

    public T withApp(Application app) {
        this.app = app;
        return (T)this;
    }


    @Override
    protected OpenApiReader buildReader(OpenApiConfiguration openApiConfiguration) throws Exception {
        LOGGER.trace("buildReader");
        return new Reader(openApiConfiguration);
    }

    @Override
    protected OpenApiScanner buildScanner(OpenApiConfiguration openApiConfiguration) throws Exception {
        LOGGER.trace("buildscanner");
        return new AnnotationJaxrsScanner().withOpenApiConfiguration(openApiConfiguration);
    }

}

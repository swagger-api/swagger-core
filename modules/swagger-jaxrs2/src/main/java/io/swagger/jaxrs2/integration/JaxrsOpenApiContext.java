package io.swagger.jaxrs2.integration;

import io.swagger.jaxrs2.Reader;
import io.swagger.oas.integration.GenericOpenApiContext;
import io.swagger.oas.integration.GenericOpenApiScanner;
import io.swagger.oas.integration.OpenApiConfiguration;
import io.swagger.oas.integration.OpenApiContext;
import io.swagger.oas.models.OpenAPI;
import io.swagger.oas.web.OpenApiReader;
import io.swagger.oas.web.OpenApiScanner;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.core.Application;
import java.util.Map;
import java.util.Set;

public class JaxrsOpenApiContext<T extends JaxrsOpenApiContext> extends GenericOpenApiContext<JaxrsOpenApiContext> implements OpenApiContext {
    Logger LOGGER = LoggerFactory.getLogger(JaxrsOpenApiContext.class);

    private Application app;

    public T app(Application app) {
        this.app = app;
        return (T)this;
    }


    @Override
    protected OpenApiReader buildReader(OpenApiConfiguration openApiConfiguration) throws Exception {
        OpenApiReader reader;
        if (StringUtils.isNotBlank(openApiConfiguration.getReaderClass())) {
            Class cls = getClass().getClassLoader().loadClass(openApiConfiguration.getReaderClass());
            // TODO instantiate with configuration
            reader = (OpenApiReader) cls.newInstance();
        } else {
            reader = new Reader(openApiConfiguration);
        }
        return reader;
    }

    @Override
    protected OpenApiScanner buildScanner(OpenApiConfiguration openApiConfiguration) throws Exception {
        OpenApiScanner scanner;
        if (StringUtils.isNotBlank(openApiConfiguration.getScannerClass())) {
            Class cls = getClass().getClassLoader().loadClass(openApiConfiguration.getScannerClass());
            // TODO instantiate with configuration
            scanner = (OpenApiScanner) cls.newInstance();
        } else {
            scanner = new JaxrsApplicationAndAnnotationScanner().application(app).openApiConfiguration(openApiConfiguration);
        }
        return scanner;
    }

}

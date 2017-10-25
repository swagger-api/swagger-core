package io.swagger.v3.jaxrs2.integration;

import io.swagger.v3.jaxrs2.Reader;
import io.swagger.v3.jaxrs2.integration.api.JaxrsOpenApiScanner;
import io.swagger.v3.oas.integration.GenericOpenApiContext;
import io.swagger.v3.oas.integration.api.OpenAPIConfiguration;
import io.swagger.v3.oas.integration.api.OpenApiContext;
import io.swagger.v3.oas.integration.api.OpenApiReader;
import io.swagger.v3.oas.integration.api.OpenApiScanner;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.core.Application;

public class JaxrsOpenApiContext<T extends JaxrsOpenApiContext> extends GenericOpenApiContext<JaxrsOpenApiContext> implements OpenApiContext {
    Logger LOGGER = LoggerFactory.getLogger(JaxrsOpenApiContext.class);

    private Application app;

    public T app(Application app) {
        this.app = app;
        return (T)this;
    }


    @Override
    protected OpenApiReader buildReader(OpenAPIConfiguration openApiConfiguration) throws Exception {
        OpenApiReader reader;
        if (StringUtils.isNotBlank(openApiConfiguration.getReaderClass())) {
            Class cls = getClass().getClassLoader().loadClass(openApiConfiguration.getReaderClass());
            reader = (OpenApiReader) cls.newInstance();
        } else {
            reader = new Reader();
        }
        if (reader instanceof Reader) {
            ((Reader)reader).setApplication(app);
        }
        reader.setConfiguration(openApiConfiguration);
        return reader;
    }

    @Override
    protected OpenApiScanner buildScanner(OpenAPIConfiguration openApiConfiguration) throws Exception {

        OpenApiScanner scanner;
        if (StringUtils.isNotBlank(openApiConfiguration.getScannerClass())) {
            Class cls = getClass().getClassLoader().loadClass(openApiConfiguration.getScannerClass());
            scanner = (OpenApiScanner) cls.newInstance();
        } else {
            scanner = new JaxrsApplicationAndAnnotationScanner();
        }
        scanner.setConfiguration(openApiConfiguration);
        if (scanner instanceof JaxrsOpenApiScanner) {
            ((JaxrsOpenApiScanner)scanner).setApplication(app);
        }
        return scanner;
    }
}

package io.swagger.jaxrs2.integration;

import io.swagger.jaxrs2.Reader;
import io.swagger.jaxrs2.integration.api.JaxrsOpenApiScanner;
import io.swagger.oas.integration.ext.OpenApiContext;
import io.swagger.oas.integration.impl.GenericOpenApiContext;
import io.swagger.oas.integration.OpenAPIConfiguration;
import io.swagger.oas.integration.OpenAPIScanner;
import io.swagger.oas.integration.OpenAPIReader;

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
    protected OpenAPIReader buildReader(OpenAPIConfiguration openApiConfiguration) throws Exception {
        OpenAPIReader reader;
        if (StringUtils.isNotBlank(openApiConfiguration.getReaderClass())) {
            Class cls = getClass().getClassLoader().loadClass(openApiConfiguration.getReaderClass());
            reader = (OpenAPIReader) cls.newInstance();
        } else {
            reader = new Reader();
        }
        reader.setConfiguration(openApiConfiguration);
        return reader;
    }

    @Override
    protected OpenAPIScanner buildScanner(OpenAPIConfiguration openApiConfiguration) throws Exception {

        OpenAPIScanner scanner;
        if (StringUtils.isNotBlank(openApiConfiguration.getScannerClass())) {
            Class cls = getClass().getClassLoader().loadClass(openApiConfiguration.getScannerClass());
            scanner = (OpenAPIScanner) cls.newInstance();
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

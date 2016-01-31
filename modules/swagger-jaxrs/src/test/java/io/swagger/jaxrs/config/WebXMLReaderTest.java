package io.swagger.jaxrs.config;

import io.swagger.models.Swagger;
import org.testng.annotations.Test;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

import static org.testng.AssertJUnit.assertEquals;
import static org.testng.AssertJUnit.assertNotNull;

public class WebXMLReaderTest {
    @Test
    public void initSwagger() {
        final Map<String, String> params = new HashMap<String, String>();
        params.put("swagger.api.title", "Too manual");
        params.put("swagger.info.title", "Override text");
        params.put("swagger.info.description", "Some text");

        final WebXMLReader reader = new WebXMLReader(new ServletConfig() {
            @Override
            public String getServletName() {
                return "swagger";
            }

            @Override
            public ServletContext getServletContext() {
                return null;
            }

            @Override
            public String getInitParameter(final String s) {
                return params.get(s);
            }

            @Override
            public Enumeration getInitParameterNames() {
                return Collections.enumeration(params.keySet());
            }
        });

        final Swagger swagger = new Swagger();
        reader.configure(swagger);

        assertNotNull(swagger.getInfo());
        assertEquals("Override text", swagger.getInfo().getTitle());
        assertEquals("Some text", swagger.getInfo().getDescription());
    }
}

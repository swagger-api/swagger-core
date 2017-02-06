package io.swagger;

import com.fasterxml.jackson.core.JsonProcessingException;
import io.swagger.jaxrs.Reader;
import io.swagger.jaxrs.listing.ApiListingResource;
import io.swagger.models.Swagger;
import org.testng.annotations.Test;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import java.util.Enumeration;

import static org.testng.Assert.assertNull;

/**
 * The {@code ApiListingResourceTest} test should confirm that scanning of
 * the {@link ApiListingResource} class doesn't affect Swagger output.
 */
public class ApiListingResourceTest {
    private final Swagger swagger = new Reader(new Swagger()).read(ApiListingResource.class);

    @Test
    public void shouldCheckModelsSet() {
        assertNull(swagger.getDefinitions());
    }

    @Test
    public void shouldHandleNullServletConfig_issue1689() throws JsonProcessingException {
        ApiListingResource a = new ApiListingResource();
        try {
            a.getListing(null, null, null, null, "json");
        } catch (RuntimeException e) {
            // test will fail before, no need to mock Response
            if(e.getCause() instanceof ClassNotFoundException) {
                return;
            }
            throw e;
        }

    }
    @Test
    public void shouldHandleErrorServletConfig_issue1691() throws JsonProcessingException {

        ServletConfig sc = new ServletConfig() {
            @Override
            public String getServletName() {
                throw new RuntimeException("test_1691");
            }

            @Override
            public ServletContext getServletContext() {
                throw new RuntimeException("test_1691");
            }

            @Override
            public String getInitParameter(String name) {
                throw new RuntimeException("test_1691");
            }

            @Override
            public Enumeration getInitParameterNames() {
                throw new RuntimeException("test_1691");
            }
        };

        ApiListingResource a = new ApiListingResource();
        try {
            a.getListing(null, sc, null, null, "json");
        } catch (RuntimeException e) {
            // test will fail before, no need to mock Response
            if(e.getCause() instanceof ClassNotFoundException) {
                return;
            }
            throw e;
        }

    }
}

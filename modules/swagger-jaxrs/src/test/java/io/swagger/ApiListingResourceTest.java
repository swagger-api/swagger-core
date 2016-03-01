package io.swagger;

import io.swagger.jaxrs.Reader;
import io.swagger.jaxrs.listing.ApiListingResource;
import io.swagger.models.Swagger;
import org.testng.annotations.Test;

import static org.testng.Assert.assertNull;

/**
 * The <code>ApiListingResourceTest</code> test should confirm that scanning of
 * the {@link ApiListingResource} class doesn't affect Swagger output.
 */
public class ApiListingResourceTest {
    private final Swagger swagger = new Reader(new Swagger()).read(ApiListingResource.class);

    @Test
    public void shouldCheckModelsSet() {
        assertNull(swagger.getDefinitions());
    }

    @Test
    public void shouldHandleNullServletConfig_issue1689() {
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
}

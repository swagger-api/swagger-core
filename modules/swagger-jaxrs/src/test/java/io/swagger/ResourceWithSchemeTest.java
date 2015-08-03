package io.swagger;

import io.swagger.jaxrs.Reader;
import io.swagger.models.Scheme;
import io.swagger.models.Swagger;
import io.swagger.resources.ResourceWithScheme;
import io.swagger.resources.ResourceWithoutScheme;
import org.testng.annotations.Test;

import java.util.Arrays;
import java.util.List;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNull;

public class ResourceWithSchemeTest {
    private final Reader reader = new Reader(new Swagger());

    private Swagger getSwagger(Class<?> resource) {
        return reader.read(resource);
    }

    private List<Scheme> loadSchemes(Swagger swagger, String path) {
        return swagger.getPaths().get(path).getGet().getSchemes();
    }

    @Test(description = "scan another resource with subresources")
    public void scanResourceWithSubresources() {
        Swagger swagger = getSwagger(ResourceWithScheme.class);
        assertEquals(loadSchemes(swagger, "/test/status"), Arrays.asList(Scheme.HTTPS));
        assertEquals(loadSchemes(swagger, "/test/value"), Arrays.asList(Scheme.WS, Scheme.WSS));
        assertEquals(loadSchemes(swagger, "/test/notes"), Arrays.asList(Scheme.HTTP));
        assertEquals(loadSchemes(swagger, "/test/description"), Arrays.asList(Scheme.HTTP));
    }

    @Test(description = "scan resource without schemes")
    public void scanResourceWithoutSchemes() {
        Swagger swagger = getSwagger(ResourceWithoutScheme.class);
        assertNull(loadSchemes(swagger, "/test/status"));
        assertNull(loadSchemes(swagger, "/test/value"));
    }
}


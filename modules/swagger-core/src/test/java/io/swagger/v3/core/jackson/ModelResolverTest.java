package io.swagger.v3.core.jackson;

import io.swagger.v3.core.util.Json;
import jakarta.ws.rs.core.Response;
import org.testng.annotations.Test;

import java.lang.reflect.Type;

import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;

public class ModelResolverTest {

    @Test
    public void shouldIgnoreJakartaRestResponse() {
        TestModelResolver resolver = new TestModelResolver();

        assertTrue(resolver.shouldIgnore(Response.class));
        assertFalse(resolver.shouldIgnore(String.class));
    }

    private static class TestModelResolver extends ModelResolver {

        private TestModelResolver() {
            super(Json.mapper());
        }

        private boolean shouldIgnore(Type type) {
            return shouldIgnoreClass(type);
        }
    }
}

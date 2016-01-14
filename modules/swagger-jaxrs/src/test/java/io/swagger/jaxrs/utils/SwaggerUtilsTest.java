package io.swagger.jaxrs.utils;

import io.swagger.models.Swagger;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.net.URI;

import static org.testng.Assert.assertNotEquals;
import static org.testng.Assert.assertNull;
import static org.testng.AssertJUnit.assertEquals;

public class SwaggerUtilsTest {

    @Test()
    public void testGetSwagger() throws Exception {
        final Swagger swagger = SwaggerUtils.getSwagger(SwaggerUtils.Host.from(URI.create("http://localhost")));
        assertNull(swagger);
    }

    @Test()
    public void testPutSwagger() throws Exception {
        final SwaggerUtils.Host host = SwaggerUtils.Host.from(URI.create("http://localhost"));
        final Swagger swagger = new Swagger();
        SwaggerUtils.putSwagger(host, swagger);
        Assert.assertEquals(swagger, SwaggerUtils.getSwagger(host));
    }

    @Test()
    public void testSimpleHost() throws Exception {
        final SwaggerUtils.Host h = SwaggerUtils.Host.from(URI.create("http://localhost"));
        assertEquals("http", h.scheme);
        assertEquals("localhost", h.host);
        assertEquals(-1, h.port);
        assertEquals("", h.path);

        final SwaggerUtils.Host h2 = SwaggerUtils.Host.from(URI.create("http://localhost"));
        assertEquals(h, h2);
        assertEquals(h.hashCode(), h2.hashCode());
    }

    @Test()
    public void testFullHost() throws Exception {
        final SwaggerUtils.Host h = SwaggerUtils.Host.from(URI.create("http://localhost:8080/path"));
        assertEquals("http", h.scheme);
        assertEquals("localhost", h.host);
        assertEquals(8080, h.port);
        assertEquals("/path", h.path);

        final SwaggerUtils.Host h2 = SwaggerUtils.Host.from(URI.create("http://localhost:8080/path"));
        assertEquals(h, h2);
        assertEquals(h.hashCode(), h2.hashCode());
    }

    @Test()
    public void testSwaggerHost() throws Exception {
        final SwaggerUtils.Host h = SwaggerUtils.Host.from(URI.create("http://localhost:8080/path/swagger"));
        assertEquals("http", h.scheme);
        assertEquals("localhost", h.host);
        assertEquals(8080, h.port);
        assertEquals("/path", h.path);

        final SwaggerUtils.Host h2 = SwaggerUtils.Host.from(URI.create("http://localhost:8080/path/swagger"));
        assertEquals(h, h2);
        assertEquals(h.hashCode(), h2.hashCode());
    }

    @Test()
    public void testJsonSwaggerHost() throws Exception {
        final SwaggerUtils.Host h = SwaggerUtils.Host.from(URI.create("http://localhost:8080/path/swagger.json"));
        assertEquals("http", h.scheme);
        assertEquals("localhost", h.host);
        assertEquals(8080, h.port);
        assertEquals("/path", h.path);

        final SwaggerUtils.Host h2 = SwaggerUtils.Host.from(URI.create("http://localhost:8080/path/swagger.json"));
        assertEquals(h, h2);
        assertEquals(h.hashCode(), h2.hashCode());
    }

    @Test()
    public void testYamlSwaggerHost() throws Exception {
        final SwaggerUtils.Host h = SwaggerUtils.Host.from(URI.create("http://localhost:8080/path/swagger.yaml"));
        assertEquals("http", h.scheme);
        assertEquals("localhost", h.host);
        assertEquals(8080, h.port);
        assertEquals("/path", h.path);

        final SwaggerUtils.Host h2 = SwaggerUtils.Host.from(URI.create("http://localhost:8080/path/swagger.yaml"));
        assertEquals(h, h2);
        assertEquals(h.hashCode(), h2.hashCode());
    }

    @Test()
    public void testNotEqualsHost() throws Exception {
        final SwaggerUtils.Host ref = SwaggerUtils.Host.from(URI.create("http://localhost:8080/path"));

        final SwaggerUtils.Host schemeDiff = SwaggerUtils.Host.from(URI.create("https://localhost:8080/path"));
        assertNotEquals(ref, schemeDiff);
        assertNotEquals(ref.hashCode(), schemeDiff.hashCode());

        final SwaggerUtils.Host hostDiff = SwaggerUtils.Host.from(URI.create("http://localhost1:8080/path"));
        assertNotEquals(ref, hostDiff);
        assertNotEquals(ref.hashCode(), hostDiff.hashCode());

        final SwaggerUtils.Host portDiff = SwaggerUtils.Host.from(URI.create("http://localhost:8081/path"));
        assertNotEquals(ref, portDiff);
        assertNotEquals(ref.hashCode(), portDiff.hashCode());

        final SwaggerUtils.Host pathDiff = SwaggerUtils.Host.from(URI.create("http://localhost:8080/path1"));
        assertNotEquals(ref, pathDiff);
        assertNotEquals(ref.hashCode(), pathDiff.hashCode());
    }
}

package io.swagger.jaxrs.config;

import org.testng.annotations.Test;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Application;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.ext.MessageBodyReader;
import javax.ws.rs.ext.Provider;
import java.io.IOException;
import java.io.InputStream;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import java.util.HashSet;
import java.util.Set;

import static java.util.Arrays.asList;
import static java.util.Collections.singleton;
import static org.testng.AssertJUnit.assertEquals;
import static org.testng.AssertJUnit.assertTrue;

public class DefaultJaxrsScannerTest {
    @Test(description = "jaxrs scanning find classes based on Application")
    public void scan() {
        final ApplicationBeanConfig config = new ApplicationBeanConfig(new Application() {
            @Override
            public Set<Class<?>> getClasses() {
                return new HashSet<Class<?>>(asList(Endpoint.class, MyProvider.class));
            }
        });
        config.setScan(false);
        assertEquals(singleton(Endpoint.class), config.classes());
    }

    @Test(description = "jaxrs scanning find classes based on Application with filtering")
    public void scanWithFiltering() {
        final ApplicationBeanConfig config = new ApplicationBeanConfig(new Application() {
            @Override
            public Set<Class<?>> getClasses() {
                return new HashSet<Class<?>>(asList(Endpoint.class, MyProvider.class));
            }
        });
        config.setResourcePackage("filter.all");
        config.setScan(false);
        assertTrue(config.classes().isEmpty());
    }

    @Path("e")
    public static class Endpoint {
        @GET
        public String e() {
            throw new UnsupportedOperationException();
        }
    }

    @Provider
    public static class MyProvider implements MessageBodyReader<String> {
        @Override
        public boolean isReadable(Class<?> type, Type genericType, Annotation[] annotations, MediaType mediaType) {
            throw new UnsupportedOperationException();
        }

        @Override
        public String readFrom(Class<String> type, Type genericType, Annotation[] annotations, MediaType mediaType, MultivaluedMap<String, String> httpHeaders, InputStream entityStream) throws IOException, WebApplicationException {
            throw new UnsupportedOperationException();
        }
    }
}

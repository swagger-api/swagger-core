package io.swagger;

import io.swagger.jaxrs.Reader;
import io.swagger.models.Info;
import io.swagger.models.Scheme;
import io.swagger.models.Swagger;
import io.swagger.resources.ResourceWithConfigAndExtensions;
import io.swagger.util.Json;
import org.testng.annotations.Test;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotEquals;
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertTrue;

public class ConfigAndExtensionScannerTest {

    @Test(description = "scan a resource with extensions")
    public void scanResourceWithExtensions() {
        Set<Class<?>> classes = new HashSet<Class<?>>();
        classes.add(ResourceWithConfigAndExtensions.class);
        Swagger swagger = new Reader(new Swagger()).read(classes);
        assertEquals(swagger.getPaths().size(), 1);

        Info info = swagger.getInfo();
        assertNotNull(info);
        assertEquals(info.getDescription(), "Custom description");
        assertEquals(info.getTermsOfService(), "do-what-you-want");
        assertEquals(info.getTitle(), "TheAwesomeApi");
        assertEquals(info.getVersion(), "V1.2.3");
        assertEquals(info.getContact().getName(), "Sponge-Bob");
        assertEquals(info.getContact().getEmail(), "sponge-bob@swagger.io");
        assertEquals(info.getContact().getUrl(), "http://swagger.io");
        assertEquals(info.getLicense().getName(), "Apache 2.0");
        assertEquals(info.getLicense().getUrl(), "http://www.apache.org");

        assertEquals(swagger.getConsumes().size(), 2);
        assertTrue(swagger.getConsumes().contains("application/json"));
        assertTrue(swagger.getConsumes().contains("application/xml"));

        assertEquals(swagger.getProduces().size(), 2);
        swagger.getProduces().contains("application/json");
        swagger.getProduces().contains("application/xml");

        assertEquals(swagger.getExternalDocs().getDescription(), "test");
        assertEquals(swagger.getExternalDocs().getUrl(), "http://swagger.io");

        assertEquals(swagger.getSchemes().size(), 2);
        assertTrue(swagger.getSchemes().contains(Scheme.HTTP));
        assertTrue(swagger.getSchemes().contains(Scheme.HTTPS));

        assertEquals(swagger.getTags().size(), 7);
        assertEquals(swagger.getTags().get(0).getName(), "Tag-added-before-read");

        assertEquals(swagger.getTags().get(1).getName(), "mytag");
        assertEquals(swagger.getTags().get(1).getDescription(), "my tag");

        assertEquals(swagger.getTags().get(2).getName(), "anothertag");
        assertEquals(swagger.getTags().get(2).getDescription(), "another tag");
        assertEquals(swagger.getTags().get(2).getExternalDocs().getDescription(), "test");
        assertEquals(swagger.getTags().get(2).getExternalDocs().getUrl(), "http://swagger.io");

        assertEquals(swagger.getTags().get(3).getName(), "tagwithextensions");
        assertEquals(swagger.getTags().get(3).getDescription(), "my tag");
        Map<String, Object> extensions = swagger.getTags().get(3).getVendorExtensions();
        assertEquals(extensions.size(), 1);
        assertEquals(extensions.get("x-test"), "value");

        assertEquals(swagger.getTags().get(4).getName(), "externalinfo");
        assertEquals(swagger.getTags().get(5).getName(), "testingtag");
        assertEquals(swagger.getTags().get(6).getName(), "Tag-added-after-read");

        extensions = swagger.getInfo().getVendorExtensions();
        assertEquals(extensions.size(), 3);
        assertEquals(extensions.get("x-test1"), "value1");
        assertEquals(extensions.get("x-test2"), "value2");

        extensions = (Map<String, Object>) extensions.get("x-test");
        assertEquals(extensions.get("test1"), "value1");
        assertEquals(extensions.get("test2"), "value2");

        extensions = swagger.getPath("/who/cares").getOperations().get(0).getVendorExtensions();
        assertEquals(extensions.get("x-test"), "value");

        String json = Json.pretty(swagger);
        assertNotEquals(json.indexOf("\"x-test\" : {"), -1);
        assertNotEquals(json.indexOf("\"x-test1\" : \"value1\""), -1);
        assertNotEquals(json.indexOf("\"x-test2\" : \"value2\""), -1);
    }
}

package io.swagger.servlet;

import io.swagger.models.Info;
import io.swagger.models.Operation;
import io.swagger.models.Path;
import io.swagger.models.Scheme;
import io.swagger.models.Swagger;
import io.swagger.models.Tag;
import io.swagger.servlet.resources.ResourceWithAnnotations;
import io.swagger.servlet.resources.ResourceWithoutApiAnnotation;

import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.Arrays;
import java.util.Collections;

public class ReaderTest {

    @Test
    public void readerTest1() {
        final Swagger swagger = new Swagger();
        Reader.read(swagger, Collections.<Class<?>>singleton(ResourceWithAnnotations.class));

        Assert.assertNotNull(swagger);

        final Info info = swagger.getInfo();
        Assert.assertNotNull(info);
        Assert.assertEquals(info.getTitle(), "Test title");
        Assert.assertEquals(info.getDescription(), "Test description");
        Assert.assertEquals(info.getVersion(), "1.0.0");
        Assert.assertEquals(info.getTermsOfService(), "link_to_terms");

        Assert.assertEquals(info.getContact().getName(), "Author");
        Assert.assertEquals(info.getContact().getEmail(), "author@mail");
        Assert.assertEquals(info.getContact().getUrl(), "site");

        Assert.assertEquals(info.getLicense().getName(), "License");
        Assert.assertEquals(info.getLicense().getUrl(), "license_url");

        Assert.assertEquals(info.getVendorExtensions().get("x-ext1_prop1"), "ext1_val1");
        Assert.assertEquals(info.getVendorExtensions().get("x-ext1_prop2"), "x-ext1_val2");

        Assert.assertEquals(swagger.getHost(), "host");
        Assert.assertEquals(swagger.getBasePath(), "/api");
        Assert.assertNotNull(swagger.getPath("/resources/testMethod3"));
        Assert.assertNotNull(swagger.getDefinitions().get("SampleData"));
        Assert.assertEquals(swagger.getExternalDocs().getDescription(), "docs");
        Assert.assertEquals(swagger.getExternalDocs().getUrl(), "url_to_docs");

        Path path = swagger.getPath("/resources/testMethod3");
        Assert.assertNotNull(path);
        Operation get = path.getGet();
        Assert.assertNotNull( get );
        Assert.assertEquals( "value", get.getVendorExtensions().get("x-name"));
    }

    @Test
    public void readerTest2() {
        final Swagger swagger = new Swagger();
        Reader.read(swagger, Collections.<Class<?>>singleton(ResourceWithoutApiAnnotation.class));

        Assert.assertNotNull(swagger);
        Assert.assertEquals(swagger.getSwagger(), "2.0");
        Assert.assertEquals(swagger.getTags(), Collections.singletonList(new Tag().name("tests").description("tests")));
        Assert.assertEquals(swagger.getSchemes(), Arrays.asList(Scheme.HTTP, Scheme.HTTPS));
        Assert.assertEquals(swagger.getConsumes(), Arrays.asList("application/json", "application/xml"));
        Assert.assertEquals(swagger.getProduces(), Arrays.asList("application/json", "application/xml"));
    }
}

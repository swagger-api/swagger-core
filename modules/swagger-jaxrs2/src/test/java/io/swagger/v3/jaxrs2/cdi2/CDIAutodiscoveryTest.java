package io.swagger.v3.jaxrs2.cdi2;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.testng.Arquillian;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.testng.AssertJUnit;
import org.testng.annotations.Test;

import io.swagger.v3.jaxrs2.SwaggerSerializers;
import io.swagger.v3.jaxrs2.integration.resources.OpenApiResource;

import javax.enterprise.inject.spi.Extension;
import javax.inject.Inject;

public class CDIAutodiscoveryTest extends Arquillian {

    @Inject
    DiscoveryTestExtension ext;

    @Deployment
    public static JavaArchive createDeployment() {
        return ShrinkWrap.create(JavaArchive.class)
                .addClasses(SwaggerSerializers.class)
                .addPackage(OpenApiResource.class.getPackage())
                .addAsServiceProviderAndClasses(Extension.class, DiscoveryTestExtension.class)
                .addAsManifestResource("META-INF/beans.xml");
    }

    @Test
    void confirmPathClassesWereDiscovered() {
        String[] expected = {
                "io.swagger.v3.jaxrs2.integration.resources.AcceptHeaderOpenApiResource",
                "io.swagger.v3.jaxrs2.integration.resources.OpenApiResource"
                };
        Object[] found = ext.getResources().stream()
                .map(resource -> resource.getName())
                .sorted()
                .toArray();
        AssertJUnit.assertArrayEquals(expected, found);
    }

    @Test
    void confirmProviderClassesWereDiscovered() {
        String[] expected = {
                "io.swagger.v3.jaxrs2.SwaggerSerializers"
                };
        Object[] found = ext.getProviders().stream()
                .map(resource -> resource.getName())
                .toArray();
        AssertJUnit.assertArrayEquals(expected, found);
    }

}

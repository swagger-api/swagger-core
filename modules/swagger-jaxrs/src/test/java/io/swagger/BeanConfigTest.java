package io.swagger;

import com.splitresourcesTestImpl.SplitResourceImpl;
import com.subresourcesTest.RootResource;
import io.swagger.jaxrs.config.BeanConfig;
import io.swagger.models.Scheme;
import io.swagger.models.Swagger;
import org.testng.annotations.Test;

import java.util.Arrays;
import java.util.EnumSet;
import java.util.HashSet;
import java.util.Set;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertTrue;

public class BeanConfigTest {
	
    private final Set<?> expectedKeys = new HashSet<String>(Arrays.asList("/packageA", "/packageB"));
    private final Set<Scheme> expectedSchemas = EnumSet.of(Scheme.HTTP, Scheme.HTTPS);

    private BeanConfig createBeanConfig(String rp) {
        BeanConfig bc = new BeanConfig();
        bc.setResourcePackage(rp);
        bc.setSchemes(new String[]{"http", "https"});
        bc.setHost("petstore.swagger.io");
        bc.setBasePath("/api");
        bc.setTitle("Petstore Sample API");
        bc.setDescription("A sample API that uses a petstore as an example to demonstrate features in the swagger-2.0 specification");
        bc.setTermsOfServiceUrl("http://swagger.io/terms/");
        bc.setContact("Swagger API Team");
        bc.setLicense("MIT");
        bc.setLicenseUrl("http://github.com/gruntjs/grunt/blob/master/LICENSE-MIT");
        bc.setScan(true);
        return bc;
    }

    @Test(description = "scan a simple resource")
    public void shouldScanASimpleResource() {
        Swagger swagger = createBeanConfig("com.my.project.resources,org.my.project.resources").getSwagger();
        assertNotNull(swagger);
        assertEquals(swagger.getPaths().keySet(), expectedKeys);
        assertEquals(swagger.getSchemes(), expectedSchemas);
    }

    @Test(description = "deep scan packages per #1011")
    public void shouldDeepScanPakagesPer1011() {
        Swagger swagger = createBeanConfig("com.my,org.my").getSwagger();
        assertNotNull(swagger);
        assertEquals(swagger.getPaths().keySet(), expectedKeys);
        assertEquals(swagger.getSchemes(), expectedSchemas);
    }

    @Test
    public void testBeanConfigOnlyScansResourcesAnnoatedWithPaths() throws Exception {
        BeanConfig bc = new BeanConfig();
        bc.setResourcePackage("com.subresourcesTest");

        Set<Class<?>> classes = bc.classes();

        assertEquals(classes.size(), 1, "BeanConfig should only pick up the root resource because it has a @Path annotation at the class level");
        assertTrue(classes.contains(RootResource.class));
    }

    @Test
    public void testBeanConfigScansSplitResourcesAnnoatedWithPathAndApi() throws Exception {
        BeanConfig bc = new BeanConfig();
        bc.setResourcePackage("com.splitresourcesTestImpl");

        Set<Class<?>> classes = bc.classes();

        assertEquals(classes.size(), 1, "BeanConfig should pick up implementations annotated with @Api that have a superinterface with @Path");
        assertTrue(classes.contains(SplitResourceImpl.class));
    }
}

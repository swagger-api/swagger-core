package io.swagger.mule;

import io.swagger.jaxrs.config.BeanConfig;
import io.swagger.models.Swagger;
import org.testng.Assert;
import org.testng.annotations.Test;

public class ApiListingJSONTest {

    @Test(description = "test Swagger initialization from BeanConfig")
    public void initializeTest() {
        final BeanConfig bc = new BeanConfig();
        bc.setTitle("Petstore Sample API");
        bc.setHost("petstore.swagger.io");
        bc.setBasePath("/api");
        bc.setScan(true);

        final ApiListingJSON listing = new ApiListingJSON();
        // Initializing by Swagger object
        ApiListingJSON.init(new Swagger());
        // Reading configuration and scanning resources
        listing.scan(null);

        final Swagger sw = ApiListingJSON.swagger;
        Assert.assertNotNull(sw);
        Assert.assertEquals(sw.getInfo().getTitle(), "Petstore Sample API");
        Assert.assertEquals(sw.getHost(), "petstore.swagger.io");
        Assert.assertEquals(sw.getBasePath(), "/api");
    }
}

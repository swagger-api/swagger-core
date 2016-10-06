package io.swagger;

import io.swagger.config.SwaggerConfig;
import io.swagger.jaxrs.config.SwaggerConfigLocator;
import io.swagger.models.Swagger;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.util.UUID;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotEquals;

public class SwaggerConfigLocatorTest {

    String id = UUID.randomUUID().toString() + System.currentTimeMillis();

    @BeforeMethod
    public void setId() {
        id = UUID.randomUUID().toString() + System.currentTimeMillis();
    }

    @Test(description = "should add given config to map ")
    public void putConfigFirstTime() {

        SwaggerConfig config = new SwaggerConfig() {
            @Override
            public Swagger configure(Swagger swagger) {
                return swagger;
            }

            @Override
            public String getFilterClass() {
                return null;
            }
        };

        SwaggerConfigLocator.getInstance().putConfig(id, config);
        assertEquals(SwaggerConfigLocator.getInstance().getConfig(id), config);

    }

    @Test(description = "shouldn't add given config to map because already set")
    public void putConfigSecondTime() {

        putConfigFirstTime();

        SwaggerConfig config = new SwaggerConfig() {
            @Override
            public Swagger configure(Swagger swagger) {
                return swagger;
            }

            @Override
            public String getFilterClass() {
                return null;
            }
        };

        SwaggerConfigLocator.getInstance().putConfig(id, config);
        assertNotEquals(SwaggerConfigLocator.getInstance().getConfig(id), config);

    }
}

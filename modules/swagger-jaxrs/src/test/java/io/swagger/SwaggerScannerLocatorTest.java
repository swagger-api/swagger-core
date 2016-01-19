package io.swagger;

import io.swagger.config.Scanner;
import io.swagger.jaxrs.config.SwaggerScannerLocator;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.util.Set;
import java.util.UUID;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotEquals;

public class SwaggerScannerLocatorTest {

    String id = UUID.randomUUID().toString() + System.currentTimeMillis();

    @BeforeMethod
    public void setId() {
        id = UUID.randomUUID().toString() + System.currentTimeMillis();
    }

    @Test(description = "should add given scanner to map ")
    public void putScannerFirstTime() {

        Scanner scanner = new Scanner() {
            @Override
            public Set<Class<?>> classes() {
                return null;
            }

            @Override
            public boolean getPrettyPrint() {
                return false;
            }

            @Override
            public void setPrettyPrint(boolean shouldPrettyPrint) {

            }
        };

        SwaggerScannerLocator. getInstance().putScanner(id, scanner);
        assertEquals(SwaggerScannerLocator.getInstance().getScanner(id), scanner);

    }

    @Test(description = "shouldn't add given scanner to map because already set")
    public void putConfigSecondTime() {

        putScannerFirstTime();

        Scanner scanner = new Scanner() {
            @Override
            public Set<Class<?>> classes() {
                return null;
            }

            @Override
            public boolean getPrettyPrint() {
                return false;
            }

            @Override
            public void setPrettyPrint(boolean shouldPrettyPrint) {

            }
        };

        SwaggerScannerLocator. getInstance().putScanner(id, scanner);
        assertNotEquals(SwaggerScannerLocator.getInstance().getScanner(id), scanner);

    }
}

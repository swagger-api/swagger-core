package io.swagger.servlet.extensions;

import io.swagger.models.Operation;
import io.swagger.servlet.resources.ResourceWithAnnotations;

import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

public class DeprecatedMethodTest extends BaseServletReaderExtensionTest {

    @DataProvider
    private Object[][] resources() {
        return new Object[][]{
                {"testMethod1", null},
                {"testMethod2", true}
        };
    }

    @Test(dataProvider = "resources")
    public void setDeprecatedTest(String methodName, Boolean expected) throws NoSuchMethodException {
        final Operation operation = new Operation();
        extension.setDeprecated(operation, ResourceWithAnnotations.class.getMethod(methodName));

        Assert.assertEquals(operation.isDeprecated(), expected);
    }
}

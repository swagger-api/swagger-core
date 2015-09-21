package io.swagger.servlet.extensions;

import io.swagger.models.Operation;
import io.swagger.servlet.resources.ResourceWithAnnotations;

import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

public class OperationIdTest extends BaseServletReaderExtensionTest {

    @DataProvider
    private Object[][] resourceWithNickname() {
        return new Object[][]{
                {"testMethod1", "testMethod1"},
                {"testMethod2", "testMethod2"},
                {"testMethod3", "users"},
                {"testMethod4", "testMethod4"},
        };
    }

    @Test(dataProvider = "resourceWithNickname")
    public void applyOperationIdTest(String methodName, String expected) throws NoSuchMethodException {
        final Operation operation = new Operation();
        extension.applyOperationId(operation, ResourceWithAnnotations.class.getMethod(methodName));

        Assert.assertEquals(operation.getOperationId(), expected);
    }
}

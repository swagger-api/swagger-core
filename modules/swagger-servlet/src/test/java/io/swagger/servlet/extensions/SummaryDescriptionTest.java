package io.swagger.servlet.extensions;

import io.swagger.models.Operation;
import io.swagger.servlet.resources.ResourceWithAnnotations;

import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

public class SummaryDescriptionTest extends BaseServletReaderExtensionTest {

    @DataProvider
    private Object[][] summaryResources() {
        return new Object[][]{
                {"testMethod1", null},
                {"testMethod2", "Test summary"},
                {"testMethod3", "Test summary"},
                {"testMethod4", null}
        };
    }

    @DataProvider
    private Object[][] descriptionResources() {
        return new Object[][]{
                {"testMethod1", null},
                {"testMethod2", null},
                {"testMethod3", "Test description"},
                {"testMethod4", null},
        };
    }

    @Test(dataProvider = "summaryResources")
    public void applySummaryTest(String methodName, String expected) throws NoSuchMethodException {
        final Operation operation = new Operation();
        extension.applySummary(operation, ResourceWithAnnotations.class.getMethod(methodName));

        Assert.assertEquals(operation.getSummary(), expected);
    }

    @Test(dataProvider = "descriptionResources")
    public void applyDescriptionTest(String methodName, String expected) throws NoSuchMethodException {
        final Operation operation = new Operation();
        extension.applyDescription(operation, ResourceWithAnnotations.class.getMethod(methodName));

        Assert.assertEquals(operation.getDescription(), expected);
    }
}

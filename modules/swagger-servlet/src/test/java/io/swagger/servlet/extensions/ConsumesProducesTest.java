package io.swagger.servlet.extensions;

import io.swagger.models.Operation;
import io.swagger.servlet.ReaderContext;

import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class ConsumesProducesTest extends BaseServletReaderExtensionTest {

    @DataProvider
    private Object[][] resourceWithAnnotations() {
        return new Object[][]{
                {"testMethod1", Arrays.asList("application/json", "application/xml")},
                {"testMethod2", Arrays.asList("application/json", "application/xml")},
                {"testMethod3", Collections.singletonList("multipart/form-data")},
                {"testMethod4", Arrays.asList("application/json", "application/xml")}
        };
    }

    @DataProvider
    private Object[][] resourceWithoutApiAnnotation() {
        return new Object[][]{
                {"testMethod1", null},
                {"testMethod2", null},
                {"testMethod3", Collections.singletonList("multipart/form-data")},
                {"testMethod4", null}
        };
    }

    @Test(dataProvider = "resourceWithAnnotations")
    public void applyConsumesProducesTest1(String methodName, List<String> expected) throws NoSuchMethodException {
        final Operation operation = new Operation();
        final ReaderContext context = createDefaultContext();
        final Method method = findMethod(context, methodName);
        extension.applyConsumes(context, operation, method);
        extension.applyProduces(context, operation, method);

        Assert.assertEquals(operation.getConsumes(), expected);
        Assert.assertEquals(operation.getProduces(), expected);
    }

    @Test(dataProvider = "resourceWithoutApiAnnotation")
    public void applyConsumesProducesTest2(String methodName, List<String> expected) throws NoSuchMethodException {
        final Operation operation = new Operation();
        final ReaderContext context = createDefaultContextWithoutApi();
        final Method method = findMethod(context, methodName);
        extension.applyConsumes(context, operation, method);
        extension.applyProduces(context, operation, method);

        Assert.assertEquals(operation.getConsumes(), expected);
        Assert.assertEquals(operation.getProduces(), expected);
    }
}

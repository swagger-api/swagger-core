package io.swagger.servlet.extensions;

import io.swagger.models.Operation;
import io.swagger.models.Scheme;
import io.swagger.servlet.ReaderContext;

import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class SchemesTest extends BaseServletReaderExtensionTest {

    @DataProvider
    private Object[][] resourceWithAnnotations() {
        return new Object[][]{
                {"testMethod1", Collections.singletonList(Scheme.HTTP)},
                {"testMethod2", Collections.singletonList(Scheme.HTTP)},
                {"testMethod3", Arrays.asList(Scheme.WS, Scheme.WSS)},
                {"testMethod4", Collections.singletonList(Scheme.HTTP)},
        };
    }

    @DataProvider
    private Object[][] resourceWithoutApiAnnotation() {
        return new Object[][]{
                {"testMethod1", null},
                {"testMethod2", null},
                {"testMethod3", Arrays.asList(Scheme.HTTP, Scheme.HTTPS)},
                {"testMethod4", null},
        };
    }

    @Test(dataProvider = "resourceWithAnnotations")
    public void applySchemesTest(String methodName, List<Scheme> expected) throws NoSuchMethodException {
        final Operation operation = new Operation();
        final ReaderContext context = createDefaultContext();
        extension.applySchemes(context, operation, findMethod(context, methodName));

        Assert.assertEquals(operation.getSchemes(), expected);
    }

    @Test(dataProvider = "resourceWithoutApiAnnotation")
    public void applySchemesWithoutApiTest(String methodName, List<Scheme> expected) throws NoSuchMethodException {
        final Operation operation = new Operation();
        final ReaderContext context = createDefaultContextWithoutApi();
        extension.applySchemes(context, operation, findMethod(context, methodName));

        Assert.assertEquals(operation.getSchemes(), expected);
    }
}

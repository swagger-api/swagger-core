package io.swagger.servlet.extensions;

import io.swagger.servlet.ReaderContext;

import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

public class PathGetterTest extends BaseServletReaderExtensionTest {

    @DataProvider
    private Object[][] resourceWithAnnotations() {
        return new Object[][]{
                {"testMethod1", "/tests/resources/testMethod1"},
                {"testMethod2", "/tests/resources/testMethod2"},
                {"testMethod3", "/tests/resources/testMethod3"},
                {"testMethod4", "/tests/resources/testMethod4"},
        };
    }

    @DataProvider
    private Object[][] resourceWithoutApiAnnotation() {
        return new Object[][]{
                {"testMethod1", "/tests/testMethod1"},
                {"testMethod2", "/tests/testMethod2"},
                {"testMethod3", "/tests/testMethod3"},
                {"testMethod4", "/tests/testMethod4"},
        };
    }

    @Test(dataProvider = "resourceWithAnnotations")
    public void getPathTest(String methodName, String expected) throws NoSuchMethodException {
        final ReaderContext context = createDefaultContext();
        context.setParentPath("/tests");

        Assert.assertEquals(extension.getPath(context, findMethod(context, methodName)), expected);
    }

    @Test(dataProvider = "resourceWithoutApiAnnotation")
    public void getPathWithoutApiTest(String methodName, String expected) throws NoSuchMethodException {
        final ReaderContext context = createDefaultContextWithoutApi();
        context.setParentPath("/tests");

        Assert.assertEquals(extension.getPath(context, findMethod(context, methodName)), expected);
    }
}

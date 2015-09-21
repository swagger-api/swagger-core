package io.swagger.servlet.extensions;

import io.swagger.servlet.ReaderContext;

import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

public class HttpMethodGetterTest extends BaseServletReaderExtensionTest {

    @DataProvider
    private Object[][] resources() {
        return new Object[][]{
                {"testMethod1", null},
                {"testMethod2", null},
                {"testMethod3", "GET"},
                {"testMethod4", null},
        };
    }

    @Test(dataProvider = "resources")
    public void getHttpMethodTest(String methodName, String expected) throws NoSuchMethodException {
        final ReaderContext context = createDefaultContext();

        Assert.assertEquals(extension.getHttpMethod(context, findMethod(context, methodName)), expected);
    }
}

package io.swagger.servlet.extensions;

import io.swagger.servlet.ReaderContext;
import io.swagger.servlet.resources.HiddenResource;
import io.swagger.servlet.resources.ResourceWithAnnotations;
import io.swagger.servlet.resources.ResourceWithoutApiAnnotation;

import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

public class CheckReadableApiTest extends BaseServletReaderExtensionTest {

    private static ReaderContext createContext(Class<?> cls, boolean readHidden) {
        final ReaderContext context = createContext(cls);
        context.setReadHidden(readHidden);
        return context;
    }

    @DataProvider
    private Object[][] resources() {
        return new Object[][]{
                {createContext(ResourceWithAnnotations.class, false), true},
                {createContext(ResourceWithAnnotations.class, true), true},
                {createContext(ResourceWithoutApiAnnotation.class, false), false},
                {createContext(ResourceWithoutApiAnnotation.class, true), false},
                {createContext(HiddenResource.class, false), false},
                {createContext(HiddenResource.class, true), true}
        };
    }

    @Test(dataProvider = "resources")
    public void isReadableTest(ReaderContext context, boolean expected) {
        Assert.assertEquals(extension.isReadable(context), expected);
    }
}

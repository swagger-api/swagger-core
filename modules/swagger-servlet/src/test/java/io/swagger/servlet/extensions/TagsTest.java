package io.swagger.servlet.extensions;

import io.swagger.models.Operation;
import io.swagger.servlet.ReaderContext;

import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.util.Arrays;
import java.util.List;

public class TagsTest extends BaseServletReaderExtensionTest {

    @DataProvider
    private Object[][] resourceWithAnnotations() {
        return new Object[][]{
                {"testMethod1", Arrays.asList("api_tag1", "api_tag2")},
                {"testMethod2", Arrays.asList("api_tag1", "api_tag2")},
                {"testMethod3", Arrays.asList("api_tag1", "api_tag2", "operation_tag1", "operation_tag2")},
                {"testMethod4", Arrays.asList("api_tag1", "api_tag2")}
        };
    }

    @DataProvider
    private Object[][] resourceWithoutApiAnnotation() {
        return new Object[][]{
                {"testMethod1", null},
                {"testMethod2", null},
                {"testMethod3", Arrays.asList("operation_tag1", "operation_tag2")},
                {"testMethod4", null}
        };
    }

    @Test(dataProvider = "resourceWithAnnotations")
    public void applyTagsTest1(String methodName, List<String> expected) throws NoSuchMethodException {
        final Operation operation = new Operation();
        final ReaderContext context = createDefaultContext();
        extension.applyTags(context, operation, findMethod(context, methodName));

        Assert.assertEquals(operation.getTags(), expected);
    }

    @Test(dataProvider = "resourceWithoutApiAnnotation")
    public void applyTagsTest2(String methodName, List<String> expected) throws NoSuchMethodException {
        final Operation operation = new Operation();
        final ReaderContext context = createDefaultContextWithoutApi();
        extension.applyTags(context, operation, findMethod(context, methodName));

        Assert.assertEquals(operation.getTags(), expected);
    }
}

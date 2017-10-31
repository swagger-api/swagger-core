package io.swagger.v3.core.resolving;

import com.google.common.collect.ImmutableMap;
import io.swagger.v3.core.util.BaseReaderUtils;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.extensions.Extension;
import io.swagger.v3.oas.annotations.extensions.ExtensionProperty;
import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.lang.reflect.Method;
import java.util.Collections;
import java.util.Map;

public class BaseReaderUtilsTest {

    @DataProvider
    private Object[][] expectedData() {
        return new Object[][]{
                {"methodOne", Collections.emptyMap()},
                {"methodTwo", Collections.emptyMap()},
                {"methodThree", ImmutableMap.of(
                        "x-test1", "value1",
                        "x-test2", "value2",
                        "x-test", ImmutableMap.of("test1", "value1", "test2", "value2"))},
                {"methodFour", ImmutableMap.of(
                        "x-test", ImmutableMap.of("test1", "value1", "test2", "value2"),
                        "x-test1", "value1",
                        "x-test2", "value2")},
                {"methodFive", ImmutableMap.of(
                        "x-test1", ImmutableMap.of("test1", "value1", "test2", "value2"),
                        "x-test2", "value2")},
                {"methodSix", ImmutableMap.of("x-test1", "value1", "x-test2", "value2")}
        };
    }

    @Test(dataProvider = "expectedData")
    public void extensionsTest(String methodName, Map<String, Object> expected) throws NoSuchMethodException {
        final Method method = getClass().getDeclaredMethod(methodName);
        final Extension[] extensions = method.getAnnotation(Operation.class).extensions();
        final Map<String, Object> map = BaseReaderUtils.parseExtensions(extensions);

        Assert.assertEquals(map, expected);
    }

    @Operation(description = "method")
    private void methodOne() {

    }

    @Operation(description = "method", extensions = {
            @Extension(name = "test", properties = {
                    @ExtensionProperty(name = "test1", value = "")
            })})
    private void methodTwo() {

    }

    @Operation(description = "method", extensions = {
            @Extension(properties = {
                    @ExtensionProperty(name = "test1", value = "value1"),
                    @ExtensionProperty(name = "test2", value = "value2")
            }),
            @Extension(name = "test", properties = {
                    @ExtensionProperty(name = "test1", value = "value1"),
                    @ExtensionProperty(name = "test2", value = "value2")
            })})
    private void methodThree() {

    }

    @Operation(description = "method", extensions = {
            @Extension(name = "test", properties = {
                    @ExtensionProperty(name = "test1", value = "value1"),
                    @ExtensionProperty(name = "test2", value = "value2")
            }),
            @Extension(properties = {
                    @ExtensionProperty(name = "test1", value = "value1"),
                    @ExtensionProperty(name = "test2", value = "value2")
            })
    })
    private void methodFour() {

    }

    @Operation(description = "method", extensions = {
            @Extension(properties = {
                    @ExtensionProperty(name = "test1", value = "value1"),
                    @ExtensionProperty(name = "test2", value = "value2")
            }),
            @Extension(name = "test1", properties = {
                    @ExtensionProperty(name = "test1", value = "value1"),
                    @ExtensionProperty(name = "test2", value = "value2")
            })
    })
    private void methodFive() {

    }

    @Operation(description = "method", extensions = {
            @Extension(name = "test1", properties = {
                    @ExtensionProperty(name = "test1", value = "value1"),
                    @ExtensionProperty(name = "test2", value = "value2")
            }),
            @Extension(properties = {
                    @ExtensionProperty(name = "test1", value = "value1"),
                    @ExtensionProperty(name = "test2", value = "value2")
            })
    })
    private void methodSix() {

    }
}

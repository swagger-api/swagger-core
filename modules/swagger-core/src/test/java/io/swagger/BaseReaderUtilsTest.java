package io.swagger;

import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.BooleanNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.Extension;
import io.swagger.annotations.ExtensionProperty;
import io.swagger.util.BaseReaderUtils;

import com.google.common.collect.ImmutableMap;
import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.lang.reflect.Method;
import java.util.Collections;
import java.util.Map;

public class BaseReaderUtilsTest {

    @DataProvider
    private Object[][] expectedData() {
        ArrayNode testJsonArray =  JsonNodeFactory.instance.arrayNode().add("11").add("22");
        BooleanNode testJsonBoolean = JsonNodeFactory.instance.booleanNode(true);

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
                {"methodSix", ImmutableMap.of("x-test1", "value1", "x-test2", "value2")},
                {"methodSeven", ImmutableMap.of("x-test1", testJsonArray)},
                {"methodEight", ImmutableMap.of("x-test1", testJsonBoolean)}
        };
    }

    @Test(dataProvider = "expectedData")
    public void extensionsTest(String methodName, Map<String, Object> expected) throws NoSuchMethodException {
        final Method method = getClass().getDeclaredMethod(methodName);
        final Extension[] extensions = method.getAnnotation(ApiOperation.class).extensions();
        final Map<String, Object> map = BaseReaderUtils.parseExtensions(extensions);

        Assert.assertEquals(map, expected);
    }

    @ApiOperation(value = "method")
    private void methodOne() {

    }

    @ApiOperation(value = "method", extensions = {
            @Extension(name = "test", properties = {
                    @ExtensionProperty(name = "test1", value = "")
            })})
    private void methodTwo() {

    }

    @ApiOperation(value = "method", extensions = {
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

    @ApiOperation(value = "method", extensions = {
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

    @ApiOperation(value = "method", extensions = {
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

    @ApiOperation(value = "method", extensions = {
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

    @ApiOperation(value = "method", extensions = {
            @Extension(properties = {
                    @ExtensionProperty(name = "test1", value = "[\"11\", \"22\"]", parseValue = true)
            })})
    private void methodSeven() {

    }

    @ApiOperation(value = "method", extensions = {
            @Extension(properties = {
                    @ExtensionProperty(name = "test1", value = "true", parseValue = true)
            })})
    private void methodEight() {

    }
}

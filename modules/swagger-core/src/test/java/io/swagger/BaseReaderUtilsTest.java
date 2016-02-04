package io.swagger;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.Extension;
import io.swagger.annotations.ExtensionProperty;
import io.swagger.util.BaseReaderUtils;

import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.lang.reflect.Method;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;


public class BaseReaderUtilsTest {

    @DataProvider
    private Object[][] expectedData() {
        return new Object[][]{
                {"methodOne", Collections.emptyMap()},
                {"methodTwo", Collections.emptyMap()},
                {"methodThree", new HashMap<String, Object>() {{
                        put("x-test1", "value1");
                        put("x-test2", "value2");
                        put("x-test", new HashMap<String, String>() {{ put("test1", "value1"); put("test2", "value2");}});}}},
                {"methodFour", new HashMap<String, Object>() {{
                        put("x-test", new HashMap<String, String>() {{ put("test1", "value1"); put("test2", "value2");}});
                        put("x-test1", "value1");
                        put("x-test2", "value2");}}},
                {"methodFive", new HashMap<String, Object>() {{
                        put("x-test1", new HashMap<String, Object>() {{ put("test1", "value1"); put("test2", "value2");}});
                        put("x-test2", "value2");}}},
                {"methodSix",  new HashMap<String, Object>() {{put("x-test1", "value1");put("x-test2", "value2");}}}
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
}

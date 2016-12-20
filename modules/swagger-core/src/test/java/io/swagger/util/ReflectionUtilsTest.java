package io.swagger.util;

import static org.testng.Assert.assertNull;

import java.lang.reflect.Method;

import org.testng.annotations.Test;

public class ReflectionUtilsTest {

    @Test
    public void testFindMethodForNullClass() throws Exception {
        Method method = ReflectionUtilsTest.class.getMethod("testFindMethodForNullClass", (Class<?>[]) null);
        assertNull(ReflectionUtils.findMethod(method, null));
    }
}

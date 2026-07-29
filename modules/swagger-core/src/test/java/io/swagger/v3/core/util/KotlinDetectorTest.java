package io.swagger.v3.core.util;

import org.testng.annotations.Test;

import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;
public class KotlinDetectorTest {

    @Test
    public void isKotlinClassNull() {
        assertFalse(KotlinDetector.isKotlinClass(null));
    }

    @Test
    public void isKotlinClassJavaClass() {
        assertFalse(KotlinDetector.isKotlinClass(String.class));
    }

    @Test
    public void isKotlinClassPrimitive() {
        assertFalse(KotlinDetector.isKotlinClass(int.class));
    }

    @Test
    public void isKotlinClassKotlinAnnotatedClass() {
        assertTrue(KotlinDetector.isKotlinClass(KotlinAnnotatedClass.class));
    }
}

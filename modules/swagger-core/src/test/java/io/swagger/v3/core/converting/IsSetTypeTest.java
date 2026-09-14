package io.swagger.v3.core.converting;

import io.swagger.v3.core.jackson.AbstractModelConverter;
import io.swagger.v3.core.util.Json;
import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;

public class IsSetTypeTest {

    private static class TestModelConverter extends AbstractModelConverter {
        protected TestModelConverter() {
            super(Json.mapper());
        }

        public boolean isSetType(Class<?> cls) {
            return _isSetType(cls);
        }
    }

    private final TestModelConverter converter = new TestModelConverter();

    @Test
    public void testIsSetTypeWithNull() {
        assertFalse(converter.isSetType(null));
    }

    @Test
    public void testIsSetTypeWithSetInterface() {
        assertTrue(converter.isSetType(Set.class));
    }

    @Test
    public void testIsSetTypeWithHashSet() {
        assertTrue(converter.isSetType(HashSet.class));
    }

    @Test
    public void testIsSetTypeWithLinkedHashSet() {
        assertTrue(converter.isSetType(LinkedHashSet.class));
    }

    @Test
    public void testIsSetTypeWithTreeSet() {
        assertTrue(converter.isSetType(TreeSet.class));
    }

    @Test
    public void testIsSetTypeWithNonSetCollection() {
        assertFalse(converter.isSetType(List.class));
        assertFalse(converter.isSetType(ArrayList.class));
    }

    @Test
    public void testIsSetTypeWithNonCollection() {
        assertFalse(converter.isSetType(String.class));
        assertFalse(converter.isSetType(Integer.class));
        assertFalse(converter.isSetType(Map.class));
    }
}

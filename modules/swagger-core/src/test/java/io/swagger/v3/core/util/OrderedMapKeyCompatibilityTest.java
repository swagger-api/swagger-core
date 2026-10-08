package io.swagger.v3.core.util;

import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import tools.jackson.databind.ObjectMapper;
import tools.jackson.databind.SerializationFeature;
import tools.jackson.databind.exc.InvalidDefinitionException;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.function.Supplier;

import static org.testng.Assert.assertTrue;
import static org.testng.Assert.expectThrows;

public class OrderedMapKeyCompatibilityTest extends ObjectMapperFactoryTestBase {

    @BeforeMethod
    public void enableJackson2Compatibility() {
        ObjectMapperFactory.setJackson2Compatibility(true);
    }

    @Test(dataProvider = "publicMappers")
    public void enablesFailureForIncomparableOrderedMapKeys(
            String name, Supplier<ObjectMapper> supplier) {
        assertTrue(supplier.get().isEnabled(
                SerializationFeature.FAIL_ON_ORDER_MAP_BY_INCOMPARABLE_KEY), name);
    }

    @Test(dataProvider = "allMappers")
    public void sortsComparableStringKeys(String name, Supplier<ObjectMapper> supplier) {
        ObjectMapper mapper = withOrderedMapEntries(supplier);
        Map<String, String> values = new LinkedHashMap<>();
        values.put("z", "last");
        values.put("a", "first");

        String output = mapper.writeValueAsString(values);

        assertTrue(output.indexOf("a") < output.indexOf("z"), name + ": " + output);
    }

    @Test(dataProvider = "allMappers")
    public void rejectsIncomparableOrderedMapKeys(
            String name, Supplier<ObjectMapper> supplier) {
        ObjectMapper mapper = withOrderedMapEntries(supplier);
        Map<Object, String> values = new LinkedHashMap<>();
        values.put(new IncomparableKey("first"), "one");
        values.put("second", "two");

        InvalidDefinitionException error = expectThrows(InvalidDefinitionException.class,
                () -> mapper.writeValueAsString(values));

        assertTrue(error.getMessage().contains("Cannot order Map entries by key of incomparable type"),
                name + ": " + error.getMessage());
        assertTrue(error.getMessage().contains(
                        "SerializationFeature.FAIL_ON_ORDER_MAP_BY_INCOMPARABLE_KEY"),
                name + ": " + error.getMessage());
    }

    private static ObjectMapper withOrderedMapEntries(Supplier<ObjectMapper> supplier) {
        return supplier.get().rebuild()
                .enable(SerializationFeature.ORDER_MAP_ENTRIES_BY_KEYS)
                .build();
    }

    private static final class IncomparableKey {
        private final String value;

        private IncomparableKey(String value) {
            this.value = value;
        }

        @Override
        public String toString() {
            return value;
        }
    }
}

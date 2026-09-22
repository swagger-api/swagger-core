package io.swagger.v3.core.util;

import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import tools.jackson.core.json.JsonFactory;
import tools.jackson.databind.ObjectMapper;
import tools.jackson.databind.SerializationFeature;
import tools.jackson.databind.exc.InvalidDefinitionException;
import tools.jackson.dataformat.yaml.YAMLFactory;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.function.Supplier;

import static org.testng.Assert.assertTrue;
import static org.testng.Assert.expectThrows;

public class OrderedMapKeyCompatibilityTest {

    @DataProvider(name = "publicMappers")
    public Object[][] publicMappers() {
        return new Object[][] {
                mapper("Json.mapper", () -> Json.mapper().rebuild().build()),
                mapper("Json31.mapper", () -> Json31.mapper().rebuild().build()),
                mapper("Yaml.mapper", () -> Yaml.mapper().rebuild().build()),
                mapper("Yaml31.mapper", () -> Yaml31.mapper().rebuild().build()),
                mapper("createJson", ObjectMapperFactory::createJson),
                mapper("createJson31", ObjectMapperFactory::createJson31),
                mapper("createYaml", ObjectMapperFactory::createYaml),
                mapper("createYaml31", ObjectMapperFactory::createYaml31),
                mapper("createJson(factory)", () -> ObjectMapperFactory.createJson(new JsonFactory())),
                mapper("createJson31(factory)", () -> ObjectMapperFactory.createJson31(new JsonFactory())),
                mapper("createYaml(factory)", () -> ObjectMapperFactory.createYaml(new YAMLFactory())),
                mapper("createYaml31(factory)", () -> ObjectMapperFactory.createYaml31(new YAMLFactory())),
                mapper("createJsonConverter", ObjectMapperFactory::createJsonConverter),
                mapper("Json31.converterMapper", () -> Json31.converterMapper().rebuild().build()),
                mapper("create(JSON, false)", () -> ObjectMapperFactory.create(new JsonFactory(), false)),
                mapper("create(YAML, true)", () -> ObjectMapperFactory.create(new YAMLFactory(), true)),
                mapper("createYaml(false)", () -> ObjectMapperFactory.createYaml(false)),
                mapper("createYaml(true)", () -> ObjectMapperFactory.createYaml(true))
        };
    }

    private static Object[] mapper(String name, Supplier<ObjectMapper> mapper) {
        return new Object[] {name, mapper};
    }

    @Test(dataProvider = "publicMappers")
    public void enablesFailureForIncomparableOrderedMapKeys(
            String name, Supplier<ObjectMapper> supplier) {
        assertTrue(supplier.get().isEnabled(
                SerializationFeature.FAIL_ON_ORDER_MAP_BY_INCOMPARABLE_KEY), name);
    }

    @Test(dataProvider = "publicMappers")
    public void sortsComparableStringKeys(String name, Supplier<ObjectMapper> supplier) {
        ObjectMapper mapper = withOrderedMapEntries(supplier);
        Map<String, String> values = new LinkedHashMap<>();
        values.put("z", "last");
        values.put("a", "first");

        String output = mapper.writeValueAsString(values);

        assertTrue(output.indexOf("a") < output.indexOf("z"), name + ": " + output);
    }

    @Test(dataProvider = "publicMappers")
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

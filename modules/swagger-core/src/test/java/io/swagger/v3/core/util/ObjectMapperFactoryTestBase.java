package io.swagger.v3.core.util;

import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import tools.jackson.core.json.JsonFactory;
import tools.jackson.dataformat.yaml.YAMLFactory;
import tools.jackson.databind.ObjectMapper;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;
import java.util.stream.Stream;

public abstract class ObjectMapperFactoryTestBase {

    protected static final String STRICT = "buildStrictGenericObjectMapper";

    @BeforeMethod
    public void resetFactoryStateBeforeTest() {
        resetFactoryState();
    }

    @AfterMethod(alwaysRun = true)
    public void resetFactoryStateAfterTest() {
        resetFactoryState();
    }

    private static void resetFactoryState() {
        ObjectMapperFactory.clearCustomizers();
        ObjectMapperFactory.setJackson2Compatibility(false);
        Json.reset();
        Json31.reset();
        Yaml.reset();
        Yaml31.reset();
    }

    /** Public mappers (JSON and YAML) followed by the strict generic mapper. */
    @DataProvider(name = "allMappers")
    public Object[][] allMappers() {
        return Stream.concat(Arrays.stream(publicMappers()), Arrays.stream(strictMapper()))
                .toArray(Object[][]::new);
    }

    @DataProvider(name = "publicMappers")
    public Object[][] publicMappers() {
        return Stream.concat(Arrays.stream(jsonMappers()), Arrays.stream(yamlMappers()))
                .toArray(Object[][]::new);
    }

    @DataProvider(name = "jsonMappers")
    public Object[][] jsonMappers() {
        return new Object[][] {
                mapper("Json.mapper", () -> Json.mapper().rebuild().build()),
                mapper("Json31.mapper", () -> Json31.mapper().rebuild().build()),
                mapper("createJson", ObjectMapperFactory::createJson),
                mapper("createJson31", ObjectMapperFactory::createJson31),
                mapper("createJson(factory)", () -> ObjectMapperFactory.createJson(new JsonFactory())),
                mapper("createJson31(factory)", () -> ObjectMapperFactory.createJson31(new JsonFactory())),
                mapper("createJsonConverter", ObjectMapperFactory::createJsonConverter),
                mapper("Json31.converterMapper", () -> Json31.converterMapper().rebuild().build()),
                mapper("create(JSON, false)", () -> ObjectMapperFactory.create(new JsonFactory(), false))
        };
    }

    @DataProvider(name = "yamlMappers")
    public Object[][] yamlMappers() {
        return new Object[][] {
                mapper("Yaml.mapper", () -> Yaml.mapper().rebuild().build()),
                mapper("Yaml31.mapper", () -> Yaml31.mapper().rebuild().build()),
                mapper("createYaml", ObjectMapperFactory::createYaml),
                mapper("createYaml31", ObjectMapperFactory::createYaml31),
                mapper("createYaml(factory)", () -> ObjectMapperFactory.createYaml(new YAMLFactory())),
                mapper("createYaml31(factory)", () -> ObjectMapperFactory.createYaml31(new YAMLFactory())),
                mapper("create(YAML, true)", () -> ObjectMapperFactory.create(new YAMLFactory(), true)),
                mapper("createYaml(false)", () -> ObjectMapperFactory.createYaml(false)),
                mapper("createYaml(true)", () -> ObjectMapperFactory.createYaml(true))
        };
    }

    @DataProvider(name = "strictMapper")
    public Object[][] strictMapper() {
        return new Object[][] {
                mapper(STRICT, ObjectMapperFactory::buildStrictGenericObjectMapper)
        };
    }

    /** Every mapper in {@code allMappers}, once with the Jackson 2 switch off and once with it on. */
    @DataProvider(name = "allMappersInBothModes")
    public Object[][] allMappersInBothModes() {
        return inBothModes(allMappers());
    }

    @DataProvider(name = "publicMappersInBothModes")
    public Object[][] publicMappersInBothModes() {
        return inBothModes(publicMappers());
    }

    @DataProvider(name = "strictMapperInBothModes")
    public Object[][] strictMapperInBothModes() {
        return inBothModes(strictMapper());
    }

    private static Object[][] inBothModes(Object[][] mappers) {
        return Stream.of(false, true)
                .flatMap(compat -> Arrays.stream(mappers)
                        .map(m -> new Object[] {compat, m[0], m[1]}))
                .toArray(Object[][]::new);
    }

    /** Sets the switch, then builds the mapper, so the mapper reflects the requested mode. */
    protected static ObjectMapper build(boolean jackson2Compatibility, Supplier<ObjectMapper> supplier) {
        ObjectMapperFactory.setJackson2Compatibility(jackson2Compatibility);
        return supplier.get();
    }

    protected static String label(boolean jackson2Compatibility, String name) {
        return (jackson2Compatibility ? "opt-in " : "default ") + name;
    }

    protected static Object[] mapper(String name, Supplier<ObjectMapper> mapper) {
        return new Object[] {name, mapper};
    }

    protected static String input(ObjectMapper mapper, String json, String yaml) {
        return mapper.tokenStreamFactory() instanceof YAMLFactory ? yaml : json;
    }

    public static class KnownProperty {
        public String known;
    }

    public static class EmptyBean {
    }

    public enum WireEnum {
        VALUE;

        @Override
        public String toString() {
            return "wire-value";
        }
    }

    public static class WireEnumContainer {
        public WireEnum value;
    }

    public static class OrderedBean {
        public String beta = "b";
        public String alpha = "a";
    }

    public static class ResolverAccessorBean {
        private String value;
        private String _value;

        public String getvalue() {
            return value;
        }

        public String get_value() {
            return _value;
        }
    }

    public static class Jackson2AccessorBean {
        private String lowerCaseValue;
        private String nonLetterValue;

        public String getvalue() {
            return lowerCaseValue;
        }

        public void setvalue(String value) {
            lowerCaseValue = value;
        }

        public String get_value() {
            return nonLetterValue;
        }

        public void set_value(String value) {
            nonLetterValue = value;
        }
    }

    public static class AcronymGetterBean {
        public String getURL() {
            return "u";
        }

        public String getIPhone() {
            return "i";
        }
    }

    public static class AcronymIsBean {
        public boolean isURL() {
            return true;
        }
    }

    public static class NullContainer {
        public String missing;
        public Map<String, String> map = new LinkedHashMap<>();
        public List<String> list = new ArrayList<>();

        public NullContainer() {
            map.put("present", "value");
            map.put("missing", null);
            list.add(null);
            list.add("value");
        }
    }

    public static class PrimitiveBean {
        public int count = 9;
        public boolean active = true;
        public Integer boxedCount;
        public Boolean boxedActive;
    }
}

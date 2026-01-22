package io.swagger.v3.core.util;

import tools.jackson.core.util.DefaultPrettyPrinter;
import tools.jackson.databind.ObjectMapper;
import tools.jackson.databind.ObjectWriter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tools.jackson.databind.cfg.MapperBuilder;
import tools.jackson.dataformat.yaml.YAMLFactoryBuilder;

import java.util.function.Consumer;

public class Yaml {

    private static final class ObjectMapperHolder {
        private static final ObjectMapper MAPPER = ObjectMapperFactory.createYaml();
    }

    private static final Logger LOGGER = LoggerFactory.getLogger(Yaml.class);

    public static ObjectMapper mapper() {
        return ObjectMapperHolder.MAPPER;
    }

    public static ObjectMapper mapper(Consumer<MapperBuilder<? extends ObjectMapper, ? extends MapperBuilder<?, ?>>> mapperBuilderCustomizer) {
        return ObjectMapperFactory.createYaml(mapperBuilderCustomizer);
    }

    public static ObjectWriter pretty() {
        return mapper().writerWithDefaultPrettyPrinter();
    }

    public static String pretty(Object o) {
        try {
            return pretty().writeValueAsString(o);
        } catch (Exception e) {
            PrettyPrintHelper.emitError(LOGGER, "Error serializing object to YAML", e);
            return null;
        }
    }

    public static void prettyPrint(Object o) {
        try {
            String prettyString = pretty().writeValueAsString(o);
            PrettyPrintHelper.emit(LOGGER, prettyString);
        } catch (Exception e) {
            PrettyPrintHelper.emitError(LOGGER, "Error pretty-printing YAML", e);
        }
    }
}

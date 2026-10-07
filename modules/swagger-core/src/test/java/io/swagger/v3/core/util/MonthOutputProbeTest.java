package io.swagger.v3.core.util;

import org.testng.annotations.Test;
import tools.jackson.databind.ObjectMapper;

import java.time.Month;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Probe that prints how the default Swagger Core mappers write and read {@link Month}.
 * It documents the current behavior and does not assert it. Run it with
 * {@code -Dtest=MonthOutputProbeTest} and read the standard output.
 */
public class MonthOutputProbeTest {

    public static class MonthBean {
        public Month month = Month.JANUARY;
    }

    @Test
    public void printMonthOutput() {
        Map<String, ObjectMapper> mappers = new LinkedHashMap<>();
        mappers.put("Json.mapper", Json.mapper());
        mappers.put("Yaml.mapper", Yaml.mapper());
        mappers.put("Json31.mapper", Json31.mapper());
        mappers.put("Yaml31.mapper", Yaml31.mapper());
        mappers.put("createJsonConverter", ObjectMapperFactory.createJsonConverter());
        mappers.put("buildStrictGenericObjectMapper", ObjectMapperFactory.buildStrictGenericObjectMapper());

        mappers.forEach((name, mapper) -> {
            System.out.println("== " + name);
            print("write Month.JANUARY", () -> mapper.writeValueAsString(Month.JANUARY));
            print("write Month.DECEMBER", () -> mapper.writeValueAsString(Month.DECEMBER));
            print("write bean with Month", () -> mapper.writeValueAsString(new MonthBean()));
            print("read \"JANUARY\"", () -> String.valueOf(mapper.readValue("\"JANUARY\"", Month.class)));
            print("read 0", () -> String.valueOf(mapper.readValue("0", Month.class)));
            print("read 1", () -> String.valueOf(mapper.readValue("1", Month.class)));
            print("read 11", () -> String.valueOf(mapper.readValue("11", Month.class)));
            print("read 12", () -> String.valueOf(mapper.readValue("12", Month.class)));
        });
    }

    private static void print(String label, ThrowingSupplier supplier) {
        String result;
        try {
            result = supplier.get().replace("\n", "\\n");
        } catch (RuntimeException e) {
            result = "FAILED: " + e.getClass().getSimpleName() + ": " + firstLine(e.getMessage());
        }
        System.out.println("   " + label + " -> " + result);
    }

    private static String firstLine(String message) {
        if (message == null) {
            return "";
        }
        int end = message.indexOf('\n');
        return end < 0 ? message : message.substring(0, end);
    }

    @FunctionalInterface
    private interface ThrowingSupplier {
        String get();
    }
}

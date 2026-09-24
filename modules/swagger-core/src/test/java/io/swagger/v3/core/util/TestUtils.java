package io.swagger.v3.core.util;

import tools.jackson.core.JacksonException;
import tools.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.io.InputStream;

/**
 * Created by russellb337 on 7/31/15.
 */
public class TestUtils {

    private static <T> T deserializeFileFromClasspath(String path, Class<T> type, ObjectMapper objectMapper) {
        try (InputStream resource = TestUtils.class.getClassLoader().getResourceAsStream(path)) {
            if (resource == null) {
                throw new RuntimeException("Could not find file on the classpath: " + path);
            }

            String contents = new String(resource.readAllBytes());
            try {
                return objectMapper.readValue(contents, type);
            } catch (JacksonException e) {
                throw new RuntimeException("Could not deserialize contents into type: " + type, e);
            }
        } catch (IOException e) {
            throw new RuntimeException("could not read from file " + path, e);
        }
    }

    public static <T> T deserializeJsonFileFromClasspath(String path, Class<T> type) {
        return deserializeFileFromClasspath(path, type, Json.mapper());
    }

    public static <T> T deserializeYamlFileFromClasspath(String path, Class<T> type) {
        return deserializeFileFromClasspath(path, type, Yaml.mapper());
    }

    public static String normalizeLineEnds(String s) {
        return s.replace("\r\n", "\n").replace('\r', '\n');
    }
}

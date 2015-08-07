package io.swagger;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.util.Json;
import io.swagger.util.Yaml;
import org.apache.commons.io.IOUtils;

import java.io.IOException;
import java.io.InputStream;

/**
 * Created by russellb337 on 7/31/15.
 */
public class TestUtils {

    private static <T> T deserializeFileFromClasspath(String path, Class<T> type, ObjectMapper objectMapper) {
        final InputStream resource = TestUtils.class.getClassLoader().getResourceAsStream(path);

        String contents;

        if(resource == null) {
            throw new RuntimeException("Could not find file on the classpath: " + path);
        }

        try {
            contents= IOUtils.toString(resource);
        } catch (IOException e) {
            throw new RuntimeException("could not read from file " + path,  e);
        }

        try {
            T result = objectMapper.readValue(contents, type);
            return result;
        } catch (IOException e) {
            throw new RuntimeException("Could not deserialize contents into type: " + type, e);
        }
    }

    public static <T> T deserializeJsonFileFromClasspath(String path, Class<T> type) {
        return deserializeFileFromClasspath(path, type, Json.mapper());
    }

    public static <T> T deserializeYamlFileFromClasspath(String path, Class<T> type) {
        return deserializeFileFromClasspath(path, type, Yaml.mapper());
    }
}

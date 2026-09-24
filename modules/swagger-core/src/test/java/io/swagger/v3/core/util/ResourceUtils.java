package io.swagger.v3.core.util;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Objects;

public class ResourceUtils {

    public static String loadClassResource(Class<?> cls, String name) throws IOException {
        try (InputStream in = cls.getClassLoader().getResourceAsStream(name)) {
            return new String(Objects.requireNonNull(in).readAllBytes(), StandardCharsets.UTF_8);
        }
    }
}

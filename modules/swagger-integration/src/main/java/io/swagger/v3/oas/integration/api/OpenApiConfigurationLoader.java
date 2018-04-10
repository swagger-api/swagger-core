package io.swagger.v3.oas.integration.api;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public interface OpenApiConfigurationLoader {

    OpenAPIConfiguration load(String path) throws IOException;

    boolean exists(String path);

    default String readInputStreamToString(InputStream stream) throws IOException {
        StringBuilder sb = new StringBuilder();
        try (BufferedReader in = new BufferedReader(
                new InputStreamReader(stream))) {
            String inputLine;
            while ((inputLine = in.readLine()) != null) {
                sb.append(inputLine).append("\n");
            }
        }
        return sb.toString();
    }

}

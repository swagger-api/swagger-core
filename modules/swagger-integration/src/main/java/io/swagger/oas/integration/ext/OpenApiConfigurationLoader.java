package io.swagger.oas.integration.ext;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import io.swagger.oas.integration.OpenAPIConfiguration;

public interface OpenApiConfigurationLoader {

    OpenAPIConfiguration load(String path) throws IOException;

    boolean exists(String path);

    default String readInputStreamToString(InputStream stream) throws IOException {
        StringBuffer sb = new StringBuffer();
        BufferedReader in = new BufferedReader(
                new InputStreamReader(stream));

        String inputLine;
        while ((inputLine = in.readLine()) != null) {
            sb.append(inputLine).append("\n");
        }
        in.close();
        return sb.toString();
    }

}

package io.swagger.oas.integration.impl;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import io.swagger.oas.integration.OpenAPIConfiguration;

public class FileOpenApiConfigurationLoader implements StringOpenApiConfigurationLoader {

    @Override
    public OpenAPIConfiguration load(String path)  throws IOException {
        File file = new File(path);
        return deserializeConfig(path, readInputStreamToString(new FileInputStream(file)));
    }

    @Override
    public boolean exists(String path) {
        File file = new File(path);
        return file.exists();
    }
}

package io.swagger.oas.integration;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

public class FileOpenApiConfigurationLoader implements StringOpenApiConfigurationLoader {

    @Override
    public OpenApiConfiguration load(String path)  throws IOException {
        File file = new File(path);
        return deserializeConfig(path, readInputStreamToString(new FileInputStream(file)));
    }

    @Override
    public boolean exists(String path) {
        File file = new File(path);
        return file.exists();
    }
}

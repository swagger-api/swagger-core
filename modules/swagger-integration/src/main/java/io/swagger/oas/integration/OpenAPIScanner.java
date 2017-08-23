package io.swagger.oas.integration;

import java.util.Map;
import java.util.Set;

public interface OpenAPIScanner {
    void setConfiguration(OpenAPIConfiguration openAPIConfiguration);

    Set<Class<?>> getClasses();

    Map<String, Object> getResources();
}

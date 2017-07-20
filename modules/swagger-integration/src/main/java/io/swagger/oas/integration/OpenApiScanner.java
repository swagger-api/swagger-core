package io.swagger.oas.integration;

import java.util.Map;
import java.util.Set;

public interface OpenApiScanner {

    Set<Class<?>> classes();

    Map<String, Object> resources();
}

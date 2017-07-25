package io.swagger.oas.web;

import java.util.Map;
import java.util.Set;

public interface OASScanner {
    Set<Class<?>> classes();

    Map<String, Object> resources();
}

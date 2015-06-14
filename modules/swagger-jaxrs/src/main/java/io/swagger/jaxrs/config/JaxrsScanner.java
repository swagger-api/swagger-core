package io.swagger.jaxrs.config;

import io.swagger.config.Scanner;

import javax.servlet.ServletConfig;
import javax.ws.rs.core.Application;
import java.util.Set;

public interface JaxrsScanner extends Scanner {
    Set<Class<?>> classesFromContext(Application app, ServletConfig sc);
}
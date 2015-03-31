package com.wordnik.swagger.jaxrs.config;

import com.wordnik.swagger.config.Scanner;

import java.util.Set;

import javax.servlet.ServletConfig;
import javax.ws.rs.core.Application;

public interface JaxrsScanner extends Scanner {
  Set<Class<?>> classesFromContext(Application app, ServletConfig sc);
}
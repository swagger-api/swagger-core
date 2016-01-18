package io.swagger.jaxrs.config;

import io.swagger.config.Scanner;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class SwaggerScannerLocator {

    private static SwaggerScannerLocator instance;

    private ConcurrentMap<String, Scanner> map = new ConcurrentHashMap<String, Scanner>();

    public static SwaggerScannerLocator getInstance() {
        if (instance == null) {
            instance = new SwaggerScannerLocator();
        }
        return instance;
    }

    private SwaggerScannerLocator() {
    }

    public Scanner getScanner(String id) {
        return map.getOrDefault(id, new DefaultJaxrsScanner());
    }

    public void putScanner(String id, Scanner scanner) {
        map.putIfAbsent(id, scanner);
    }
}

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
        Scanner value = map.get(id);
        if (value != null) {
            return value;
        }
        return new DefaultJaxrsScanner();
    }

    public void putScanner(String id, Scanner scanner) {
        if (!map.containsKey(id)) map.put(id, scanner);
    }
}

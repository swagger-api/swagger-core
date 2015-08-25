package io.swagger.config;

import io.swagger.jaxrs.config.DefaultJaxrsScanner;

public class ScannerSingleton {
    private static Scanner SCANNER = new DefaultJaxrsScanner();

    public static Scanner getScanner() {
        return SCANNER;
    }

    public static void setScanner(Scanner scanner) {
        SCANNER = scanner;
    }
}
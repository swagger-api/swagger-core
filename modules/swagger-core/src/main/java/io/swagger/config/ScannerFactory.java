package io.swagger.config;

public class ScannerFactory {
    private static Scanner SCANNER;

    public static Scanner getScanner() {
        return SCANNER;
    }

    public static void setScanner(Scanner scanner) {
        SCANNER = scanner;
    }
}
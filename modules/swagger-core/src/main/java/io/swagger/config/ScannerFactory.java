package io.swagger.config;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
public class ScannerFactory {
    private static Scanner DEFAULT_SCANNER;
    private static Map<String, Map<String, Scanner>> SCANNERS = new ConcurrentHashMap<String, Map<String, Scanner>>();

    @Deprecated
    public static Scanner getScanner() {
        return DEFAULT_SCANNER;
    }

    @Deprecated
    public static void setScanner(Scanner scanner) {
        DEFAULT_SCANNER = scanner;
    }

    public static Scanner getScanner(String host, String basePath) {
        if (DEFAULT_SCANNER != null) {
            return DEFAULT_SCANNER;
        }

        final Map<String, Scanner> scannersForHost = SCANNERS.get(host);
        if (scannersForHost == null) {
            return null;
        }
        return scannersForHost.get(basePath);
    }

    public static synchronized void setScanner(Scanner scanner, String host, String basePath) {
        if (DEFAULT_SCANNER != null || host == null || basePath == null) {
            DEFAULT_SCANNER = scanner;
            return;
        }

        Map<String, Scanner> hostScanners = SCANNERS.get(host);
        if (hostScanners == null) {
            hostScanners = new ConcurrentHashMap<String, Scanner>();
            SCANNERS.put(host, hostScanners);
        }
        hostScanners.put(basePath, scanner);
    }

}
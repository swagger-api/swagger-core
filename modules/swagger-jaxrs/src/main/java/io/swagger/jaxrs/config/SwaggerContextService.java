package io.swagger.jaxrs.config;

import io.swagger.config.Scanner;
import io.swagger.config.ScannerFactory;
import io.swagger.config.SwaggerConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletConfig;

public class SwaggerContextService {

    Logger LOGGER = LoggerFactory.getLogger(SwaggerContextService.class);

    private ServletConfig sc;
    private String configId;
    private SwaggerConfig swaggerConfig;
    private String scannerId;
    private Scanner scanner;

    public void setScannerId(String scannerId) {
        this.scannerId = scannerId;
    }

    public void setScanner(Scanner scanner) {
        this.scanner = scanner;
    }

    public void setConfigId(String configId) {
        this.configId = configId;
    }

    public void setSwaggerConfig(SwaggerConfig swaggerConfig) {
        this.swaggerConfig = swaggerConfig;
    }

    public void setSc(ServletConfig sc) {
        this.sc = sc;
    }

    public SwaggerContextService withServletConfig(ServletConfig sc) {
        this.sc = sc;
        return this;
    }

    public SwaggerContextService withConfigId(String configId) {
        this.configId = configId;
        return this;
    }

    public SwaggerContextService withSwaggerConfig(SwaggerConfig swaggerConfig) {
        this.swaggerConfig = swaggerConfig;
        return this;
    }

    public SwaggerContextService withScannerId(String scannerId) {
        this.scannerId = scannerId;
        return this;
    }

    public SwaggerContextService withScanner(Scanner scanner) {
        this.scanner = scanner;
        return this;
    }

    public SwaggerContextService initConfig() {

        String configIdKey;
        if (configId != null) {
            configIdKey = SwaggerConfig.CONFIG_ID_PREFIX + configId;
        } else {
            if (sc != null) {
                configIdKey = (sc.getInitParameter(SwaggerConfig.CONFIG_ID_KEY) != null) ? SwaggerConfig.CONFIG_ID_PREFIX + sc.getInitParameter(SwaggerConfig.CONFIG_ID_KEY) : SwaggerConfig.CONFIG_ID_DEFAULT;
            } else {
                configIdKey = SwaggerConfig.CONFIG_ID_DEFAULT;
            }
        }
        SwaggerConfig value = (swaggerConfig != null) ? swaggerConfig : null;
        if (value == null && sc != null) {
            value = new WebXMLReader(sc);
        }
        if (value != null) {
            SwaggerConfigMap.getInstance().putConfig(configIdKey, value);
        }
        return this;

    }

    public SwaggerConfig getConfig() {

        String configIdKey;
        if (configId != null) {
            configIdKey = SwaggerConfig.CONFIG_ID_PREFIX + configId;
        } else {
            if (sc != null) {
                configIdKey = (sc.getInitParameter(SwaggerConfig.CONFIG_ID_KEY) != null) ? SwaggerConfig.CONFIG_ID_PREFIX + configId : SwaggerConfig.CONFIG_ID_DEFAULT;
            } else {
                configIdKey = SwaggerConfig.CONFIG_ID_DEFAULT;
            }
        }
        return SwaggerConfigMap.getInstance().getConfig(configIdKey);

    }

    public SwaggerContextService initScanner() {
        String scannerIdKey;
        Scanner value = (scanner != null) ? scanner : new DefaultJaxrsScanner();
        ScannerFactory.setScanner(value);
        if (sc != null) {
            if (scannerId != null) {
                scannerIdKey = AbstractScanner.SCANNER_ID_PREFIX + scannerId;
            } else {
                scannerIdKey = (sc.getInitParameter(AbstractScanner.SCANNER_ID_KEY) != null) ? AbstractScanner.SCANNER_ID_PREFIX + sc.getInitParameter(AbstractScanner.SCANNER_ID_KEY) : AbstractScanner.SCANNER_ID_DEFAULT;
            }
            sc.getServletContext().setAttribute(scannerIdKey, scanner);
            String shouldPrettyPrint = sc.getInitParameter("swagger.pretty.print");
            if (shouldPrettyPrint != null) {
                scanner.setPrettyPrint(Boolean.parseBoolean(shouldPrettyPrint));
            }

        }

        return this;
    }

    public Scanner getScanner() {
        String scannerIdKey;
        if (scannerId != null) {
            scannerIdKey = AbstractScanner.SCANNER_ID_PREFIX + scannerId;
        } else {
            if (sc != null) {
                scannerIdKey = (sc.getInitParameter(AbstractScanner.SCANNER_ID_KEY) != null) ? AbstractScanner.SCANNER_ID_PREFIX + sc.getInitParameter(AbstractScanner.SCANNER_ID_KEY) : AbstractScanner.SCANNER_ID_DEFAULT;
            } else {
                scannerIdKey = AbstractScanner.SCANNER_ID_DEFAULT;
            }
        }
        Scanner value = (Scanner) sc.getServletContext().getAttribute(scannerIdKey);
        if (value == null) {
            value = ScannerFactory.getScanner();
        }

        return value;
    }

    public static boolean isScannerIdInitParamDefined(ServletConfig sc) {
        return (sc.getInitParameter(AbstractScanner.SCANNER_ID_KEY) != null) ? true : false;
    }

    public static String getScannerIdFromInitParam(ServletConfig sc) {
        return sc.getInitParameter(AbstractScanner.SCANNER_ID_KEY);
    }
}

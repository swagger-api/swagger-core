package io.swagger.jaxrs.config;

import io.swagger.config.Scanner;
import io.swagger.config.ScannerFactory;
import io.swagger.config.SwaggerConfig;

import javax.servlet.ServletConfig;

public class SwaggerContextService {

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
            SwaggerConfigLocator.getInstance().putConfig(configIdKey, value);
        }
        return this;

    }

    public SwaggerConfig getConfig() {

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
        return SwaggerConfigLocator.getInstance().getConfig(configIdKey);

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
            sc.getServletContext().setAttribute(scannerIdKey, value);
            String shouldPrettyPrint = sc.getInitParameter("swagger.pretty.print");
            if (shouldPrettyPrint != null) {
                value.setPrettyPrint(Boolean.parseBoolean(shouldPrettyPrint));
            }

        }
        return this;
    }

    public Scanner getScanner() {
        if (sc != null) {
            String scannerIdKey;
            if (scannerId != null) {
                scannerIdKey = AbstractScanner.SCANNER_ID_PREFIX + scannerId;
            } else {
                scannerIdKey = (sc.getInitParameter(AbstractScanner.SCANNER_ID_KEY) != null) ? AbstractScanner.SCANNER_ID_PREFIX + sc.getInitParameter(AbstractScanner.SCANNER_ID_KEY) : AbstractScanner.SCANNER_ID_DEFAULT;
            }
            return (Scanner) sc.getServletContext().getAttribute(scannerIdKey);
        } else {
            return ScannerFactory.getScanner();
        }
    }

    public static boolean isScannerIdInitParamDefined(ServletConfig sc) {
        return (sc.getInitParameter(AbstractScanner.SCANNER_ID_KEY) != null) ? true : false;
    }

    public static String getScannerIdFromInitParam(ServletConfig sc) {
        return sc.getInitParameter(AbstractScanner.SCANNER_ID_KEY);
    }
}

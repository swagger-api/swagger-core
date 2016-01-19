package io.swagger.jaxrs.config;

import io.swagger.config.Scanner;
import io.swagger.config.ScannerFactory;
import io.swagger.config.SwaggerConfig;
import io.swagger.models.Swagger;

import javax.servlet.ServletConfig;

public class SwaggerContextService {

    public static final String CONFIG_ID_KEY = "swagger.config.id";
    public static final String CONFIG_ID_PREFIX = CONFIG_ID_KEY + ".";
    public static final String CONFIG_ID_DEFAULT = CONFIG_ID_PREFIX + "default";

    public static final String SCANNER_ID_KEY = "swagger.scanner.id";
    public static final String SCANNER_ID_PREFIX = SCANNER_ID_KEY + ".";
    public static final String SCANNER_ID_DEFAULT = SCANNER_ID_PREFIX + "default";

    public static final String CONTEXT_ID_KEY = "swagger.context.id";

    private ServletConfig sc;
    private String configId;
    private SwaggerConfig swaggerConfig;
    private String scannerId;
    private Scanner scanner;
    private String contextId;

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

    public void setContextId(String contextId) {
        this.contextId = contextId;
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

    public SwaggerContextService withContextId(String contextId) {
        this.contextId = contextId;
        return this;
    }
    
    public SwaggerContextService withScanner(Scanner scanner) {
        this.scanner = scanner;
        return this;
    }

    public SwaggerContextService initConfig() {
        return initConfig(null);
    }

    public SwaggerContextService initConfig(Swagger swagger) {

        String configIdKey;
        if (configId != null) {
            configIdKey = CONFIG_ID_PREFIX + configId;
        } else if (contextId != null) {
            configIdKey = CONFIG_ID_PREFIX + contextId;
        } else {
            if (sc != null) {
                configIdKey = (sc.getInitParameter(CONFIG_ID_KEY) != null) ? CONFIG_ID_PREFIX + sc.getInitParameter(CONFIG_ID_KEY) : null;
                if (configIdKey == null) {
                    configIdKey = (sc.getInitParameter(CONTEXT_ID_KEY) != null) ? CONFIG_ID_PREFIX + sc.getInitParameter(CONTEXT_ID_KEY) : CONFIG_ID_DEFAULT;
                }
            } else {
                configIdKey = CONFIG_ID_DEFAULT;
            }
        }
        SwaggerConfig value = (swaggerConfig != null) ? swaggerConfig : null;
        if (value == null && sc != null) {
            value = new WebXMLReader(sc);
        }
        if (value != null) {
            SwaggerConfigLocator.getInstance().putConfig(configIdKey, value);
        }
        if (swagger != null) {
            SwaggerConfigLocator.getInstance().putSwagger(configIdKey, swagger);
        }
        return this;

    }

    private Object getConfigOrSwagger(boolean returnSwagger) {
        String configIdKey;
        if (configId != null) {
            configIdKey = CONFIG_ID_PREFIX + configId;
        } else if (contextId != null) {
            configIdKey = CONFIG_ID_PREFIX + contextId;
        } else {
            if (sc != null) {
                configIdKey = (sc.getInitParameter(CONFIG_ID_KEY) != null) ? CONFIG_ID_PREFIX + sc.getInitParameter(CONFIG_ID_KEY) : null;
                if (configIdKey == null) {
                    configIdKey = (sc.getInitParameter(CONTEXT_ID_KEY) != null) ? CONFIG_ID_PREFIX + sc.getInitParameter(CONTEXT_ID_KEY) : CONFIG_ID_DEFAULT;
                }
            } else {
                configIdKey = CONFIG_ID_DEFAULT;
            }
        }
        if (returnSwagger) {
            return SwaggerConfigLocator.getInstance().getSwagger(configIdKey);
        } else {
            return SwaggerConfigLocator.getInstance().getConfig(configIdKey);
        }
    }

    public SwaggerConfig getConfig() {
        return (SwaggerConfig)getConfigOrSwagger(false);
    }

    public Swagger getSwagger() {
        Swagger value = (Swagger) getConfigOrSwagger(true);
        if (value == null && sc != null) {
            value = (Swagger) sc.getServletContext().getAttribute("swagger");
        }
        return value;
    }

    public SwaggerContextService updateSwagger(Swagger swagger) {

        String configIdKey;
        if (configId != null) {
            configIdKey = CONFIG_ID_PREFIX + configId;
        } else if (contextId != null) {
            configIdKey = CONFIG_ID_PREFIX + contextId;
        } else {
            if (sc != null) {
                configIdKey = (sc.getInitParameter(CONFIG_ID_KEY) != null) ? CONFIG_ID_PREFIX + sc.getInitParameter(CONFIG_ID_KEY) : null;
                if (configIdKey == null) {
                    configIdKey = (sc.getInitParameter(CONTEXT_ID_KEY) != null) ? CONFIG_ID_PREFIX + sc.getInitParameter(CONTEXT_ID_KEY) : CONFIG_ID_DEFAULT;
                }
            } else {
                configIdKey = CONFIG_ID_DEFAULT;
            }
        }
        if (swagger != null) {
            SwaggerConfigLocator.getInstance().putSwagger(configIdKey, swagger);
        }
        return this;

    }

    public SwaggerContextService initScanner() {
        String scannerIdKey;
        if (scannerId != null) {
            scannerIdKey = SCANNER_ID_PREFIX + scannerId;
        } else if (contextId != null) {
            scannerIdKey = SCANNER_ID_PREFIX + contextId;
        } else {
            if (sc != null) {
                scannerIdKey = (sc.getInitParameter(SCANNER_ID_KEY) != null) ? SCANNER_ID_PREFIX + sc.getInitParameter(SCANNER_ID_KEY) : null;
                if (scannerIdKey == null) {
                    scannerIdKey = (sc.getInitParameter(CONTEXT_ID_KEY) != null) ? SCANNER_ID_PREFIX + sc.getInitParameter(CONTEXT_ID_KEY) : SCANNER_ID_DEFAULT;
                }
            } else {
                scannerIdKey = SCANNER_ID_DEFAULT;
            }
        }
        Scanner value = (scanner != null) ? scanner : new DefaultJaxrsScanner();
        ScannerFactory.setScanner(value);
        if (sc != null) {
            sc.getServletContext().setAttribute(scannerIdKey, value);
            String shouldPrettyPrint = sc.getInitParameter("swagger.pretty.print");
            if (shouldPrettyPrint != null) {
                value.setPrettyPrint(Boolean.parseBoolean(shouldPrettyPrint));
            }
        }

        SwaggerScannerLocator.getInstance().putScanner(scannerIdKey, value);
        return this;
    }

    public Scanner getScanner() {
        String scannerIdKey;
        Scanner value = null;
        if (scannerId != null) {
            scannerIdKey = SCANNER_ID_PREFIX + scannerId;
        } else if (contextId != null) {
            scannerIdKey = SCANNER_ID_PREFIX + contextId;
        } else {
            if (sc != null) {
                scannerIdKey = (sc.getInitParameter(SCANNER_ID_KEY) != null) ? SCANNER_ID_PREFIX + sc.getInitParameter(SCANNER_ID_KEY) : null;
                if (scannerIdKey == null) {
                    scannerIdKey = (sc.getInitParameter(CONTEXT_ID_KEY) != null) ? SCANNER_ID_PREFIX + sc.getInitParameter(CONTEXT_ID_KEY) : SCANNER_ID_DEFAULT;
                }
                value = (Scanner) sc.getServletContext().getAttribute(scannerIdKey);
            } else {
                scannerIdKey = SCANNER_ID_DEFAULT;
            }
        }
        if (value != null) {
            return value;
        }
        value = SwaggerScannerLocator.getInstance().getScanner(scannerIdKey);
        if (value != null) {
            return value;
        }
        return ScannerFactory.getScanner();
    }

    public static boolean isScannerIdInitParamDefined(ServletConfig sc) {
        String key = sc.getInitParameter(SCANNER_ID_KEY);
        if (key != null){
            return true;
        } else {
            return (sc.getInitParameter(CONTEXT_ID_KEY) != null) ? true : false;
        }
    }

    public static String getScannerIdFromInitParam(ServletConfig sc) {
        String key = sc.getInitParameter(SCANNER_ID_KEY);
        if (key != null){
            return key;
        } else {
            return sc.getInitParameter(CONTEXT_ID_KEY);
        }
    }

    public static boolean isConfigIdInitParamDefined(ServletConfig sc) {
        String key = sc.getInitParameter(CONFIG_ID_KEY);
        if (key != null){
            return true;
        } else {
            return (sc.getInitParameter(CONTEXT_ID_KEY) != null) ? true : false;
        }
    }

    public static String getConfigIdFromInitParam(ServletConfig sc) {
        String key = sc.getInitParameter(CONFIG_ID_KEY);
        if (key != null){
            return key;
        } else {
            return sc.getInitParameter(CONTEXT_ID_KEY);
        }
    }
}

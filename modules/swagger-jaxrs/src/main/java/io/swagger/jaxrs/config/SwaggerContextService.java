package io.swagger.jaxrs.config;

import io.swagger.config.Scanner;
import io.swagger.config.ScannerFactory;
import io.swagger.config.SwaggerConfig;
import io.swagger.models.Swagger;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletConfig;

import org.apache.commons.lang3.StringUtils;

public class SwaggerContextService {

    private static Logger LOGGER = LoggerFactory.getLogger(SwaggerContextService.class);

    public static final String CONFIG_ID_KEY = "swagger.config.id";
    public static final String CONFIG_ID_PREFIX = CONFIG_ID_KEY + ".";
    public static final String CONFIG_ID_DEFAULT = CONFIG_ID_PREFIX + "default";

    public static final String SCANNER_ID_KEY = "swagger.scanner.id";
    public static final String SCANNER_ID_PREFIX = SCANNER_ID_KEY + ".";
    public static final String SCANNER_ID_DEFAULT = SCANNER_ID_PREFIX + "default";

    public static final String CONTEXT_ID_KEY = "swagger.context.id";
    public static final String USE_PATH_BASED_CONFIG = "swagger.use.path.based.config";

    private ServletConfig sc;
    private String configId;
    private SwaggerConfig swaggerConfig;
    private String scannerId;
    private Scanner scanner;
    private String contextId;
    private String basePath;
    private boolean usePathBasedConfig = false;
    
    public boolean isUsePathBasedConfig() {
        return usePathBasedConfig;
    }
    
    public void setUsePathBasedConfig(boolean usePathBasedConfig) {
        this.usePathBasedConfig = usePathBasedConfig;
    }
    
    public void setBasePath(String basePath) {
        this.basePath = normalizeBasePath(basePath);
    }
    
    public String getBasePath() {
        return basePath;
    }

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
        if (isServletConfigAvailable(sc)) this.sc = sc;
        return this;
    }

    public static boolean isServletConfigAvailable (ServletConfig sc) {
        if (sc == null) return false;
        // hack for quick fix for https://github.com/swagger-api/swagger-core/issues/1691
        // in v1.5.7, targeting v1.5.8; overall improved/refactored "swagger context" to be applied in later major versions
        try {
            sc.getInitParameter("test");
        } catch (Exception e) {
            LOGGER.info("Exception caught testing servletConfig. see https://github.com/swagger-api/swagger-core/issues/1691 ", e.getMessage());
            return false;
        }
        return true;
    }

    public SwaggerContextService withBasePath(String basePath) {
        this.basePath = normalizeBasePath(basePath);
        return this;
    }
    
    public SwaggerContextService withPathBasedConfig(boolean usePathBasedConfig) {
        this.usePathBasedConfig = usePathBasedConfig;
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
            if (isServletConfigAvailable(sc)) {
                configIdKey = (sc.getInitParameter(CONFIG_ID_KEY) != null) ? CONFIG_ID_PREFIX + sc.getInitParameter(CONFIG_ID_KEY) : null;
                if (configIdKey == null) {
                    boolean usePathBasedConfig = Boolean.valueOf(sc.getInitParameter(USE_PATH_BASED_CONFIG));
                    if (usePathBasedConfig && StringUtils.isNotBlank(basePath)) {
                        configIdKey = CONFIG_ID_PREFIX + basePath;
                    } else {
                        configIdKey = (sc.getInitParameter(CONTEXT_ID_KEY) != null) ? CONFIG_ID_PREFIX + sc.getInitParameter(CONTEXT_ID_KEY) : CONFIG_ID_DEFAULT;
                    }
                }
            } else {
                if (isUsePathBasedConfig() && StringUtils.isNotBlank(basePath)) {
                    configIdKey = CONFIG_ID_PREFIX + basePath;
                } else {
                    configIdKey = CONFIG_ID_DEFAULT;
                }
            }
        }
        SwaggerConfig value = (swaggerConfig != null) ? swaggerConfig : null;
        if (value == null && isServletConfigAvailable(sc)) {
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
            if (isServletConfigAvailable(sc)) {
                configIdKey = (sc.getInitParameter(CONFIG_ID_KEY) != null) ? CONFIG_ID_PREFIX + sc.getInitParameter(CONFIG_ID_KEY) : null;
                if (configIdKey == null) {
                    boolean usePathBasedConfig = Boolean.valueOf(sc.getInitParameter(USE_PATH_BASED_CONFIG));
                    if (usePathBasedConfig && StringUtils.isNotBlank(basePath)) {
                        configIdKey = CONFIG_ID_PREFIX + basePath;
                    } else {
                        configIdKey = (sc.getInitParameter(CONTEXT_ID_KEY) != null) ? CONFIG_ID_PREFIX + sc.getInitParameter(CONTEXT_ID_KEY) : CONFIG_ID_DEFAULT;
                    }
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
        if (value == null && isServletConfigAvailable(sc)) {
            value = (Swagger) sc.getServletContext().getAttribute("swagger");
        }
        if (value == null) value = new Swagger();
        return value;
    }

    public SwaggerContextService updateSwagger(Swagger swagger) {

        String configIdKey;
        if (configId != null) {
            configIdKey = CONFIG_ID_PREFIX + configId;
        } else if (contextId != null) {
            configIdKey = CONFIG_ID_PREFIX + contextId;
        } else {
            if (isServletConfigAvailable(sc)) {
                configIdKey = (sc.getInitParameter(CONFIG_ID_KEY) != null) ? CONFIG_ID_PREFIX + sc.getInitParameter(CONFIG_ID_KEY) : null;
                if (configIdKey == null) {
                    boolean usePathBasedConfig = Boolean.valueOf(sc.getInitParameter(USE_PATH_BASED_CONFIG));
                    if (usePathBasedConfig && StringUtils.isNotBlank(basePath)) {
                        configIdKey = CONFIG_ID_PREFIX + basePath;
                    } else {
                        configIdKey = (sc.getInitParameter(CONTEXT_ID_KEY) != null) ? CONFIG_ID_PREFIX + sc.getInitParameter(CONTEXT_ID_KEY) : CONFIG_ID_DEFAULT;
                    }
                }
            } else {
                if (isUsePathBasedConfig() && StringUtils.isNotBlank(basePath)) {
                    configIdKey = CONFIG_ID_PREFIX + basePath;
                } else {
                    configIdKey = CONFIG_ID_DEFAULT;
                }
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
            if (isServletConfigAvailable(sc)) {
                scannerIdKey = (sc.getInitParameter(SCANNER_ID_KEY) != null) ? SCANNER_ID_PREFIX + sc.getInitParameter(SCANNER_ID_KEY) : null;
                if (scannerIdKey == null) {
                    boolean usePathBasedConfig = Boolean.valueOf(sc.getInitParameter(USE_PATH_BASED_CONFIG));
                    if (usePathBasedConfig && StringUtils.isNotBlank(basePath)) {
                        scannerIdKey = SCANNER_ID_PREFIX + basePath;
                    } else {    
                        scannerIdKey = (sc.getInitParameter(CONTEXT_ID_KEY) != null) ? SCANNER_ID_PREFIX + sc.getInitParameter(CONTEXT_ID_KEY) : SCANNER_ID_DEFAULT;
                    }
                }
            } else {
                if (isUsePathBasedConfig() && StringUtils.isNotBlank(basePath)) {
                    scannerIdKey = SCANNER_ID_PREFIX + basePath;
                } else {
                    scannerIdKey = SCANNER_ID_DEFAULT;
                }
            }
        }
        Scanner value = (scanner != null) ? scanner : new DefaultJaxrsScanner();
        ScannerFactory.setScanner(value);
        if (isServletConfigAvailable(sc)) {
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
            if (isServletConfigAvailable(sc)) {
                scannerIdKey = (sc.getInitParameter(SCANNER_ID_KEY) != null) ? SCANNER_ID_PREFIX + sc.getInitParameter(SCANNER_ID_KEY) : null;
                if (scannerIdKey == null) {
                    boolean usePathBasedConfig = Boolean.valueOf(sc.getInitParameter(USE_PATH_BASED_CONFIG));
                    if (usePathBasedConfig && StringUtils.isNotBlank(basePath)) {
                        scannerIdKey = SCANNER_ID_PREFIX + basePath;
                    } else {
                        scannerIdKey = (sc.getInitParameter(CONTEXT_ID_KEY) != null) ? SCANNER_ID_PREFIX + sc.getInitParameter(CONTEXT_ID_KEY) : SCANNER_ID_DEFAULT;
                    }
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

    public static boolean isUsePathBasedConfigInitParamDefined(ServletConfig sc) {
        if (!isServletConfigAvailable(sc)) return false;
        String key = sc.getInitParameter(USE_PATH_BASED_CONFIG);
        if (key != null){
            return true;
        } else {
            return (sc.getInitParameter(CONTEXT_ID_KEY) != null) ? true : false;
        }
    }

    public static boolean isScannerIdInitParamDefined(ServletConfig sc) {
        if (!isServletConfigAvailable(sc)) return false;
        String key = sc.getInitParameter(SCANNER_ID_KEY);
        if (key != null){
            return true;
        } else {
            return (sc.getInitParameter(CONTEXT_ID_KEY) != null);
        }
    }

    public static String getScannerIdFromInitParam(ServletConfig sc) {
        if (!isServletConfigAvailable(sc)) return null;
        String key = sc.getInitParameter(SCANNER_ID_KEY);
        if (key != null){
            return key;
        } else {
            return sc.getInitParameter(CONTEXT_ID_KEY);
        }
    }

    public static boolean isConfigIdInitParamDefined(ServletConfig sc) {
        if (!isServletConfigAvailable(sc)) return false;
        String key = sc.getInitParameter(CONFIG_ID_KEY);
        if (key != null){
            return true;
        } else {
            return (sc.getInitParameter(CONTEXT_ID_KEY) != null);
        }
    }

    public static String getConfigIdFromInitParam(ServletConfig sc) {
        if (!isServletConfigAvailable(sc)) return null;
        String key = sc.getInitParameter(CONFIG_ID_KEY);
        if (key != null){
            return key;
        } else {
            return sc.getInitParameter(CONTEXT_ID_KEY);
        }
    }
    
    /**
     * Normalize base path to the canonical form by adding trailing and leading slashes
     * @param basePath base path to normalize
     * @return normalized base path
     */
    private static String normalizeBasePath(final String basePath) {
        if (basePath == null) {
            return basePath;
        }
        
        String normalizedBasePath = basePath.trim();
        if (!normalizedBasePath.startsWith("/")) {
            normalizedBasePath = "/" + normalizedBasePath;
        }
        
        if (!normalizedBasePath.endsWith("/")) {
            normalizedBasePath = normalizedBasePath + "/";
        }
        
        return normalizedBasePath;
    }
}

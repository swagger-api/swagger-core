package io.swagger.jaxrs.config;

public abstract class AbstractScanner {
    protected boolean prettyPrint = false;

    public static final String SCANNER_ID_KEY = "swagger.scanner.id";
    public static final String SCANNER_ID_PREFIX = SCANNER_ID_KEY + ".";
    public static final String SCANNER_ID_DEFAULT = SCANNER_ID_PREFIX + "default";

    public boolean getPrettyPrint() {
        return prettyPrint;
    }

    public void setPrettyPrint(boolean shouldPrettyPrint) {
        this.prettyPrint = shouldPrettyPrint;
    }
}
package io.swagger.jaxrs.config;

public abstract class AbstractScanner {
    protected boolean prettyPrint = false;

    public final static String ATTR_SCANNER_ID = "scanner.id";
    public final static String ATTR_SCANNER_INSTANCE = "scanner.instance";

    public boolean getPrettyPrint() {
        return prettyPrint;
    }

    public void setPrettyPrint(boolean shouldPrettyPrint) {
        this.prettyPrint = shouldPrettyPrint;
    }
}
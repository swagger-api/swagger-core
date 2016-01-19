package io.swagger.jaxrs.config;

public abstract class AbstractScanner {
    protected boolean prettyPrint = false;

    public boolean getPrettyPrint() {
        return prettyPrint;
    }

    public void setPrettyPrint(boolean shouldPrettyPrint) {
        this.prettyPrint = shouldPrettyPrint;
    }
}
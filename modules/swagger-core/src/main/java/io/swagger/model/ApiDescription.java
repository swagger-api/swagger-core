package io.swagger.model;

/**
 * legacy support for swagger 1.2 deployments
 **/
@Deprecated
public class ApiDescription {
    private String path;
    private String method;

    public ApiDescription(String path, String method) {
        this.setPath(path);
        this.setMethod(method);
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }
}

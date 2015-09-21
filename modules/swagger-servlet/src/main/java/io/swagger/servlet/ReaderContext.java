package io.swagger.servlet;

import io.swagger.models.Swagger;
import io.swagger.models.parameters.Parameter;

import java.util.List;

/**
 * The <code>ReaderContext</code> class is wrapper for the <code>Reader</code> parameters.
 */
public class ReaderContext {

    private Swagger swagger;
    private Class<?> cls;
    private String parentPath;
    private String parentHttpMethod;
    private boolean readHidden;
    private List<String> parentConsumes;
    private List<String> parentProduces;
    private List<String> parentTags;
    private List<Parameter> parentParameters;

    public ReaderContext(Swagger swagger, Class<?> cls, String parentPath, String parentHttpMethod,
            boolean readHidden, List<String> parentConsumes, List<String> parentProduces,
            List<String> parentTags, List<Parameter> parentParameters) {
        setSwagger(swagger);
        setCls(cls);
        setParentPath(parentPath);
        setParentHttpMethod(parentHttpMethod);
        setReadHidden(readHidden);
        setParentConsumes(parentConsumes);
        setParentProduces(parentProduces);
        setParentTags(parentTags);
        setParentParameters(parentParameters);
    }

    public Swagger getSwagger() {
        return swagger;
    }

    public void setSwagger(Swagger swagger) {
        this.swagger = swagger;
    }

    public Class<?> getCls() {
        return cls;
    }

    public void setCls(Class<?> cls) {
        this.cls = cls;
    }

    public String getParentPath() {
        return parentPath;
    }

    public void setParentPath(String parentPath) {
        this.parentPath = parentPath;
    }

    public String getParentHttpMethod() {
        return parentHttpMethod;
    }

    public void setParentHttpMethod(String parentHttpMethod) {
        this.parentHttpMethod = parentHttpMethod;
    }

    public boolean isReadHidden() {
        return readHidden;
    }

    public void setReadHidden(boolean readHidden) {
        this.readHidden = readHidden;
    }

    public List<String> getParentConsumes() {
        return parentConsumes;
    }

    public void setParentConsumes(List<String> parentConsumes) {
        this.parentConsumes = parentConsumes;
    }

    public List<String> getParentProduces() {
        return parentProduces;
    }

    public void setParentProduces(List<String> parentProduces) {
        this.parentProduces = parentProduces;
    }

    public List<String> getParentTags() {
        return parentTags;
    }

    public void setParentTags(List<String> parentTags) {
        this.parentTags = parentTags;
    }

    public List<Parameter> getParentParameters() {
        return parentParameters;
    }

    public void setParentParameters(List<Parameter> parentParameters) {
        this.parentParameters = parentParameters;
    }
}

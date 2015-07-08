package io.swagger.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.models.parameters.Parameter;
import io.swagger.models.refs.GenericRef;
import io.swagger.models.refs.RefFormat;
import io.swagger.models.refs.RefType;

import java.util.List;
import java.util.Map;

/**
 * Created by Helmsdown on 7/8/15.
 */
public class RefPath implements Path {

    //TODO what should I do in the methods required by Path interface

    private GenericRef genericRef;

    public RefPath() {
    }

    public RefPath(String ref) {
        set$ref(ref);
    }

    public void set$ref(String ref) {
        this.genericRef = new GenericRef(RefType.PATH, ref);
    }

    public String get$ref() {
        return genericRef.getRef();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        RefPath refPath = (RefPath) o;

        return !(genericRef != null ? !genericRef.equals(refPath.genericRef) : refPath.genericRef != null);

    }

    @Override
    public int hashCode() {
        return genericRef != null ? genericRef.hashCode() : 0;
    }

    @JsonIgnore
    public RefFormat getRefFormat() {
        return this.genericRef.getFormat();
    }


    @Override
    public Path set(String method, Operation op) {
        throw new RuntimeException("Not implemented");
    }

    @Override
    public Path get(Operation get) {
        throw new RuntimeException("Not implemented");
    }

    @Override
    public Path put(Operation put) {
        throw new RuntimeException("Not implemented");
    }

    @Override
    public Path post(Operation post) {
        throw new RuntimeException("Not implemented");
    }

    @Override
    public Path delete(Operation delete) {
        throw new RuntimeException("Not implemented");
    }

    @Override
    public Path patch(Operation patch) {
        throw new RuntimeException("Not implemented");
    }

    @Override
    public Path options(Operation options) {
        throw new RuntimeException("Not implemented");
    }

    @Override
    @JsonIgnore
    public Operation getGet() {
        throw new RuntimeException("Not implemented");
    }

    @Override
    public void setGet(Operation get) {
        throw new RuntimeException("Not implemented");
    }

    @Override
    @JsonIgnore
    public Operation getPut() {
        throw new RuntimeException("Not implemented");
    }

    @Override
    public void setPut(Operation put) {
        throw new RuntimeException("Not implemented");
    }

    @Override
    @JsonIgnore
    public Operation getPost() {
        throw new RuntimeException("Not implemented");
    }

    @Override
    public void setPost(Operation post) {
        throw new RuntimeException("Not implemented");
    }

    @Override
    @JsonIgnore
    public Operation getDelete() {
        throw new RuntimeException("Not implemented");
    }

    @Override
    public void setDelete(Operation delete) {
        throw new RuntimeException("Not implemented");
    }

    @Override
    @JsonIgnore
    public Operation getPatch() {
        throw new RuntimeException("Not implemented");
    }

    @Override
    public void setPatch(Operation patch) {
        throw new RuntimeException("Not implemented");
    }

    @Override
    @JsonIgnore
    public Operation getOptions() {
        throw new RuntimeException("Not implemented");
    }

    @Override
    public void setOptions(Operation options) {
        throw new RuntimeException("Not implemented");
    }

    @Override
    @JsonIgnore
    public List<Operation> getOperations() {
        throw new RuntimeException("Not implemented");
    }

    @Override
    @JsonIgnore
    public List<Parameter> getParameters() {
        throw new RuntimeException("Not implemented");
    }

    @Override
    public void setParameters(List<Parameter> parameters) {
        throw new RuntimeException("Not implemented");
    }

    @Override
    public void addParameter(Parameter parameter) {
        throw new RuntimeException("Not implemented");
    }

    @Override
    public boolean isEmpty() {
        throw new RuntimeException("Not implemented");
    }

    @Override
    @JsonIgnore
    public Map<String, Object> getVendorExtensions() {
        throw new RuntimeException("Not implemented");
    }

    @Override
    public void setVendorExtension(String name, Object value) {
        throw new RuntimeException("Not implemented");
    }


}

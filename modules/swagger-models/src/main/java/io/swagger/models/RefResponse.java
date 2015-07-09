package io.swagger.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.models.properties.Property;
import io.swagger.models.refs.GenericRef;
import io.swagger.models.refs.RefFormat;
import io.swagger.models.refs.RefType;

import java.util.Map;

/**
 * Created by russellb337 on 7/9/15.
 */
public class RefResponse implements Response {

    private GenericRef genericRef;

    public RefResponse() {
    }

    public RefResponse(String ref) {
        set$ref(ref);
    }

    public void set$ref(String ref) {
        this.genericRef = new GenericRef(RefType.RESPONSE, ref);
    }

    public String get$ref() {
        return genericRef.getRef();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        RefResponse refResponse = (RefResponse) o;

        return !(genericRef != null ? !genericRef.equals(refResponse.genericRef) : refResponse.genericRef != null);

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
    public ResponseImpl schema(Property property) {
        throw new RuntimeException("Not implemented");
    }

    @Override
    public ResponseImpl description(String description) {
        throw new RuntimeException("Not implemented");
    }

    @Override
    public ResponseImpl example(String type, Object example) {
        throw new RuntimeException("Not implemented");
    }

    @Override
    public ResponseImpl header(String name, Property property) {
        throw new RuntimeException("Not implemented");
    }

    @Override
    public ResponseImpl headers(Map<String, Property> headers) {
        throw new RuntimeException("Not implemented");
    }

    @Override
    @JsonIgnore
    public String getDescription() {
        throw new RuntimeException("Not implemented");
    }

    @Override
    public void setDescription(String description) {
        throw new RuntimeException("Not implemented");
    }

    @Override
    @JsonIgnore
    public Property getSchema() {
        throw new RuntimeException("Not implemented");
    }

    @Override
    public void setSchema(Property schema) {
        throw new RuntimeException("Not implemented");
    }

    @Override
    @JsonIgnore
    public Map<String, Object> getExamples() {
        throw new RuntimeException("Not implemented");
    }

    @Override
    public void setExamples(Map<String, Object> examples) {
        throw new RuntimeException("Not implemented");
    }

    @Override
    @JsonIgnore
    public Map<String, Property> getHeaders() {
        throw new RuntimeException("Not implemented");
    }

    @Override
    public void setHeaders(Map<String, Property> headers) {
        throw new RuntimeException("Not implemented");
    }

    @Override
    public void addHeader(String key, Property property) {
        throw new RuntimeException("Not implemented");
    }
}

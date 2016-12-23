package io.swagger.models;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import io.swagger.models.properties.Property;

import java.util.LinkedHashMap;
import java.util.Map;

public class Response {
    private String description;
    private Property schema;
    private Map<String, Object> examples;
    private Map<String, Property> headers;
    private Map<String, Object> vendorExtensions = new LinkedHashMap<String, Object>();

    public Response schema(Property property) {
        this.setSchema(property);
        return this;
    }

    public Response description(String description) {
        this.setDescription(description);
        return this;
    }

    public Response example(String type, Object example) {
        if (examples == null) {
            examples = new LinkedHashMap<String, Object>();
        }
        examples.put(type, example);
        return this;
    }

    public Response header(String name, Property property) {
        addHeader(name, property);
        return this;
    }

    public Response headers(Map<String, Property> headers) {
        this.headers = headers;
        return this;
    }

    public Response vendorExtension(String key, Object obj) {
        this.setVendorExtension(key, obj);
        return this;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Property getSchema() {
        return schema;
    }

    public void setSchema(Property schema) {
        this.schema = schema;
    }

    public Map<String, Object> getExamples() {
        return this.examples;
    }

    public void setExamples(Map<String, Object> examples) {
        this.examples = examples;
    }

    public Map<String, Property> getHeaders() {
        return headers;
    }

    public void setHeaders(Map<String, Property> headers) {
        this.headers = headers;
    }

    public void addHeader(String key, Property property) {
        if (this.headers == null) {
            this.headers = new LinkedHashMap<String, Property>();
        }
        this.headers.put(key, property);
    }

    @JsonAnyGetter
    public Map<String, Object> getVendorExtensions() {
        return vendorExtensions;
    }

    @JsonAnySetter
    public void setVendorExtension(String name, Object value) {
        if (name.startsWith("x-")) {
            vendorExtensions.put(name, value);
        }
    }

    public void setVendorExtensions(Map<String, Object> vendorExtensions) {
        this.vendorExtensions = vendorExtensions;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Response)) {
            return false;
        }

        Response response = (Response) o;

        if (description != null ? !description.equals(response.description) : response.description != null) {
            return false;
        }
        if (schema != null ? !schema.equals(response.schema) : response.schema != null) {
            return false;
        }
        if (examples != null ? !examples.equals(response.examples) : response.examples != null) {
            return false;
        }
        if (headers != null ? !headers.equals(response.headers) : response.headers != null) {
            return false;
        }
        return vendorExtensions != null ? vendorExtensions.equals(response.vendorExtensions) : response.vendorExtensions == null;

    }

    @Override
    public int hashCode() {
        int result = description != null ? description.hashCode() : 0;
        result = 31 * result + (schema != null ? schema.hashCode() : 0);
        result = 31 * result + (examples != null ? examples.hashCode() : 0);
        result = 31 * result + (headers != null ? headers.hashCode() : 0);
        result = 31 * result + (vendorExtensions != null ? vendorExtensions.hashCode() : 0);
        return result;
    }
}

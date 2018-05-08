package io.swagger.models;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import io.swagger.models.properties.Property;
import io.swagger.models.utils.PropertyModelConverter;

import java.util.LinkedHashMap;
import java.util.Map;

public class Response {
    private String description;
    private Model schemaAsModel;
    private Property schemaAsProperty;
    private Map<String, Object> examples;
    private Map<String, Property> headers;
    private Map<String, Object> vendorExtensions = new LinkedHashMap<String, Object>();

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

    @Deprecated
    public Property getSchema() {
        if (schemaAsProperty == null && schemaAsModel != null) {
            return new PropertyModelConverter().modelToProperty(schemaAsModel);
        }
        return schemaAsProperty;
    }

    @Deprecated
    public void setSchema(Property schema) {
        this.schemaAsProperty = schema;
    }

    @Deprecated
    public Response schema(Property property) {
        this.setSchema(property);
        return this;
    }

    public Model getResponseSchema() {
        if(schemaAsModel == null && schemaAsProperty != null) {
            return new PropertyModelConverter().propertyToModel(schemaAsProperty);
        }
        return schemaAsModel;
    }

    public void setResponseSchema(Model model) {
        this.schemaAsModel = model;
    }

    public Response responseSchema(Model model) {
        this.setResponseSchema(model);
        return this;
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
        if (schemaAsModel != null ? !schemaAsModel.equals(response.schemaAsModel) : response.schemaAsModel != null) {
            return false;
        }
        if (schemaAsProperty != null ? !schemaAsProperty.equals(response.schemaAsProperty) : response.schemaAsProperty != null) {
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
        result = 31 * result + (schemaAsModel != null ? schemaAsModel.hashCode() : 0);
        result = 31 * result + (schemaAsProperty != null ? schemaAsProperty.hashCode() : 0);
        result = 31 * result + (examples != null ? examples.hashCode() : 0);
        result = 31 * result + (headers != null ? headers.hashCode() : 0);
        result = 31 * result + (vendorExtensions != null ? vendorExtensions.hashCode() : 0);
        return result;
    }
}

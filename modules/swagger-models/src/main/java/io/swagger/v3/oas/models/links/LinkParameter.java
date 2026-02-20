package io.swagger.v3.oas.models.links;

import io.swagger.v3.oas.models.annotations.OpenAPI31;

import java.util.Objects;

/**
 * LinkParameter
 *
 */

public class LinkParameter {
    private String value;

    public LinkParameter() {
    }

    private java.util.Map<String, Object> extensions = null;

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public LinkParameter value(String value) {
        this.value = value;
        return this;
    }

    @Override
    public boolean equals(java.lang.Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        LinkParameter linkParameters = (LinkParameter) o;
        return Objects.equals(this.value, linkParameters.value) &&
                Objects.equals(this.extensions, linkParameters.extensions);
    }

    @Override
    public int hashCode() {
        return Objects.hash(value, extensions);
    }

    public java.util.Map<String, Object> getExtensions() {
        return extensions;
    }

    public void addExtension(String name, Object value) {
        if (name == null || name.isEmpty() || !name.startsWith("x-")) {
            return;
        }
        if (this.extensions == null) {
            this.extensions = new java.util.LinkedHashMap<>();
        }
        this.extensions.put(name, value);
    }

    @OpenAPI31
    public void addExtension31(String name, Object value) {
        if (name != null && (name.startsWith("x-oas-") || name.startsWith("x-oai-"))) {
            return;
        }
        addExtension(name, value);
    }

    public void setExtensions(java.util.Map<String, Object> extensions) {
        this.extensions = extensions;
    }

    public LinkParameter extensions(java.util.Map<String, Object> extensions) {
        this.extensions = extensions;
        return this;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("class LinkParameter {\n");

        sb.append("}");
        return sb.toString();
    }

}


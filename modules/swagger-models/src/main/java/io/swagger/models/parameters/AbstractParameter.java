package io.swagger.models.parameters;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;

import java.util.LinkedHashMap;
import java.util.Map;

public abstract class AbstractParameter {
    private Map<String, Object> vendorExtensions = new LinkedHashMap<String, Object>();
    protected String in;
    protected String name;
    protected String description;
    protected boolean required = false;
    protected String access;
    protected String pattern;
    protected Boolean allowEmptyValue;
    protected Boolean readOnly;

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof AbstractParameter)) {
            return false;
        }

        AbstractParameter that = (AbstractParameter) o;

        if (required != that.required) {
            return false;
        }
        if (vendorExtensions != null ? !vendorExtensions.equals(that.vendorExtensions) : that.vendorExtensions != null) {
            return false;
        }
        if (in != null ? !in.equals(that.in) : that.in != null) {
            return false;
        }
        if (name != null ? !name.equals(that.name) : that.name != null) {
            return false;
        }
        if (description != null ? !description.equals(that.description) : that.description != null) {
            return false;
        }
        if (access != null ? !access.equals(that.access) : that.access != null) {
            return false;
        }
        if (pattern != null ? !pattern.equals(that.pattern) : that.pattern != null) {
            return false;
        }
        if (allowEmptyValue != null ? !allowEmptyValue.equals(that.allowEmptyValue) : that.allowEmptyValue != null) {
            return false;
        }
        return readOnly != null ? readOnly.equals(that.readOnly) : that.readOnly == null;

    }

    @Override
    public int hashCode() {
        int result = vendorExtensions != null ? vendorExtensions.hashCode() : 0;
        result = 31 * result + (in != null ? in.hashCode() : 0);
        result = 31 * result + (name != null ? name.hashCode() : 0);
        result = 31 * result + (description != null ? description.hashCode() : 0);
        result = 31 * result + (required ? 1 : 0);
        result = 31 * result + (access != null ? access.hashCode() : 0);
        result = 31 * result + (pattern != null ? pattern.hashCode() : 0);
        result = 31 * result + (allowEmptyValue != null ? allowEmptyValue.hashCode() : 0);
        result = 31 * result + (readOnly != null ? readOnly.hashCode() : 0);
        return result;
    }

    public String getIn() {
        return in;
    }

    public void setIn(String in) {
        this.in = in;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean getRequired() {
        return required;
    }

    public void setRequired(boolean required) {
        this.required = required;
    }

    public String getAccess() {
        return access;
    }

    public Boolean isReadOnly() {
        return readOnly;
    }

    public void setReadOnly(Boolean readOnly) {
        this.readOnly = readOnly;
    }

    public Boolean getAllowEmptyValue() {
        return allowEmptyValue;
    }

    public void setAllowEmptyValue(Boolean allowEmptyValue) {
        this.allowEmptyValue = allowEmptyValue;
    }

    public void setAccess(String access) {
        this.access = access;
    }

    public String getPattern() {
        return pattern;
    }

    public void setPattern(String pattern) {
        this.pattern = pattern;
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

}

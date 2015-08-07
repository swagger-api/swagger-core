package io.swagger.models.auth;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;

import java.util.HashMap;
import java.util.Map;


public abstract class AbstractSecuritySchemeDefinition implements SecuritySchemeDefinition {

    private final Map<String, Object> vendorExtensions = new HashMap<String, Object>();

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        AbstractSecuritySchemeDefinition that = (AbstractSecuritySchemeDefinition) o;

        return !(vendorExtensions != null ? !vendorExtensions.equals(that.vendorExtensions) : that.vendorExtensions != null);

    }

    @Override
    public int hashCode() {
        return vendorExtensions != null ? vendorExtensions.hashCode() : 0;
    }
}

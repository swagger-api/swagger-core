package io.swagger.models;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.LinkedHashMap;
import java.util.Map;

public abstract class AbstractModel implements Model {
    private ExternalDocs externalDocs;
    private String reference;
    private String title;
    private Map<String, Object> vendorExtensions = new LinkedHashMap<String, Object>();

    @Override
    public ExternalDocs getExternalDocs() {
        return externalDocs;
    }

    public void setExternalDocs(ExternalDocs value) {
        externalDocs = value;
    }

    @Override
    public String getTitle() {
        return title;
    }

    @Override
    public void setTitle(String title) {
        this.title = title;
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

    public void cloneTo(Object clone) {
        AbstractModel cloned = (AbstractModel) clone;
        cloned.externalDocs = this.externalDocs;
    }

    public Object clone() {
        return null;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result
                + ((externalDocs == null) ? 0 : externalDocs.hashCode());
        result = prime * result
                + ((vendorExtensions == null) ? 0 : vendorExtensions.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        AbstractModel other = (AbstractModel) obj;
        if (externalDocs == null) {
            if (other.externalDocs != null) {
                return false;
            }
        } else if (!externalDocs.equals(other.externalDocs)) {
            return false;
        }
        if (vendorExtensions == null) {
            if (other.vendorExtensions != null) {
                return false;
            }
        } else if (!vendorExtensions.equals(other.vendorExtensions)) {
            return false;
        }
        return true;
    }

    @JsonIgnore
    @Override
    public String getReference() {
        return reference;
    }

    @Override
    public void setReference(String reference) {
        this.reference = reference;
    }


}

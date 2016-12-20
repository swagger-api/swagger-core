package io.swagger.models;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Container for a <a href="https://github.com/OAI/OpenAPI-Specification/blob/master/versions/2.0.md#externalDocumentationObject">External Documentation Object</a>.
 */
public class ExternalDocs {
    /**
     * A short description of the target documentation. GFM syntax can be used for rich text representation.
     */
    private String description;

    /**
     * Required. The URL for the target documentation. Value MUST be in the format of a URL.
     */
    private String url;

    private Map<String, Object> vendorExtensions = new LinkedHashMap<String, Object>();

    public ExternalDocs() {
    }

    public ExternalDocs(String description, String url) {
        this.setDescription(description);
        this.setUrl(url);
    }

    public ExternalDocs description(String description) {
        this.setDescription(description);
        return this;
    }

    public ExternalDocs url(String url) {
        this.setUrl(url);
        return this;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
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
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result
                + ((description == null) ? 0 : description.hashCode());
        result = prime * result + ((url == null) ? 0 : url.hashCode());
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
        ExternalDocs other = (ExternalDocs) obj;
        if (description == null) {
            if (other.description != null) {
                return false;
            }
        } else if (!description.equals(other.description)) {
            return false;
        }
        if (url == null) {
            if (other.url != null) {
                return false;
            }
        } else if (!url.equals(other.url)) {
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
}

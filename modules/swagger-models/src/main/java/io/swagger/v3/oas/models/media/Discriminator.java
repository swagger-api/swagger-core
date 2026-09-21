package io.swagger.v3.oas.models.media;

import io.swagger.v3.oas.models.annotations.OpenAPI31;
import io.swagger.v3.oas.models.annotations.OpenAPI32;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;

public class Discriminator {
    private String propertyName;
    /**
     * @since 2.2.56 (OpenAPI 3.2)
     */
    @OpenAPI32
    private String defaultMapping;
    private Map<String, String> mapping;

    /**
     * @since 2.2.0 (OpenAPI 3.1.0)
     */
    @OpenAPI31
    private Map<String, Object> extensions;

    public Discriminator propertyName(String propertyName) {
        this.propertyName = propertyName;
        return this;
    }

    public String getPropertyName() {
        return propertyName;
    }

    public void setPropertyName(String propertyName) {
        this.propertyName = propertyName;
    }

    /**
     * returns the defaultMapping property from a Discriminator instance.
     *
     * @since 2.2.56 (OpenAPI 3.2)
     * @return String defaultMapping
     **/
    @OpenAPI32
    public String getDefaultMapping() {
        return defaultMapping;
    }

    @OpenAPI32
    public void setDefaultMapping(String defaultMapping) {
        this.defaultMapping = defaultMapping;
    }

    @OpenAPI32
    public Discriminator defaultMapping(String defaultMapping) {
        this.defaultMapping = defaultMapping;
        return this;
    }

    public Discriminator mapping(String name, String value) {
        if (this.mapping == null) {
            this.mapping = new LinkedHashMap<>();
        }
        this.mapping.put(name, value);
        return this;
    }

    public Discriminator mapping(Map<String, String> mapping) {
        this.mapping = mapping;
        return this;
    }

    public Map<String, String> getMapping() {
        return mapping;
    }

    public void setMapping(Map<String, String> mapping) {
        this.mapping = mapping;
    }

    /**
     * returns the specific extensions from a Discriminator instance.
     *
     * @since 2.2.0 (OpenAPI 3.1.0)
     * @return Map&lt;String, Object&gt; extensions
     **/
    @OpenAPI31
    public Map<String, Object> getExtensions() {
        return extensions;
    }

    @OpenAPI31
    public void setExtensions(Map<String, Object> extensions) {
        this.extensions = extensions;
    }

    @OpenAPI31
    public void addExtension(String name, Object value) {
        if (name == null || name.isEmpty() || !name.startsWith("x-")) {
            return;
        }
        if (name.startsWith("x-oas-") || name.startsWith("x-oai-")) {
            return;
        }
        if (this.extensions == null) {
            this.extensions = new java.util.LinkedHashMap<>();
        }
        this.extensions.put(name, value);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Discriminator)) {
            return false;
        }

        Discriminator that = (Discriminator) o;

        if (propertyName != null ? !propertyName.equals(that.propertyName) : that.propertyName != null) {
            return false;
        }
        if (defaultMapping != null ? !defaultMapping.equals(that.defaultMapping) : that.defaultMapping != null) {
            return false;
        }
        if (extensions != null ? !extensions.equals(that.extensions) : that.extensions != null) {
            return false;
        }
        return mapping != null ? mapping.equals(that.mapping) : that.mapping == null;

    }

    @Override
    public int hashCode() {
        return Objects.hash(propertyName, defaultMapping, mapping, extensions);
    }

    @Override
    public String toString() {
        return "Discriminator{" +
                "propertyName='" + propertyName + '\'' +
                ", defaultMapping='" + defaultMapping + '\'' +
                ", mapping=" + mapping +
                ", extensions=" + extensions +
                '}';
    }
}

package io.swagger.v3.oas.models.media;

import java.util.HashMap;
import java.util.Map;

public class Discriminator {
    private String propertyName;
    private Map<String, String> mapping;

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

    public Discriminator mapping(String name, String value) {
        if(this.mapping == null) {
            this.mapping = new HashMap<>();
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
        return mapping != null ? mapping.equals(that.mapping) : that.mapping == null;

    }

    @Override
    public int hashCode() {
        int result = propertyName != null ? propertyName.hashCode() : 0;
        result = 31 * result + (mapping != null ? mapping.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "Discriminator{" +
                "propertyName='" + propertyName + '\'' +
                ", mapping=" + mapping +
                '}';
    }
}

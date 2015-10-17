package io.swagger.models.properties;

import io.swagger.models.Xml;

public class MapProperty extends AbstractProperty implements Property {
    Property property;

    public MapProperty() {
        super.type = "object";
    }

    public MapProperty(Property property) {
        super.type = "object";
        this.property = property;
    }

    public static boolean isType(String type, String format) {
        if ("object".equals(type) && format == null) {
            return true;
        }
        return false;
    }

    public MapProperty xml(Xml xml) {
        this.setXml(xml);
        return this;
    }

    public MapProperty additionalProperties(Property property) {
        this.setAdditionalProperties(property);
        return this;
    }

    public MapProperty description(String description) {
        this.setDescription(description);
        return this;
    }

    public MapProperty vendorExtension(String key, Object obj) {
        this.setVendorExtension(key, obj);
        return this;
    }

    public Property getAdditionalProperties() {
        return property;
    }

    public void setAdditionalProperties(Property property) {
        this.property = property;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = super.hashCode();
        result = prime * result + ((property == null) ? 0 : property.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (!super.equals(obj)) {
            return false;
        }
        if (!(obj instanceof MapProperty)) {
            return false;
        }
        MapProperty other = (MapProperty) obj;
        if (property == null) {
            if (other.property != null) {
                return false;
            }
        } else if (!property.equals(other.property)) {
            return false;
        }
        return true;
    }
}
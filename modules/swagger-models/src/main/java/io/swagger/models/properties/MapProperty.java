package io.swagger.models.properties;

import io.swagger.models.Xml;

public class MapProperty extends AbstractProperty implements Property {
    Property property;
    private Integer minProperties;
    private Integer maxProperties;

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

    public MapProperty title(String title) {
        this.setTitle(title);
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

    public Integer getMinProperties() {
        return minProperties;
    }

    public void setMinProperties(Integer minProperties) {
        this.minProperties = minProperties;
    }

    public Integer getMaxProperties() {
        return maxProperties;
    }

    public void setMaxProperties(Integer maxProperties) {
        this.maxProperties = maxProperties;
    }

    public MapProperty readOnly() {
        this.setReadOnly(Boolean.TRUE);
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;

        MapProperty that = (MapProperty) o;

        if (property != null ? !property.equals(that.property) : that.property != null) return false;
        if (minProperties != null ? !minProperties.equals(that.minProperties) : that.minProperties != null)
            return false;
        return maxProperties != null ? maxProperties.equals(that.maxProperties) : that.maxProperties == null;

    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + (property != null ? property.hashCode() : 0);
        result = 31 * result + (minProperties != null ? minProperties.hashCode() : 0);
        result = 31 * result + (maxProperties != null ? maxProperties.hashCode() : 0);
        return result;
    }
}
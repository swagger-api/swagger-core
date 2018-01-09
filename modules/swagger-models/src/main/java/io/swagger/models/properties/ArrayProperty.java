package io.swagger.models.properties;

import io.swagger.models.Xml;

public class ArrayProperty extends AbstractProperty implements Property {
    public static final String TYPE = "array";
    protected Boolean uniqueItems;
    protected Property items;
    private Integer maxItems;
    private Integer minItems;

    public ArrayProperty() {
        super.type = TYPE;
    }

    public ArrayProperty(Property items) {
        super.type = TYPE;
        setItems(items);
    }

    public static boolean isType(String type) {
        return TYPE.equals(type);
    }

    public ArrayProperty xml(Xml xml) {
        this.setXml(xml);
        return this;
    }

    public ArrayProperty uniqueItems() {
        this.setUniqueItems(true);
        return this;
    }

    public ArrayProperty description(String description) {
        this.setDescription(description);
        return this;
    }

    public ArrayProperty title(String title) {
        this.setTitle(title);
        return this;
    }

    public ArrayProperty example( Object example ){
        this.setExample( example );
        return this;
    }

    public ArrayProperty items(Property items) {
        setItems(items);
        return this;
    }

    public ArrayProperty vendorExtension(String key, Object obj) {
        this.setVendorExtension(key, obj);
        return this;
    }

    public ArrayProperty readOnly() {
        this.setReadOnly(Boolean.TRUE);
        return this;
    }

    public Property getItems() {
        return items;
    }

    public void setItems(Property items) {
        this.items = items;
    }

    public Boolean getUniqueItems() {
        return uniqueItems;
    }

    public void setUniqueItems(Boolean uniqueItems) {
        this.uniqueItems = Boolean.TRUE.equals(uniqueItems) ? true : null;
    }

    public Integer getMaxItems() {
        return maxItems;
    }

    public void setMaxItems(Integer maxItems) {
        this.maxItems = maxItems;
    }

    public Integer getMinItems() {
        return minItems;
    }

    public void setMinItems(Integer minItems) {
        this.minItems = minItems;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ArrayProperty)) {
            return false;
        }
        if (!super.equals(o)) {
            return false;
        }

        ArrayProperty that = (ArrayProperty) o;

        if (uniqueItems != null ? !uniqueItems.equals(that.uniqueItems) : that.uniqueItems != null) {
            return false;
        }
        if (items != null ? !items.equals(that.items) : that.items != null) {
            return false;
        }
        if (maxItems != null ? !maxItems.equals(that.maxItems) : that.maxItems != null) {
            return false;
        }
        return minItems != null ? minItems.equals(that.minItems) : that.minItems == null;

    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + (uniqueItems != null ? uniqueItems.hashCode() : 0);
        result = 31 * result + (items != null ? items.hashCode() : 0);
        result = 31 * result + (maxItems != null ? maxItems.hashCode() : 0);
        result = 31 * result + (minItems != null ? minItems.hashCode() : 0);
        return result;
    }
}

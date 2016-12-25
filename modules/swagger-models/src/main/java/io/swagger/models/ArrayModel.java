package io.swagger.models;

import io.swagger.models.properties.Property;

import java.util.Map;

public class ArrayModel extends AbstractModel {
    private Map<String, Property> properties;
    private String type;
    private String description;
    private Property items;
    private Object example;
    private Integer minItems;
    private Integer maxItems;

    public ArrayModel() {
        this.type = "array";
    }

    public ArrayModel description(String description) {
        this.setDescription(description);
        return this;
    }

    public ArrayModel items(Property items) {
        this.setItems(items);
        return this;
    }

    public ArrayModel minItems(int minItems) {
        this.setMinItems(minItems);
        return this;
    }

    public ArrayModel maxItems(int maxItems) {
        this.setMaxItems(maxItems);
        return this;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Property getItems() {
        return items;
    }

    public void setItems(Property items) {
        this.items = items;
    }

    public Map<String, Property> getProperties() {
        return properties;
    }

    public void setProperties(Map<String, Property> properties) {
        this.properties = properties;
    }

    public Object getExample() {
        return example;
    }

    public void setExample(Object example) {
        this.example = example;
    }

    public Integer getMinItems() {
        return minItems;
    }

    public void setMinItems(Integer minItems) {
        this.minItems = minItems;
    }

    public Integer getMaxItems() {
        return maxItems;
    }

    public void setMaxItems(Integer maxItems) {
        this.maxItems = maxItems;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ArrayModel)) {
            return false;
        }
        if (!super.equals(o)) {
            return false;
        }

        ArrayModel that = (ArrayModel) o;

        if (properties != null ? !properties.equals(that.properties) : that.properties != null) {
            return false;
        }
        if (type != null ? !type.equals(that.type) : that.type != null) {
            return false;
        }
        if (description != null ? !description.equals(that.description) : that.description != null) {
            return false;
        }
        if (items != null ? !items.equals(that.items) : that.items != null) {
            return false;
        }
        if (example != null ? !example.equals(that.example) : that.example != null) {
            return false;
        }
        if (minItems != null ? !minItems.equals(that.minItems) : that.minItems != null) {
            return false;
        }
        return maxItems != null ? maxItems.equals(that.maxItems) : that.maxItems == null;

    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + (properties != null ? properties.hashCode() : 0);
        result = 31 * result + (type != null ? type.hashCode() : 0);
        result = 31 * result + (description != null ? description.hashCode() : 0);
        result = 31 * result + (items != null ? items.hashCode() : 0);
        result = 31 * result + (example != null ? example.hashCode() : 0);
        result = 31 * result + (minItems != null ? minItems.hashCode() : 0);
        result = 31 * result + (maxItems != null ? maxItems.hashCode() : 0);
        return result;
    }

    public Object clone() {
        ArrayModel cloned = new ArrayModel();
        super.cloneTo(cloned);

        cloned.properties = this.properties;
        cloned.type = this.type;
        cloned.description = this.description;
        cloned.items = this.items;
        cloned.example = this.example;

        return cloned;

    }

}
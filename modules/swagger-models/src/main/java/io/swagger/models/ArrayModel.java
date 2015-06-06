package io.swagger.models;

import io.swagger.models.properties.Property;

import java.util.Map;

public class ArrayModel extends AbstractModel {
    private Map<String, Property> properties;
    private String type;
    private String description;
    private Property items;
    private Object example;

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

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = super.hashCode();
        result = prime * result
                + ((description == null) ? 0 : description.hashCode());
        result = prime * result + ((example == null) ? 0 : example.hashCode());
        result = prime * result + ((items == null) ? 0 : items.hashCode());
        result = prime * result
                + ((properties == null) ? 0 : properties.hashCode());
        result = prime * result + ((type == null) ? 0 : type.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!super.equals(obj)) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        ArrayModel other = (ArrayModel) obj;
        if (description == null) {
            if (other.description != null) {
                return false;
            }
        } else if (!description.equals(other.description)) {
            return false;
        }
        if (example == null) {
            if (other.example != null) {
                return false;
            }
        } else if (!example.equals(other.example)) {
            return false;
        }
        if (items == null) {
            if (other.items != null) {
                return false;
            }
        } else if (!items.equals(other.items)) {
            return false;
        }
        if (properties == null) {
            if (other.properties != null) {
                return false;
            }
        } else if (!properties.equals(other.properties)) {
            return false;
        }
        if (type == null) {
            if (other.type != null) {
                return false;
            }
        } else if (!type.equals(other.type)) {
            return false;
        }
        return true;
    }
}
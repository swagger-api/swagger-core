package io.swagger.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.models.properties.Property;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ComposedModel extends AbstractModel {
    private List<Model> allOf = new ArrayList<Model>();
    private Model parent;
    private Model child;
    private List<RefModel> interfaces;
    private String description;
    private Object example;

    public ComposedModel parent(Model model) {
        this.setParent(model);
        return this;
    }

    public ComposedModel child(Model model) {
        this.setChild(model);
        return this;
    }

    public ComposedModel interfaces(List<RefModel> interfaces) {
        this.setInterfaces(interfaces);
        return this;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Map<String, Property> getProperties() {
        return null;
    }

    public void setProperties(Map<String, Property> properties) {

    }

    public Object getExample() {
        return example;
    }

    public void setExample(Object example) {
        this.example = example;
    }

    public List<Model> getAllOf() {
        return allOf;
    }

    public void setAllOf(List<Model> allOf) {
        this.allOf = allOf;
    }

    public Model getParent() {
        return parent;
    }

    @JsonIgnore
    public void setParent(Model model) {
        this.parent = model;
        if (!allOf.contains(model)) {
            this.allOf.add(model);
        }
    }

    public Model getChild() {
        return child;
    }

    @JsonIgnore
    public void setChild(Model model) {
        this.child = model;
        if (!allOf.contains(model)) {
            this.allOf.add(model);
        }
    }

    public List<RefModel> getInterfaces() {
        return interfaces;
    }

    @JsonIgnore
    public void setInterfaces(List<RefModel> interfaces) {
        this.interfaces = interfaces;
        for (RefModel model : interfaces) {
            if (!allOf.contains(model)) {
                allOf.add(model);
            }
        }
    }
    
    public Object clone() {
        ComposedModel cloned = new ComposedModel();
        super.cloneTo(cloned);
        cloned.allOf = this.allOf;
        cloned.parent = this.parent;
        cloned.child = this.child;
        cloned.interfaces = this.interfaces;
        cloned.description = this.description;
        cloned.example = this.example;

        return cloned;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = super.hashCode();
        result = prime * result + ((allOf == null) ? 0 : allOf.hashCode());
        result = prime * result + ((child == null) ? 0 : child.hashCode());
        result = prime * result
                + ((description == null) ? 0 : description.hashCode());
        result = prime * result + ((example == null) ? 0 : example.hashCode());
        result = prime * result
                + ((interfaces == null) ? 0 : interfaces.hashCode());
        result = prime * result + ((parent == null) ? 0 : parent.hashCode());
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
        ComposedModel other = (ComposedModel) obj;
        if (allOf == null) {
            if (other.allOf != null) {
                return false;
            }
        } else if (!allOf.equals(other.allOf)) {
            return false;
        }
        if (child == null) {
            if (other.child != null) {
                return false;
            }
        } else if (!child.equals(other.child)) {
            return false;
        }
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
        if (interfaces == null) {
            if (other.interfaces != null) {
                return false;
            }
        } else if (!interfaces.equals(other.interfaces)) {
            return false;
        }
        if (parent == null) {
            if (other.parent != null) {
                return false;
            }
        } else if (!parent.equals(other.parent)) {
            return false;
        }
        return true;
    }
}

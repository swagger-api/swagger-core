package io.swagger.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.models.properties.Property;
import io.swagger.models.refs.GenericRef;
import io.swagger.models.refs.RefFormat;
import io.swagger.models.refs.RefType;

import java.util.Map;

public class RefModel implements Model {
    private GenericRef genericRef;
    private String description;
    private ExternalDocs externalDocs;
    private Map<String, Property> properties;
    private Object example;
    private String title;

    public RefModel() {
    }

    public RefModel(String ref) {
        set$ref(ref);
    }

    public RefModel asDefault(String ref) {
        this.set$ref(RefType.DEFINITION.getInternalPrefix() + ref);
        return this;
    }

    // not allowed in a $ref
    @JsonIgnore
    @Override
    public String getTitle() {
        return title;
    }

    @Override
    public void setTitle(String title) {
        this.title = title;
    }

    // not allowed in a $ref
    @JsonIgnore
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @JsonIgnore
    public Map<String, Property> getProperties() {
        return properties;
    }

    public void setProperties(Map<String, Property> properties) {
        this.properties = properties;
    }

    @JsonIgnore
    public String getSimpleRef() {
        return genericRef.getSimpleRef();
    }

    public String get$ref() {
        return genericRef.getRef();
    }

    public void set$ref(String ref) {
        this.genericRef = new GenericRef(RefType.DEFINITION, ref);
    }

    @JsonIgnore
    public RefFormat getRefFormat() {
        return this.genericRef.getFormat();
    }

    @JsonIgnore
    public Object getExample() {
        return example;
    }

    public void setExample(Object example) {
        this.example = example;
    }

    @JsonIgnore
    public ExternalDocs getExternalDocs() {
        return externalDocs;
    }

    public void setExternalDocs(ExternalDocs value) {
        externalDocs = value;
    }

    public Object clone() {
        RefModel cloned = new RefModel();
        cloned.genericRef = this.genericRef; //GenericRef is an immutable class
        cloned.description = this.description;
        cloned.properties = this.properties;
        cloned.example = this.example;

        return cloned;
    }

    @Override
    @JsonIgnore
    public Map<String, Object> getVendorExtensions() {
        return null;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result
                + ((description == null) ? 0 : description.hashCode());
        result = prime * result + ((example == null) ? 0 : example.hashCode());
        result = prime * result
                + ((externalDocs == null) ? 0 : externalDocs.hashCode());
        result = prime * result
                + ((properties == null) ? 0 : properties.hashCode());
        result = prime * result + ((genericRef == null) ? 0 : genericRef.hashCode());
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
        RefModel other = (RefModel) obj;
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
        if (externalDocs == null) {
            if (other.externalDocs != null) {
                return false;
            }
        } else if (!externalDocs.equals(other.externalDocs)) {
            return false;
        }
        if (properties == null) {
            if (other.properties != null) {
                return false;
            }
        } else if (!properties.equals(other.properties)) {
            return false;
        }
        if (genericRef == null) {
            if (other.genericRef != null) {
                return false;
            }
        } else if (!genericRef.equals(other.genericRef)) {
            return false;
        }
        return true;
    }

    @Override
    @JsonIgnore
    public String getReference() {
        return genericRef.getRef();
    }

    @Override
    public void setReference(String reference) {
        this.genericRef = new GenericRef(RefType.DEFINITION, reference);
    }
}
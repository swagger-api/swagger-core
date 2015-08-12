package io.swagger.models.properties;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.models.refs.GenericRef;
import io.swagger.models.refs.RefConstants;
import io.swagger.models.refs.RefFormat;
import io.swagger.models.refs.RefType;

public class RefProperty extends AbstractProperty implements Property {
    private static final String TYPE = "ref";
    private GenericRef genericRef;

    public RefProperty() {
        setType(TYPE);
    }

    public RefProperty(String ref) {
        this();
        set$ref(ref);
    }

    public static boolean isType(String type, String format) {
        if (TYPE.equals(type)) {
            return true;
        } else {
            return false;
        }
    }

    public RefProperty asDefault(String ref) {
        this.set$ref(RefConstants.INTERNAL_DEFINITION_PREFIX + ref);
        return this;
    }

    public RefProperty description(String description) {
        this.setDescription(description);
        return this;
    }

    @Override
    @JsonIgnore
    public String getType() {
        return this.type;
    }

    @Override
    @JsonIgnore
    public void setType(String type) {
        this.type = type;
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
    public String getSimpleRef() {
        return genericRef.getSimpleRef();
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = super.hashCode();
        result = prime * result + ((genericRef == null) ? 0 : genericRef.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (!super.equals(obj)) {
            return false;
        }
        if (!(obj instanceof RefProperty)) {
            return false;
        }
        RefProperty other = (RefProperty) obj;
        if (genericRef == null) {
            if (other.genericRef != null) {
                return false;
            }
        } else if (!genericRef.equals(other.genericRef)) {
            return false;
        }
        return true;
    }
}
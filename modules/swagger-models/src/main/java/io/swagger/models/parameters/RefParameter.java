package io.swagger.models.parameters;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.models.refs.GenericRef;
import io.swagger.models.refs.RefFormat;
import io.swagger.models.refs.RefType;

public class RefParameter extends AbstractParameter implements Parameter {
    private GenericRef genericRef;

    public RefParameter(String ref) {
        set$ref(ref);
    }

    public static boolean isType(String type, String format) {
        if ("$ref".equals(type)) {
            return true;
        } else {
            return false;
        }
    }

    public RefParameter asDefault(String ref) {
        this.set$ref(RefType.PARAMETER.getInternalPrefix() + ref);
        return this;
    }

    public RefParameter description(String description) {
        this.setDescription(description);
        return this;
    }

    public String get$ref() {
        return genericRef.getRef();
    }

    public void set$ref(String ref) {
        this.genericRef = new GenericRef(RefType.PARAMETER, ref);
    }

    @JsonIgnore
    public RefFormat getRefFormat() {
        return this.genericRef.getFormat();
    }

    @Override
    @JsonIgnore
    public boolean getRequired() {
        return required;
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
        if (this == obj) {
            return true;
        }
        if (!super.equals(obj)) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        RefParameter other = (RefParameter) obj;
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
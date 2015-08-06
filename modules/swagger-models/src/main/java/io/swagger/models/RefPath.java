package io.swagger.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.models.parameters.Parameter;
import io.swagger.models.refs.GenericRef;
import io.swagger.models.refs.RefFormat;
import io.swagger.models.refs.RefType;

import java.util.List;
import java.util.Map;

/**
 * Created by Helmsdown on 7/8/15.
 *
 * This class extends directly from Path for now. At some future date we will need
 * to make {@link io.swagger.models.Path} an interface to follow the pattern established by
 * {@link io.swagger.models.Model}, {@link io.swagger.models.properties.Property} and {@link io.swagger.models.parameters.Parameter}
 */
public class RefPath extends Path {

    private GenericRef genericRef;

    public RefPath() {
    }

    public RefPath(String ref) {
        set$ref(ref);
    }

    public void set$ref(String ref) {
        this.genericRef = new GenericRef(RefType.PATH, ref);
    }

    public String get$ref() {
        return genericRef.getRef();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        RefPath refPath = (RefPath) o;

        return !(genericRef != null ? !genericRef.equals(refPath.genericRef) : refPath.genericRef != null);

    }

    @Override
    public int hashCode() {
        return genericRef != null ? genericRef.hashCode() : 0;
    }

    @JsonIgnore
    public RefFormat getRefFormat() {
        return this.genericRef.getFormat();
    }

}

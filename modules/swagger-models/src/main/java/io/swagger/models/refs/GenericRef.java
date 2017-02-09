package io.swagger.models.refs;

/**
 * A class the encapsulates logic that is common to RefModel, RefParameter, and RefProperty.
 */
public class GenericRef {
    private RefFormat format;
    private RefType type;
    private String ref;
    private String simpleRef;

    public GenericRef(){}

    public GenericRef(RefType type, String ref) {
        this.format = computeRefFormat(ref);
        this.type = type;

        if (format == RefFormat.INTERNAL && !ref.startsWith("#/")) {
            /* this is an internal path that did not start with a #/, we must be in some of ModelResolver code
            while currently relies on the ability to create RefModel/RefProperty objects via a constructor call like
            1) new RefModel("Animal")..and expects get$ref to return #/definitions/Animal
            2) new RefModel("http://blah.com/something/file.json")..and expects get$ref to turn the URL
             */
            this.ref = type.getInternalPrefix() + ref;
        } else {
            this.ref = ref;
        }

        this.simpleRef = computeSimpleRef(this.ref, format, type);
    }

    public RefFormat getFormat() {
        return format;
    }

    public RefType getType() {
        return type;
    }

    public String getRef() {
        return ref;
    }

    public String getSimpleRef() {
        return simpleRef;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof GenericRef)) {
            return false;
        }

        GenericRef that = (GenericRef) o;

        if (format != that.format) {
            return false;
        }
        if (type != that.type) {
            return false;
        }
        if (ref != null ? !ref.equals(that.ref) : that.ref != null) {
            return false;
        }
        return simpleRef != null ? simpleRef.equals(that.simpleRef) : that.simpleRef == null;

    }

    @Override
    public int hashCode() {
        int result = format != null ? format.hashCode() : 0;
        result = 31 * result + (type != null ? type.hashCode() : 0);
        result = 31 * result + (ref != null ? ref.hashCode() : 0);
        result = 31 * result + (simpleRef != null ? simpleRef.hashCode() : 0);
        return result;
    }

    private static String computeSimpleRef(String ref, RefFormat format, RefType type) {
        String result = ref;
        //simple refs really only apply to internal refs
        if (format == RefFormat.INTERNAL) {
            String prefix = type.getInternalPrefix();
            result = ref.substring(prefix.length());
        }

        return result;
    }

    private static RefFormat computeRefFormat(String ref) {
        RefFormat result = RefFormat.INTERNAL;
        if (ref.startsWith("http:") || ref.startsWith("https:")) {
            result = RefFormat.URL;
        } else if (ref.startsWith("#/")) {
            result = RefFormat.INTERNAL;
        } else if (ref.startsWith(".") || ref.startsWith("/")) {
            result = RefFormat.RELATIVE;
        }

        return result;
    }

}

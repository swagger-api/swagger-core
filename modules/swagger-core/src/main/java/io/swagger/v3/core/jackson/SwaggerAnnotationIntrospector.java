package io.swagger.v3.core.jackson;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.Version;
import com.fasterxml.jackson.databind.AnnotationIntrospector;
import com.fasterxml.jackson.databind.introspect.Annotated;
import com.fasterxml.jackson.databind.introspect.AnnotatedClass;
import com.fasterxml.jackson.databind.introspect.AnnotatedMember;
import com.fasterxml.jackson.databind.jsontype.NamedType;
import io.swagger.v3.core.util.AnnotationsUtils;
import io.swagger.v3.oas.annotations.Hidden;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Schema;

import javax.xml.bind.annotation.XmlElement;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class SwaggerAnnotationIntrospector extends AnnotationIntrospector {
    private static final long serialVersionUID = 1L;

    @Override
    public Version version() {
        return PackageVersion.VERSION;
    }

    @Override
    public boolean hasIgnoreMarker(AnnotatedMember m) {
        Schema ann = m.getAnnotation(Schema.class);
        if (ann != null && ann.hidden()) {
            return true;
        }
        Hidden hidden = m.getAnnotation(Hidden.class);
        if (hidden != null) {
            return true;
        }
        return false;
    }

    @Override
    public Boolean hasRequiredMarker(AnnotatedMember m) {
        XmlElement elem = m.getAnnotation(XmlElement.class);
        if (elem != null) {
            if (elem.required()) {
                return true;
            }
        }
        JsonProperty jsonProperty = m.getAnnotation(JsonProperty.class);
        if (jsonProperty != null) {
            if (jsonProperty.required()) {
                return true;
            }
        }
        Schema ann = m.getAnnotation(Schema.class);
        if (ann != null) {
            if (ann.required()) {
                return ann.required();
            }
        }
        ArraySchema arraySchema = m.getAnnotation(ArraySchema.class);
        if (arraySchema != null) {
            if (arraySchema.arraySchema().required()) {
                return arraySchema.arraySchema().required();
            }
            if (arraySchema.schema().required()) {
                return arraySchema.schema().required();
            }
        }
        return null;
    }

    @Override
    public String findPropertyDescription(Annotated a) {
        Schema model = a.getAnnotation(Schema.class);
        if (model != null && !"".equals(model.description())) {
            return model.description();
        }

        return null;
    }

    @Override
    public List<NamedType> findSubtypes(Annotated a) {
        Schema schema = a.getAnnotation(Schema.class);
        if (schema == null) {
            final ArraySchema arraySchema = a.getAnnotation(ArraySchema.class);
            if (arraySchema != null) {
                schema = arraySchema.schema();
            }
        }

        if (AnnotationsUtils.hasSchemaAnnotation(schema)) {
            final Class<?>[] classes = schema.subTypes();
            final List<NamedType> names = new ArrayList<>(classes.length);
            for (Class<?> subType : classes) {
                names.add(new NamedType(subType));
            }
            if (!names.isEmpty()) {
                return names;
            }
        }

        return Collections.emptyList();
    }

    @Override
    public String findTypeName(AnnotatedClass ac) {
        io.swagger.v3.oas.annotations.media.Schema mp = AnnotationsUtils.getSchemaAnnotation(ac);
        // allow override of name from annotation
        if (mp != null && !mp.name().isEmpty()) {
            return mp.name();
        }

        return null;
    }
}

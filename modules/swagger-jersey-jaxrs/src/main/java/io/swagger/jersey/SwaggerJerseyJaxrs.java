package io.swagger.jersey;

import com.fasterxml.jackson.databind.type.TypeFactory;
import com.sun.jersey.multipart.FormDataParam;
import io.swagger.converter.ModelConverters;
import io.swagger.jaxrs.ext.AbstractSwaggerExtension;
import io.swagger.jaxrs.ext.SwaggerExtension;
import io.swagger.models.parameters.FormParameter;
import io.swagger.models.parameters.Parameter;
import io.swagger.models.properties.Property;

import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

public class SwaggerJerseyJaxrs extends AbstractSwaggerExtension {

    @Override
    public List<Parameter> extractParameters(List<Annotation> annotations, Type type, Set<Type> typesToSkip,
                                             Iterator<SwaggerExtension> chain) {
        if (shouldIgnoreType(type, typesToSkip)) {
            // stop the processing chain
            return Collections.emptyList();
        }
        for (Annotation annotation : annotations) {
            if (annotation instanceof FormDataParam) {
                final FormDataParam fd = (FormDataParam) annotation;
                final Class<?> cls = TypeFactory.defaultInstance().constructType(type).getRawClass();
                final Parameter param;
                if (java.io.InputStream.class.isAssignableFrom(cls)) {
                    param = new FormParameter().type("file").name(fd.value());
                } else {
                    FormParameter fp = new FormParameter().name(fd.value());
                    Property schema = ModelConverters.getInstance().readAsProperty(type);
                    if (schema != null) {
                        fp.setProperty(schema);
                    }
                    param = fp;
                }
                return Collections.singletonList(param);
            }
        }
        return super.extractParameters(annotations, type, typesToSkip, chain);
    }

    @Override
    protected boolean shouldIgnoreClass(Class<?> cls) {
        for (Class<?> item : Arrays.asList(com.sun.jersey.core.header.FormDataContentDisposition.class, com.sun.jersey.multipart.FormDataBodyPart.class)) {
            if (item.isAssignableFrom(cls)) {
                return true;
            }
        }
        return false;
    }
}

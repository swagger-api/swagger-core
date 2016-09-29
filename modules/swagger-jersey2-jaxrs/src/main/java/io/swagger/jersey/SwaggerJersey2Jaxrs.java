package io.swagger.jersey;

import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import java.util.*;

import javax.ws.rs.BeanParam;

import org.glassfish.jersey.media.multipart.FormDataParam;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.BeanDescription;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.introspect.AnnotatedField;
import com.fasterxml.jackson.databind.introspect.AnnotatedMethod;
import com.fasterxml.jackson.databind.introspect.BeanPropertyDefinition;


import io.swagger.converter.ModelConverters;
import io.swagger.jaxrs.ext.AbstractSwaggerExtension;
import io.swagger.jaxrs.ext.SwaggerExtension;
import io.swagger.jaxrs.ext.SwaggerExtensions;
import io.swagger.models.parameters.FormParameter;
import io.swagger.models.parameters.Parameter;
import io.swagger.models.properties.Property;
import io.swagger.util.ParameterProcessor;

/**
 * Swagger extension for handling JAX-RS 2.0 processing.
 */
public class SwaggerJersey2Jaxrs extends AbstractSwaggerExtension {
    private static final ObjectMapper mapper = new ObjectMapper();
    static {
      mapper.setVisibility(PropertyAccessor.ALL, Visibility.NONE);
      mapper.setVisibility(PropertyAccessor.FIELD, Visibility.ANY);
      mapper.setVisibility(PropertyAccessor.SETTER, Visibility.ANY);
    }

    @Override
    public List<Parameter> extractParameters(final List<Annotation> annotations, final Type type, final Set<Type> typesToSkip, final Iterator<SwaggerExtension> chain) {
        List<Parameter> parameters = new ArrayList<Parameter>();

        if (shouldIgnoreType(type, typesToSkip)) {
            return parameters;
        }
        for (final Annotation annotation : annotations) {
            // just handle the jersey specific annotation
            if (annotation instanceof FormDataParam) {
                FormDataParam fd = (FormDataParam) annotation;
                if (java.io.InputStream.class.isAssignableFrom(constructType(type).getRawClass())) {
                    final Parameter param = new FormParameter().type("file").name(fd.value());
                    parameters.add(param);
                } else {
                    final FormParameter fp = new FormParameter().name(fd.value());
                    final Property schema = ModelConverters.getInstance().readAsProperty(type);
                    if (schema != null) {
                        fp.setProperty(schema);
                    }
                    parameters.add(fp);
                }
            }
        }

        // Only call down to the other items in the chain if no parameters were produced
        if (parameters.isEmpty()) {
            parameters = super.extractParameters(annotations, type, typesToSkip, chain);
        }

        return parameters;
    }

    @Override
    protected boolean shouldIgnoreClass(Class<?> cls) {
        for (Class<?> item : Arrays.asList(org.glassfish.jersey.media.multipart.FormDataContentDisposition.class,
                org.glassfish.jersey.media.multipart.FormDataBodyPart.class)) {
            if (item.isAssignableFrom(cls)) {
                return true;
            }
        }
        return false;
    }
}

package io.swagger.jersey;

import io.swagger.converter.ModelConverters;
import io.swagger.jaxrs.ext.AbstractSwaggerExtension;
import io.swagger.jaxrs.ext.SwaggerExtension;
import io.swagger.jaxrs.ext.SwaggerExtensions;
import io.swagger.models.parameters.FormParameter;
import io.swagger.models.parameters.Parameter;
import io.swagger.models.properties.Property;
import io.swagger.util.Json;
import io.swagger.util.ParameterProcessor;

import com.fasterxml.jackson.databind.BeanDescription;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.introspect.AnnotatedField;
import com.fasterxml.jackson.databind.introspect.AnnotatedMethod;
import com.fasterxml.jackson.databind.introspect.BeanPropertyDefinition;
import org.glassfish.jersey.media.multipart.FormDataParam;

import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.ws.rs.BeanParam;

/**
 * Swagger extension for handling JAX-RS 2.0 processing.
 */
public class SwaggerJersey2Jaxrs extends AbstractSwaggerExtension {
    final ObjectMapper mapper = Json.mapper();

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

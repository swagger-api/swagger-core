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
            if (annotation instanceof BeanParam) {
                // Use Jackson's logic for processing Beans
                final BeanDescription beanDesc = mapper.getSerializationConfig().introspect(constructType(type));
                final List<BeanPropertyDefinition> properties = beanDesc.findProperties();

                for (final BeanPropertyDefinition propDef : properties) {
                    final AnnotatedField field = propDef.getField();
                    final AnnotatedMethod setter = propDef.getSetter();
                    final List<Annotation> paramAnnotations = new ArrayList<Annotation>();
                    final Iterator<SwaggerExtension> extensions = SwaggerExtensions.chain();
                    Type paramType = null;

                    // Gather the field's details
                    if (field != null) {
                        paramType = field.getGenericType();

                        for (final Annotation fieldAnnotation : field.annotations()) {
                            if (!paramAnnotations.contains(fieldAnnotation)) {
                                paramAnnotations.add(fieldAnnotation);
                            }
                        }
                    }

                    // Gather the setter's details but only the ones we need
                    if (setter != null) {
                        // Do not set the param class/type from the setter if the values are already identified
                        if (paramType == null) {
                            paramType = setter.getGenericParameterTypes() != null ? setter.getGenericParameterTypes()[0] : null;
                        }

                        for (final Annotation fieldAnnotation : setter.annotations()) {
                            if (!paramAnnotations.contains(fieldAnnotation)) {
                                paramAnnotations.add(fieldAnnotation);
                            }
                        }
                    }

                    // Re-process all Bean fields and let the default swagger-jaxrs/swagger-jersey-jaxrs processors do their thing
                    List<Parameter> extracted =
                            extensions.next().extractParameters(paramAnnotations, paramType, typesToSkip, extensions);

                    // since downstream processors won't know how to introspect @BeanParam, process here
                    for (Parameter param : extracted) {
                        if (ParameterProcessor.applyAnnotations(null, param, paramType, paramAnnotations) != null) {
                            parameters.add(param);
                        }
                    }
                }
            } else if (annotation instanceof FormDataParam) {
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

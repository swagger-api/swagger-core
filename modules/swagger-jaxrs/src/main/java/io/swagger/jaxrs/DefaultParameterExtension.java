package io.swagger.jaxrs;

import io.swagger.converter.ModelConverters;
import io.swagger.jaxrs.ext.AbstractSwaggerExtension;
import io.swagger.jaxrs.ext.SwaggerExtension;
import io.swagger.jaxrs.ext.SwaggerExtensions;
import io.swagger.models.parameters.CookieParameter;
import io.swagger.models.parameters.FormParameter;
import io.swagger.models.parameters.HeaderParameter;
import io.swagger.models.parameters.Parameter;
import io.swagger.models.parameters.PathParameter;
import io.swagger.models.parameters.QueryParameter;
import io.swagger.models.properties.ArrayProperty;
import io.swagger.models.properties.Property;
import io.swagger.models.properties.RefProperty;
import io.swagger.models.properties.StringProperty;
import io.swagger.util.Json;
import io.swagger.util.ParameterProcessor;

import javax.ws.rs.CookieParam;
import javax.ws.rs.FormParam;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.PathParam;
import javax.ws.rs.QueryParam;

import com.fasterxml.jackson.databind.BeanDescription;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.introspect.AnnotatedField;
import com.fasterxml.jackson.databind.introspect.AnnotatedMethod;
import com.fasterxml.jackson.databind.introspect.BeanPropertyDefinition;

import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

public class DefaultParameterExtension extends AbstractSwaggerExtension {
    // make jaxrs 2.0 classes optional
    private static Class<?> CLASS_BEAN_PARAM;
    static {
        try {
            CLASS_BEAN_PARAM = Class.forName("javax.ws.rs.BeanParam", true, DefaultParameterExtension.class.getClassLoader());
        } catch (Throwable t) {
            //ignore and assume no jsr399-api on classpath
        }
    }

    final ObjectMapper mapper = Json.mapper();

    @Override
    public List<Parameter> extractParameters(List<Annotation> annotations, Type type, Set<Type> typesToSkip, Iterator<SwaggerExtension> chain) {
        if (shouldIgnoreType(type, typesToSkip)) {
            return new ArrayList<Parameter>();
        }

        List<Parameter> parameters = new ArrayList<Parameter>();
        Parameter parameter = null;
        for (Annotation annotation : annotations) {
            if (annotation instanceof QueryParam) {
                QueryParam param = (QueryParam) annotation;
                QueryParameter qp = new QueryParameter()
                        .name(param.value());

                Property schema = createProperty(type);
                if (schema != null) {
                    qp.setProperty(schema);
                }
                parameter = qp;
            } else if (annotation instanceof PathParam) {
                PathParam param = (PathParam) annotation;
                PathParameter pp = new PathParameter()
                        .name(param.value());
                Property schema = createProperty(type);
                if (schema != null) {
                    pp.setProperty(schema);
                }
                parameter = pp;
            } else if (annotation instanceof HeaderParam) {
                HeaderParam param = (HeaderParam) annotation;
                HeaderParameter hp = new HeaderParameter()
                        .name(param.value());
                Property schema = createProperty(type);
                if (schema != null) {
                    hp.setProperty(schema);
                }
                parameter = hp;
            } else if (annotation instanceof CookieParam) {
                CookieParam param = (CookieParam) annotation;
                CookieParameter cp = new CookieParameter()
                        .name(param.value());
                Property schema = createProperty(type);
                if (schema != null) {
                    cp.setProperty(schema);
                }
                parameter = cp;
            } else if (annotation instanceof FormParam) {
                FormParam param = (FormParam) annotation;
                FormParameter fp = new FormParameter()
                        .name(param.value());
                Property schema = createProperty(type);
                if (schema != null) {
                    fp.setProperty(schema);
                }
                parameter = fp;
            } else {
                handleAdditionalAnnotation(parameters, annotation, type, typesToSkip);
            }
        }
        if (parameter != null) {
            parameters.add(parameter);
        }

        return parameters;
    }

    /**
     * Adds additional annotation processing support 
     * 
     * @param parameters
     * @param annotation
     * @param type
     * @param typesToSkip
     */
    private void handleAdditionalAnnotation(List<Parameter> parameters, Annotation annotation, 
        final Type type, Set<Type> typesToSkip) {
        if (CLASS_BEAN_PARAM != null && CLASS_BEAN_PARAM.isAssignableFrom(annotation.getClass())) {
            // Use Jackson's logic for processing Beans
            final BeanDescription beanDesc = mapper.getSerializationConfig().introspect(constructType(type));
            final List<BeanPropertyDefinition> properties = beanDesc.findProperties();

            for (final BeanPropertyDefinition propDef : properties) {
                final AnnotatedField field = propDef.getField();
                final AnnotatedMethod setter = propDef.getSetter();
                final AnnotatedMethod getter = propDef.getGetter();
                final List<Annotation> paramAnnotations = new ArrayList<Annotation>();
                final Iterator<SwaggerExtension> extensions = SwaggerExtensions.chain();
                Type paramType = null;

                // Gather the field's details
                if (field != null) {
                    paramType = field.getRawType();

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
                        paramType = setter.getRawParameterTypes() != null ? setter.getRawParameterTypes()[0] : null;
                    }

                    for (final Annotation fieldAnnotation : setter.annotations()) {
                        if (!paramAnnotations.contains(fieldAnnotation)) {
                            paramAnnotations.add(fieldAnnotation);
                        }
                    }
                }
                
                // Gather the getter's details but only the ones we need
                if (getter != null) {
                    // Do not set the param class/type from the getter if the values are already identified
                    if (paramType == null) {
                        paramType = getter.getRawReturnType();
                    }

                    for (final Annotation fieldAnnotation : getter.annotations()) {
                        if (!paramAnnotations.contains(fieldAnnotation)) {
                            paramAnnotations.add(fieldAnnotation);
                        }
                    }
                }

                if (paramType == null) {
                    continue;
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
        }
    }

    @Override
    protected boolean shouldIgnoreClass(Class<?> cls) {
        return cls.getName().startsWith("javax.ws.rs.");
    }

    private Property createProperty(Type type) {
        return enforcePrimitive(ModelConverters.getInstance().readAsProperty(type), 0);
    }

    private Property enforcePrimitive(Property in, int level) {
        if (in instanceof RefProperty) {
            return new StringProperty();
        }
        if (in instanceof ArrayProperty) {
            if (level == 0) {
                final ArrayProperty array = (ArrayProperty) in;
                array.setItems(enforcePrimitive(array.getItems(), level + 1));
            } else {
                return new StringProperty();
            }
        }
        return in;
    }
}

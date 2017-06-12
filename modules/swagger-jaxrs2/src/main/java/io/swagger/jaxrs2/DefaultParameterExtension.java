package io.swagger.jaxrs2;

import com.fasterxml.jackson.databind.BeanDescription;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.introspect.AnnotatedField;
import com.fasterxml.jackson.databind.introspect.AnnotatedMethod;
import com.fasterxml.jackson.databind.introspect.BeanPropertyDefinition;
import io.swagger.jaxrs2.ext.AbstractOpenAPIExtension;
import io.swagger.jaxrs2.ext.OpenAPIExtension;
import io.swagger.jaxrs2.ext.OpenAPIExtensions;
import io.swagger.oas.models.parameters.Parameter;
import io.swagger.util.Json;
import io.swagger.util.ParameterProcessor;

import javax.ws.rs.CookieParam;
import javax.ws.rs.FormParam;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.PathParam;
import javax.ws.rs.QueryParam;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

public class DefaultParameterExtension extends AbstractOpenAPIExtension {
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
    public List<Parameter> extractParameters(List<Annotation> annotations, Type type, Set<Type> typesToSkip, Iterator<OpenAPIExtension> chain) {
        if (shouldIgnoreType(type, typesToSkip)) {
            return new ArrayList<>();
        }

        List<Parameter> parameters = new ArrayList<Parameter>();
        Parameter parameter = null;
        for (Annotation annotation : annotations) {
            if (annotation instanceof QueryParam) {
                QueryParam param = (QueryParam) annotation;
                Parameter qp = new Parameter();
                qp.name(param.value());
                parameter = qp;
            } else if (annotation instanceof PathParam) {
                PathParam param = (PathParam) annotation;
                Parameter pp = new Parameter();
                pp.name(param.value());
                parameter = pp;
            } else if (annotation instanceof HeaderParam) {
                HeaderParam param = (HeaderParam) annotation;
                Parameter pp = new Parameter();
                pp.name(param.value());
                parameter = pp;
            } else if (annotation instanceof CookieParam) {
                CookieParam param = (CookieParam) annotation;
                Parameter pp = new Parameter();
                pp.name(param.value());
                parameter = pp;
            } else if (annotation instanceof FormParam) {
                FormParam param = (FormParam) annotation;
                Parameter pp = new Parameter();
                pp.name(param.value());
                parameter = pp;
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
                final Iterator<OpenAPIExtension> extensions = OpenAPIExtensions.chain();
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

    /*private Property createProperty(Type type) {
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
    }*/
}

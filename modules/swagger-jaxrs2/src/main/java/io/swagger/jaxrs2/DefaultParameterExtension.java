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

import javax.ws.rs.BeanParam;
import javax.ws.rs.CookieParam;
import javax.ws.rs.FormParam;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.MatrixParam;
import javax.ws.rs.PathParam;
import javax.ws.rs.QueryParam;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

public class DefaultParameterExtension extends AbstractOpenAPIExtension {
    private static String QUERY_PARAM = "query";
    private static String HEADER_PARAM = "header";
    private static String COOKIE_PARAM = "cookie";
    private static String PATH_PARAM = "path";
    private static String FORM_PARAM = "form";
    private static String MATRIX_PARAM = "matrix";
    private static String BEAN_PARAM = "bean";

    final ObjectMapper mapper = Json.mapper();

    @Override
    public List<Parameter> extractParameters(List<Annotation> annotations, Type type, Set<Type> typesToSkip, Iterator<OpenAPIExtension> chain) {
        if (shouldIgnoreType(type, typesToSkip)) {
            return new ArrayList<>();
        }

        List<Parameter> parameters = new ArrayList<>();
        Parameter parameter = null;
        for (Annotation annotation : annotations) {
            if (annotation instanceof QueryParam) {
                QueryParam param = (QueryParam) annotation;
                Parameter qp = new Parameter();
                qp.setIn(QUERY_PARAM);
                qp.setName(param.value());
                parameter = qp;
            } else if (annotation instanceof PathParam) {
                PathParam param = (PathParam) annotation;
                Parameter pp = new Parameter();
                pp.setIn(PATH_PARAM);
                pp.setName(param.value());
                parameter = pp;
            } else if (annotation instanceof HeaderParam) {
                HeaderParam param = (HeaderParam) annotation;
                Parameter pp = new Parameter();
                pp.setIn(HEADER_PARAM);
                pp.setName(param.value());
                parameter = pp;
            } else if (annotation instanceof CookieParam) {
                CookieParam param = (CookieParam) annotation;
                Parameter pp = new Parameter();
                pp.setIn(COOKIE_PARAM);
                pp.setName(param.value());
                parameter = pp;
            } else if (annotation instanceof FormParam) {
                FormParam param = (FormParam) annotation;
                Parameter pp = new Parameter();
                pp.setIn(FORM_PARAM);
                pp.setName(param.value());
                parameter = pp;
            }  else if (annotation instanceof FormParam) {
                FormParam param = (FormParam) annotation;
                Parameter pp = new Parameter();
                pp.setIn(FORM_PARAM);
                pp.setName(param.value());
                parameter = pp;
            }  else if (annotation instanceof MatrixParam) {
                MatrixParam param = (MatrixParam) annotation;
                Parameter pp = new Parameter();
                pp.setIn(MATRIX_PARAM);
                pp.setName(param.value());
                parameter = pp;
            } else if (annotation instanceof BeanParam) {
                Parameter pp = new Parameter();
                pp.setIn(BEAN_PARAM);
                parameter = pp;
            } else if (annotation instanceof io.swagger.oas.annotations.Parameter) {
                Parameter pp = new Parameter();
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
        if (BeanParam.class.isAssignableFrom(annotation.getClass())) {
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

package io.swagger.v3.jaxrs2;

import com.fasterxml.jackson.annotation.JsonView;
import com.fasterxml.jackson.databind.BeanDescription;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.introspect.AnnotatedField;
import com.fasterxml.jackson.databind.introspect.AnnotatedMethod;
import com.fasterxml.jackson.databind.introspect.AnnotationMap;
import com.fasterxml.jackson.databind.introspect.BeanPropertyDefinition;

import io.swagger.v3.core.util.Json;
import io.swagger.v3.core.util.ParameterProcessor;
import io.swagger.v3.jaxrs2.ext.AbstractOpenAPIExtension;
import io.swagger.v3.jaxrs2.ext.OpenAPIExtension;
import io.swagger.v3.jaxrs2.ext.OpenAPIExtensions;
import io.swagger.v3.oas.annotations.Hidden;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.parameters.Parameter;
import org.apache.commons.lang3.StringUtils;

import javax.ws.rs.BeanParam;
import javax.ws.rs.CookieParam;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.PathParam;
import javax.ws.rs.QueryParam;
import javax.ws.rs.MatrixParam;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

public class DefaultParameterExtension extends AbstractOpenAPIExtension {
    private static final String QUERY_PARAM = "query";
    private static final String HEADER_PARAM = "header";
    private static final String COOKIE_PARAM = "cookie";
    private static final String PATH_PARAM = "path";

    final ObjectMapper mapper = Json.mapper();

    @Override
    public ResolvedParameter extractParameters(List<Annotation> annotations,
                                               Type type,
                                               Set<Type> typesToSkip,
                                               Components components,
                                               javax.ws.rs.Consumes classConsumes,
                                               javax.ws.rs.Consumes methodConsumes,
                                               boolean includeRequestBody,
                                               JsonView jsonViewAnnotation,
                                               Iterator<OpenAPIExtension> chain) {
        if (shouldIgnoreType(type, typesToSkip)) {
            return new ResolvedParameter();
        }


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
            } else if (annotation instanceof MatrixParam) {
                MatrixParam param = (MatrixParam) annotation;
                Parameter pp = new Parameter();
                pp.setIn(PATH_PARAM);
                pp.setStyle(Parameter.StyleEnum.MATRIX);
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
            } else if (annotation instanceof io.swagger.v3.oas.annotations.Parameter) {
                if (((io.swagger.v3.oas.annotations.Parameter) annotation).hidden()) {
                    return new ResolvedParameter();
                }
                if (parameter == null) {
                    parameter = new Parameter();
                }
                if (StringUtils.isNotBlank(((io.swagger.v3.oas.annotations.Parameter) annotation).ref())) {
                    parameter.$ref(((io.swagger.v3.oas.annotations.Parameter) annotation).ref());
                }
            } else {
                List<Parameter> formParameters = new ArrayList<>();
                List<Parameter> parameters = new ArrayList<>();
                if (handleAdditionalAnnotation(parameters, formParameters, annotation, type, typesToSkip, classConsumes, methodConsumes, components, includeRequestBody, jsonViewAnnotation)) {
                    ResolvedParameter extractParametersResult = new ResolvedParameter();
                    extractParametersResult.parameters.addAll(parameters);
                    extractParametersResult.formParameters.addAll(formParameters);
                    return extractParametersResult;
                }
            }
        }
        List<Parameter> parameters = new ArrayList<>();
        ResolvedParameter extractParametersResult = new ResolvedParameter();

        if (parameter != null && (StringUtils.isNotBlank(parameter.getIn()) || StringUtils.isNotBlank(parameter.get$ref()))) {
            parameters.add(parameter);
        } else if (includeRequestBody) {
            Parameter unknownParameter = ParameterProcessor.applyAnnotations(
                    null,
                    type,
                    annotations,
                    components,
                    classConsumes == null ? new String[0] : classConsumes.value(),
                    methodConsumes == null ? new String[0] : methodConsumes.value(), jsonViewAnnotation, configuration);
            if (unknownParameter != null) {
                if (StringUtils.isNotBlank(unknownParameter.getIn()) && !"form".equals(unknownParameter.getIn())) {
                    extractParametersResult.parameters.add(unknownParameter);
                } else if ("form".equals(unknownParameter.getIn())) {
                    unknownParameter.setIn(null);
                    extractParametersResult.formParameters.add(unknownParameter);
                } else {            // return as request body
                    extractParametersResult.requestBody = unknownParameter;
                }
            }
        }
        for (Parameter p : parameters) {
            Parameter processedParameter = ParameterProcessor.applyAnnotations(
                    p,
                    type,
                    annotations,
                    components,
                    classConsumes == null ? new String[0] : classConsumes.value(),
                    methodConsumes == null ? new String[0] : methodConsumes.value(),
                    jsonViewAnnotation,
                    openapi31,
                    this.schemaResolution);
            if (processedParameter != null) {
                extractParametersResult.parameters.add(processedParameter);
            }
        }
        return extractParametersResult;
    }

    /**
     * Adds additional annotation processing support
     *
     * @param parameters
     * @param annotation
     * @param type
     * @param typesToSkip
     */

    private boolean handleAdditionalAnnotation(List<Parameter> parameters, List<Parameter> formParameters, Annotation annotation,
                                               final Type type, Set<Type> typesToSkip, javax.ws.rs.Consumes classConsumes,
                                               javax.ws.rs.Consumes methodConsumes, Components components, boolean includeRequestBody, JsonView jsonViewAnnotation) {
        boolean processed = false;
        if (BeanParam.class.isAssignableFrom(annotation.getClass())) {
            // Use Jackson's logic for processing Beans
            final BeanDescription beanDesc = mapper.getSerializationConfig().introspect(constructType(type));
            final List<BeanPropertyDefinition> properties = beanDesc.findProperties();

            for (final BeanPropertyDefinition propDef : properties) {
                final AnnotatedField field = propDef.getField();
                final AnnotatedMethod setter = propDef.getSetter();
                final AnnotatedMethod getter = propDef.getGetter();
                final List<Annotation> paramAnnotations = new ArrayList<>();
                final Iterator<OpenAPIExtension> extensions = OpenAPIExtensions.chain();
                Type paramType = null;

                // Gather the field's details
                if (field != null) {
                    paramType = field.getType();
                    AnnotationMap annotationMap = field.getAllAnnotations();
                    if (annotationMap != null) {
                        for (final Annotation fieldAnnotation : annotationMap.annotations()) {
                            if (!paramAnnotations.contains(fieldAnnotation)) {
                                paramAnnotations.add(fieldAnnotation);
                            }
                        }
                    }
                }

                // Gather the setter's details but only the ones we need
                if (setter != null) {
                    // Do not set the param class/type from the setter if the values are already identified
                    if (paramType == null) {
                        // paramType will stay null if there is no parameter
                        paramType = setter.getParameterType(0);
                    }
                    AnnotationMap annotationMap = setter.getAllAnnotations();
                    if (annotationMap != null) {
                        for (final Annotation fieldAnnotation : annotationMap.annotations()) {
                            if (!paramAnnotations.contains(fieldAnnotation)) {
                                paramAnnotations.add(fieldAnnotation);
                            }
                        }
                    }
                }

                // Gather the getter's details but only the ones we need
                if (getter != null) {
                    // Do not set the param class/type from the getter if the values are already identified
                    if (paramType == null) {
                        paramType = getter.getType();
                    }
                    AnnotationMap annotationMap = getter.getAllAnnotations();
                    if (annotationMap != null) {
                        for (final Annotation fieldAnnotation : annotationMap.annotations()) {
                            if (!paramAnnotations.contains(fieldAnnotation)) {
                                paramAnnotations.add(fieldAnnotation);
                            }
                        }
                    }
                }

                if (paramType == null) {
                    continue;
                }

                // skip hidden properties
                boolean hidden  = false;
                for (Annotation a : paramAnnotations) {
                    if (a instanceof io.swagger.v3.oas.annotations.media.Schema) {
                        if (((io.swagger.v3.oas.annotations.media.Schema) a).hidden()) {
                            hidden = true;
                            break;
                        };
                    } else if (a instanceof Hidden) {
                        hidden = true;
                        break;
                    }
                }
                if (hidden) {
                    continue;
                }
                // Re-process all Bean fields and let the default swagger-jaxrs/swagger-jersey-jaxrs processors do their thing
                ResolvedParameter resolvedParameter = extensions.next().extractParameters(
                        paramAnnotations,
                        paramType,
                        typesToSkip,
                        components,
                        classConsumes,
                        methodConsumes,
                        includeRequestBody,
                        jsonViewAnnotation,
                        extensions);

                List<Parameter> extractedParameters =
                        resolvedParameter.parameters;

                for (Parameter p : extractedParameters) {
                    if (p != null) {
                        parameters.add(p);
                    }
                }

                List<Parameter> extractedFormParameters =
                        resolvedParameter.formParameters;

                for (Parameter p : extractedFormParameters) {
                    if (p != null) {
                        formParameters.add(p);
                    }
                }

                processed = true;
            }
        }
        return processed;
    }

    @Override
    protected boolean shouldIgnoreClass(Class<?> cls) {
        return cls.getName().startsWith("javax.ws.rs.");
    }

}

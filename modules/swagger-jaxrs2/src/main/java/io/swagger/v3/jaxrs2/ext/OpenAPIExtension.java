package io.swagger.v3.jaxrs2.ext;

import com.fasterxml.jackson.annotation.JsonView;
import io.swagger.v3.jaxrs2.ResolvedParameter;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.Operation;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

public interface OpenAPIExtension {

    String extractOperationMethod(Method method, Iterator<OpenAPIExtension> chain);

    ResolvedParameter extractParameters(List<Annotation> annotations, Type type, Set<Type> typesToSkip, Components components,
                                        javax.ws.rs.Consumes classConsumes, javax.ws.rs.Consumes methodConsumes, boolean includeRequestBody, JsonView jsonViewAnnotation, Iterator<OpenAPIExtension> chain);

    /**
     * Decorates operation with additional vendor based extensions.
     *
     * @param operation the operation, build from swagger definition
     * @param method    the method for additional scan
     * @param chain     the chain with swagger extensions to process
     */
    void decorateOperation(Operation operation, Method method, Iterator<OpenAPIExtension> chain);
}

package io.swagger.jaxrs2.ext;

import io.swagger.oas.models.Operation;
import io.swagger.oas.models.parameters.Parameter;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

public interface OpenAPIExtension {

    String extractOperationMethod(Operation apiOperation, Method method, Iterator<OpenAPIExtension> chain);

    List<Parameter> extractParameters(List<Annotation> annotations, Type type, Set<Type> typesToSkip, Iterator<OpenAPIExtension> chain);

    /**
     * Decorates operation with additional vendor based extensions.
     *
     * @param operation the operation, build from swagger definition
     * @param method the method for additional scan
     * @param chain the chain with swagger extensions to process
     */
    void decorateOperation(Operation operation, Method method, Iterator<OpenAPIExtension> chain);
}

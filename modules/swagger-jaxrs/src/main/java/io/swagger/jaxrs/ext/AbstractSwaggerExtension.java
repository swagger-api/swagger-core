package io.swagger.jaxrs.ext;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.type.TypeFactory;

import io.swagger.annotations.ApiOperation;
import io.swagger.models.Operation;
import io.swagger.models.parameters.Parameter;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

public abstract class AbstractSwaggerExtension implements SwaggerExtension {

    @Override
    public String extractOperationMethod(ApiOperation apiOperation, Method method, Iterator<SwaggerExtension> chain) {
        if (chain.hasNext()) {
            return chain.next().extractOperationMethod(apiOperation, method, chain);
        } else {
            return null;
        }
    }

    @Override
    public List<Parameter> extractParameters(List<Annotation> annotations, Type type, Set<Type> typesToSkip,
                                             Iterator<SwaggerExtension> chain) {
        if (chain.hasNext()) {
            return chain.next().extractParameters(annotations, type, typesToSkip, chain);
        } else {
            return Collections.emptyList();
        }
    }
    
    @Override
    public void decorateOperation(Operation operation, Method method, Iterator<SwaggerExtension> chain) {
        if (chain.hasNext()) {
            chain.next().decorateOperation(operation, method, chain);
        } 
    }

    protected boolean shouldIgnoreClass(Class<?> cls) {
        return false;
    }

    protected boolean shouldIgnoreType(Type type, Set<Type> typesToSkip) {
        if (typesToSkip.contains(type)) {
            return true;
        }
        if (shouldIgnoreClass(constructType(type).getRawClass())) {
            typesToSkip.add(type);
            return true;
        }
        return false;
    }

    protected JavaType constructType(Type type) {
        return TypeFactory.defaultInstance().constructType(type);
    }
}

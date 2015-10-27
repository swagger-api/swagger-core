package io.swagger.servlet.extensions;

import io.swagger.models.Operation;
import io.swagger.servlet.ReaderContext;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.lang.reflect.Type;

public class ReaderExtension2 implements ReaderExtension {

    @Override
    public int getPriority() {
        return 20;
    }

    @Override
    public boolean isReadable(ReaderContext context) {
        return false;
    }

    @Override
    public void applyConsumes(ReaderContext context, Operation operation, Method method) {

    }

    @Override
    public void applyProduces(ReaderContext context, Operation operation, Method method) {

    }

    @Override
    public String getHttpMethod(ReaderContext context, Method method) {
        return null;
    }

    @Override
    public String getPath(ReaderContext context, Method method) {
        return null;
    }

    @Override
    public void applyOperationId(Operation operation, Method method) {

    }

    @Override
    public void applySummary(Operation operation, Method method) {

    }

    @Override
    public void applyDescription(Operation operation, Method method) {

    }

    @Override
    public void applySchemes(ReaderContext context, Operation operation, Method method) {

    }

    @Override
    public void setDeprecated(Operation operation, Method method) {

    }

    @Override
    public void applySecurityRequirements(ReaderContext context, Operation operation, Method method) {

    }

    @Override
    public void applyTags(ReaderContext context, Operation operation, Method method) {

    }

    @Override
    public void applyResponses(ReaderContext context, Operation operation, Method method) {

    }

    @Override
    public void applyParameters(ReaderContext context, Operation operation, Type type, Annotation[] annotations) {

    }

    @Override
    public void applyImplicitParameters(ReaderContext context, Operation operation, Method method) {

    }

    @Override
    public void applyExtensions(ReaderContext context, Operation operation, Method method) {

    }
}

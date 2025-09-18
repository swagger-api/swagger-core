package io.swagger.v3.core.converter;

import com.fasterxml.jackson.annotation.JsonView;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.media.Schema;

import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.function.Function;

public class AnnotatedType {
    private Type type;
    private String name;
    private Schema parent;
    private Function<AnnotatedType, Schema> jsonUnwrappedHandler;
    private boolean skipOverride;
    private boolean schemaProperty;
    private Annotation[] ctxAnnotations;
    private boolean resolveAsRef;
    private boolean resolveEnumAsRef;
    private JsonView jsonViewAnnotation;
    private boolean includePropertiesWithoutJSONView = true;
    private boolean skipSchemaName;
    private boolean skipJsonIdentity;
    private String propertyName;

    private Components components;

    public AnnotatedType() {
    }

    public AnnotatedType(Type type) {
        this.type = type;
    }

    public boolean isSkipOverride() {
        return skipOverride;
    }

    public void setSkipOverride(boolean skipOverride) {
        this.skipOverride = skipOverride;
    }

    public AnnotatedType skipOverride(boolean skipOverride) {
        this.skipOverride = skipOverride;
        return this;
    }

    public boolean isSkipJsonIdentity() {
        return skipJsonIdentity;
    }

    public void setSkipJsonIdentity(boolean skipJsonIdentity) {
        this.skipJsonIdentity = skipJsonIdentity;
    }

    public AnnotatedType skipJsonIdentity(boolean skipJsonIdentity) {
        this.skipJsonIdentity = skipJsonIdentity;
        return this;
    }

    public boolean isSkipSchemaName() {
        return skipSchemaName;
    }

    public void setSkipSchemaName(boolean skipSchemaName) {
        this.skipSchemaName = skipSchemaName;
    }

    public AnnotatedType skipSchemaName(boolean skipSchemaName) {
        this.skipSchemaName = skipSchemaName;
        return this;
    }

    public boolean isResolveAsRef() {
        return resolveAsRef;
    }

    public void setResolveAsRef(boolean resolveAsRef) {
        this.resolveAsRef = resolveAsRef;
    }

    public AnnotatedType resolveAsRef(boolean resolveAsRef) {
        this.resolveAsRef = resolveAsRef;
        return this;
    }

    public boolean isResolveEnumAsRef() {
        return resolveEnumAsRef;
    }

    public void setResolveEnumAsRef(boolean resolveEnumAsRef) {
        this.resolveEnumAsRef = resolveEnumAsRef;
    }

    public AnnotatedType resolveEnumAsRef(boolean resolveEnumAsRef) {
        this.resolveEnumAsRef = resolveEnumAsRef;
        return this;
    }

    public boolean isSchemaProperty() {
        return schemaProperty;
    }

    public void setSchemaProperty(boolean schemaProperty) {
        this.schemaProperty = schemaProperty;
    }

    public AnnotatedType schemaProperty(boolean schemaProperty) {
        this.schemaProperty = schemaProperty;
        return this;
    }

    public Function<AnnotatedType, Schema> getJsonUnwrappedHandler() {
        return jsonUnwrappedHandler;
    }

    public void setJsonUnwrappedHandler(Function<AnnotatedType, Schema> jsonUnwrappedHandler) {
        this.jsonUnwrappedHandler = jsonUnwrappedHandler;
    }

    public AnnotatedType jsonUnwrappedHandler(Function<AnnotatedType, Schema> jsonUnwrappedHandler) {
        this.jsonUnwrappedHandler = jsonUnwrappedHandler;
        return this;
    }

    public Schema getParent() {
        return parent;
    }

    public void setParent(Schema parent) {
        this.parent = parent;
    }

    public AnnotatedType parent(Schema parent) {
        this.parent = parent;
        return this;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public AnnotatedType name(String name) {
        this.name = name;
        return this;
    }

    public Annotation[] getCtxAnnotations() {
        return ctxAnnotations;
    }

    public void setCtxAnnotations(Annotation[] ctxAnnotations) {
        this.ctxAnnotations = ctxAnnotations;
    }

    public AnnotatedType ctxAnnotations(Annotation[] ctxAnnotations) {
        setCtxAnnotations(ctxAnnotations);
        return this;
    }

    public Components getComponents() {
        return components;
    }

    public void setComponents(Components components) {
        this.components = components;
    }

    public AnnotatedType components(Components components) {
        setComponents(components);
        return this;
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public AnnotatedType type(Type type) {
        setType(type);
        return this;
    }

    public JsonView getJsonViewAnnotation() {
        return jsonViewAnnotation;
    }

    public void setJsonViewAnnotation(JsonView jsonViewAnnotation) {
        this.jsonViewAnnotation = jsonViewAnnotation;
    }

    public AnnotatedType jsonViewAnnotation(JsonView jsonViewAnnotation) {
        this.jsonViewAnnotation = jsonViewAnnotation;
        return this;
    }

    public boolean isIncludePropertiesWithoutJSONView() {
        return includePropertiesWithoutJSONView;
    }

    public void setIncludePropertiesWithoutJSONView(boolean includePropertiesWithoutJSONView) {
        this.includePropertiesWithoutJSONView = includePropertiesWithoutJSONView;
    }

    public AnnotatedType includePropertiesWithoutJSONView(boolean includePropertiesWithoutJSONView) {
        this.includePropertiesWithoutJSONView = includePropertiesWithoutJSONView;
        return this;
    }

    /**
     * @since 2.0.4
     */
    public String getPropertyName() {
        return propertyName;
    }

    /**
     * @since 2.0.4
     */
    public void setPropertyName(String propertyName) {
        this.propertyName = propertyName;
    }

    /**
     * @since 2.0.4
     */
    public AnnotatedType propertyName(String propertyName) {
        this.propertyName = propertyName;
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AnnotatedType that = (AnnotatedType) o;
        return skipOverride == that.skipOverride &&
                schemaProperty == that.schemaProperty &&
                resolveAsRef == that.resolveAsRef &&
                resolveEnumAsRef == that.resolveEnumAsRef &&
                includePropertiesWithoutJSONView == that.includePropertiesWithoutJSONView &&
                skipSchemaName == that.skipSchemaName &&
                skipJsonIdentity == that.skipJsonIdentity &&
                Objects.equals(type, that.type) &&
                Objects.equals(name, that.name) &&
                Arrays.equals(ctxAnnotations, that.ctxAnnotations) &&
                Objects.equals(jsonViewAnnotation, that.jsonViewAnnotation);
    }

    @Override
    public int hashCode() {
        int result = Objects.hash(type, name, skipOverride, schemaProperty, resolveAsRef,
                resolveEnumAsRef, jsonViewAnnotation, includePropertiesWithoutJSONView, skipSchemaName,
                skipJsonIdentity);
        result = 31 * result + Arrays.hashCode(ctxAnnotations);
        return result;
    }
}

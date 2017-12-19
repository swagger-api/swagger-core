package io.swagger.v3.core.converter;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.introspect.Annotated;
import io.swagger.v3.oas.models.media.Schema;

import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import java.util.Iterator;
import java.util.List;
import java.util.function.BiFunction;

public interface ModelConverterContext {

    /**
     * needs to be called whenever a Model is defined which can be referenced from another
     * Model or Property
     *
     * @param name  the name of the model
     * @param model the Model
     */
    void defineModel(String name, Schema model);

    /**
     * needs to be called whenever a Schema is defined which can be referenced from another
     * Model or Property
     *
     * @param name     the name of the model
     * @param model    the Model
     * @param type     the Type
     * @param prevName the (optional) previous name
     */
    void defineModel(String name, Schema model, Type type, String prevName);

    /**
     * @param type The Schema
     * @return a Model representation of the Class. Any referenced models will be defined already.
     */
    Schema resolve(Type type);

    Schema resolve(Type type, Annotation[] annotations);

    Schema resolveAnnotatedType(Type type, List<Annotation> annotations, String elementName);

    Schema resolveAnnotatedType(
            Type type,
            Annotated member,
            String elementName,
            Schema parent,
            BiFunction<JavaType, Annotation[], Schema> jsonUnwrappedHandler,
            ModelConverterContext context,
            Iterator<ModelConverter> chain
    );

    /**
     * @return an Iterator of ModelConverters.  This iterator is not reused
     */
    public Iterator<ModelConverter> getConverters();
}
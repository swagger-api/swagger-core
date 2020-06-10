package io.swagger.v3.core.converter;

import io.swagger.v3.oas.models.media.Schema;

import java.lang.reflect.Type;
import java.util.Iterator;
import java.util.Map;

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
     * @param type     the AnnotatedType
     * @param prevName the (optional) previous name
     */
    void defineModel(String name, Schema model, AnnotatedType type, String prevName);

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
    Schema resolve(AnnotatedType type);

    Map<String, Schema> getDefinedModels();

    /**
     * @return an Iterator of ModelConverters.  This iterator is not reused
     */
    public Iterator<ModelConverter> getConverters();
}

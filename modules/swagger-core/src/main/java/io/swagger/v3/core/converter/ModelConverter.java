package io.swagger.v3.core.converter;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.introspect.Annotated;
import io.swagger.v3.oas.models.media.Schema;

import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import java.util.Iterator;
import java.util.function.BiFunction;

public interface ModelConverter {

    /**
     * @param type
     * @param context
     * @param annotations to consider when resolving the property
     * @param chain       the chain of model converters to try if this implementation cannot process
     * @return null if this ModelConverter cannot convert the given Type
     */
    Schema resolve(Type type,
                          ModelConverterContext context,
                          Annotation[] annotations,
                          Iterator<ModelConverter> chain);

    /**
     * @param type
     * @param context
     * @param chain   the chain of model converters to try if this implementation cannot process
     * @return null if this ModelConverter cannot convert the given Type
     */
    Schema resolve(Type type, ModelConverterContext context, Iterator<ModelConverter> chain);

    /**
     * @param type
     * @param member
     * @param elementName
     * @param parent
     * @param jsonUnwrappedHandler
     * @param context
     * @param chain       the chain of model converters to try if this implementation cannot process
     * @return null if this ModelConverter cannot convert the given Type
     */
    Schema resolveAnnotatedType(
            Type type,
            Annotated member,
            String elementName,
            Schema parent,
            BiFunction<JavaType, Annotation[], Schema> jsonUnwrappedHandler,
            ModelConverterContext context,
            Iterator<ModelConverter> chain
);

}
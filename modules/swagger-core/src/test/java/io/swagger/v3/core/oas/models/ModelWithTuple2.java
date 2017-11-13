package io.swagger.v3.core.oas.models;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.v3.core.converter.ModelConverter;
import io.swagger.v3.core.converter.ModelConverterContext;
import io.swagger.v3.core.jackson.AbstractModelConverter;
import io.swagger.v3.oas.models.media.MapSchema;
import io.swagger.v3.oas.models.media.Schema;
import org.apache.commons.lang3.text.WordUtils;
import org.apache.commons.lang3.tuple.Pair;

import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import java.util.Iterator;
import java.util.Set;

public class ModelWithTuple2 {
    @io.swagger.v3.oas.annotations.media.Schema(description = "Possible values for state property of timesheet or timesheet entry", required = true)
    public Pair<String, String> timesheetStates;

    @io.swagger.v3.oas.annotations.media.Schema(description = "set of pairs", required = true)
    public Set<Pair<String, String>> manyPairs;

    @io.swagger.v3.oas.annotations.media.Schema(description = "set of pairs with complex left", required = true)
    public Set<Pair<ComplexLeft, String>> complexLeft;

    static class ComplexLeft {
        public String name;
        public Integer age;
    }

    public static class TupleAsMapModelConverter extends AbstractModelConverter {

        public TupleAsMapModelConverter(ObjectMapper mapper) {
            super(mapper);
        }

        @Override
        public Schema resolve(Type type, ModelConverterContext context, Annotation[] annotations, Iterator<ModelConverter> chain) {
            return this.resolve(type, context, chain);
        }

        @Override
        public Schema resolve(Type type, ModelConverterContext context, Iterator<ModelConverter> chain) {
            final JavaType javaType = _mapper.constructType(type);
            if (Pair.class.isAssignableFrom(javaType.getRawClass())) {
                final JavaType left = javaType.containedType(0);
                final String name = "MapOf" + WordUtils.capitalize(_typeName(left));

                return new MapSchema()
                        .name(name)
                        .additionalProperties(context.resolve(left, new Annotation[]{}));
            }
            return super.resolve(type, context, chain);
        }
    }

    public static class TupleAsMapPropertyConverter extends AbstractModelConverter {

        public TupleAsMapPropertyConverter(ObjectMapper mapper) {
            super(mapper);
        }

        @Override
        public Schema resolve(Type type, ModelConverterContext context, Annotation[] annotations,
                              Iterator<ModelConverter> chain) {
            final JavaType javaType = _mapper.constructType(type);
            if (Pair.class.isAssignableFrom(javaType.getRawClass())) {
                final JavaType left = javaType.containedType(0);
                return new MapSchema().additionalProperties(context.resolve(left, new Annotation[]{}));
            }
            if (chain.hasNext()) {
                return chain.next().resolve(type, context, annotations, chain);
            }
            return null;
        }

        @Override
        public Schema resolve(Type type, ModelConverterContext context, Iterator<ModelConverter> chain) {
            final JavaType javaType = _mapper.constructType(type);
            if (Pair.class.isAssignableFrom(javaType.getRawClass())) {
                return null;
            }
            return super.resolve(type, context, chain);
        }
    }
}

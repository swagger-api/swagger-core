package io.swagger.models;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.converter.ModelConverter;
import io.swagger.converter.ModelConverterContext;
import io.swagger.jackson.AbstractModelConverter;
import io.swagger.models.Model;
import io.swagger.models.ModelImpl;
import io.swagger.models.properties.MapProperty;
import io.swagger.models.properties.Property;
import org.apache.commons.lang3.text.WordUtils;
import org.apache.commons.lang3.tuple.Pair;

import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import java.util.Iterator;
import java.util.Set;

public class ModelWithTuple2 {
    @ApiModelProperty(value = "Possible values for state property of timesheet or timesheet entry", required = true)
    public Pair<String, String> timesheetStates;

    @ApiModelProperty(value = "set of pairs", required = true)
    public Set<Pair<String, String>> manyPairs;

    @ApiModelProperty(value = "set of pairs with complex left", required = true)
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
        public Model resolve(Type type, ModelConverterContext context, Iterator<ModelConverter> chain) {
            final JavaType javaType = _mapper.constructType(type);
            if (Pair.class.isAssignableFrom(javaType.getRawClass())) {
                final JavaType left = javaType.containedType(0);
                final String name = "MapOf" + WordUtils.capitalize(_typeName(left));

                return new ModelImpl().name(name).additionalProperties(context.resolveProperty(left, new Annotation[]{}));
            }
            return super.resolve(type, context, chain);
        }
    }

    public static class TupleAsMapPropertyConverter extends AbstractModelConverter {

        public TupleAsMapPropertyConverter(ObjectMapper mapper) {
            super(mapper);
        }

        @Override
        public Property resolveProperty(Type type, ModelConverterContext context, Annotation[] annotations,
                                        Iterator<ModelConverter> chain) {
            final JavaType javaType = _mapper.constructType(type);
            if (Pair.class.isAssignableFrom(javaType.getRawClass())) {
                final JavaType left = javaType.containedType(0);
                return new MapProperty(context.resolveProperty(left, new Annotation[]{}));
            }
            return super.resolveProperty(type, context, annotations, chain);
        }

        @Override
        public Model resolve(Type type, ModelConverterContext context, Iterator<ModelConverter> chain) {
            final JavaType javaType = _mapper.constructType(type);
            if (Pair.class.isAssignableFrom(javaType.getRawClass())) {
                return null;
            }
            return super.resolve(type, context, chain);
        }
    }
}

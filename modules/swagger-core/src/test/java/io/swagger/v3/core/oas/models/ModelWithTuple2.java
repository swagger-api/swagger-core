package io.swagger.v3.core.oas.models;

import com.fasterxml.jackson.databind.BeanDescription;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.v3.core.converter.AnnotatedType;
import io.swagger.v3.core.converter.ModelConverter;
import io.swagger.v3.core.converter.ModelConverterContext;
import io.swagger.v3.core.jackson.AbstractModelConverter;
import io.swagger.v3.oas.models.media.MapSchema;
import io.swagger.v3.oas.models.media.Schema;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.text.WordUtils;
import org.apache.commons.lang3.tuple.Pair;

import java.util.Iterator;
import java.util.Set;

import static io.swagger.v3.core.util.RefUtils.constructRef;

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
        public Schema resolve(AnnotatedType type, ModelConverterContext context, Iterator<ModelConverter> chain) {
            final JavaType javaType = _mapper.constructType(type.getType());
            if (Pair.class.isAssignableFrom(javaType.getRawClass())) {
                final JavaType left = javaType.containedType(0);
                final String name = "MapOf" + WordUtils.capitalize(_typeName(left));

                return new MapSchema()
                        .name(name)
                        .additionalProperties(context.resolve(new AnnotatedType(left)));
            }
            return super.resolve(type, context, chain);
        }
    }

    public static class TupleAsMapPropertyConverter extends AbstractModelConverter {

        public TupleAsMapPropertyConverter(ObjectMapper mapper) {
            super(mapper);
        }

        @Override
        public Schema resolve(AnnotatedType type, ModelConverterContext context,
                              Iterator<ModelConverter> chain) {
            final JavaType javaType = _mapper.constructType(type.getType());
            if (Pair.class.isAssignableFrom(javaType.getRawClass())) {
                if (!type.isSchemaProperty()) {
                    return null;
                }
                final JavaType left = javaType.containedType(0);
                Schema schema = context.resolve(new AnnotatedType().type(left).schemaProperty(type.isSchemaProperty()));
                String pName = null;
                if (left != null) {
                    BeanDescription valueTypeBeanDesc = _mapper.getSerializationConfig().introspect(left);
                    pName = _typeName(left, valueTypeBeanDesc);
                }

                if ("object".equals(schema.getType()) && pName != null) {
                    // create a reference for the items
                    if (context.getDefinedModels().containsKey(pName)) {
                        schema = new Schema().$ref(constructRef(pName));
                    }
                } else if (schema.get$ref() != null) {
                    schema = new Schema().$ref(StringUtils.isNotEmpty(schema.get$ref()) ? schema.get$ref() : schema.getName());
                }
                return new MapSchema().additionalProperties(schema);
            }
            if (chain.hasNext()) {
                return chain.next().resolve(type, context, chain);
            }
            return null;
        }
    }
}

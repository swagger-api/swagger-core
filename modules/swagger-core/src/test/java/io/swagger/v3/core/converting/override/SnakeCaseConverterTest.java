package io.swagger.v3.core.converting.override;

import com.google.common.collect.Sets;
import io.swagger.v3.core.converter.AnnotatedType;
import io.swagger.v3.core.converter.ModelConverter;
import io.swagger.v3.core.converter.ModelConverterContext;
import io.swagger.v3.core.converter.ModelConverters;
import io.swagger.v3.core.matchers.SerializationMatchers;
import io.swagger.v3.core.util.Json;
import io.swagger.v3.oas.models.media.Schema;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.Pair;
import org.testng.annotations.Test;

import javax.xml.bind.annotation.XmlRootElement;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;
import java.util.function.BiFunction;

import static io.swagger.v3.core.util.RefUtils.constructRef;
import static io.swagger.v3.core.util.RefUtils.extractSimpleName;

public class SnakeCaseConverterTest {

    @Test(description = "it should change naming style")
    public void testConvert() {
        // add the custom converter
        final SnakeCaseConverter snakeCaseConverter = new SnakeCaseConverter();
        final ModelConverters converters = new ModelConverters();

        converters.addConverter(snakeCaseConverter);

        final Map<String, Schema> models = converters.readAll(SnakeCaseModel.class);
        final String json = "{" +
                "   \"bar\":{" +
                "      \"type\":\"object\"," +
                "      \"properties\":{" +
                "         \"foo\":{" +
                "            \"type\":\"string\"" +
                "         }" +
                "      }" +
                "   }," +
                "   \"snake_case_model\":{" +
                "      \"type\":\"object\"," +
                "      \"properties\":{" +
                "         \"bar\":{" +
                "            \"$ref\":\"#/components/schemas/bar\"" +
                "         }," +
                "         \"title\":{" +
                "            \"type\":\"string\"" +
                "         }" +
                "      }," +
                "      \"xml\":{" +
                "        \"name\":\"snakeCaseModel\"" +
                "      }" +
                "   }" +
                "}";

        // TODO test xml if/when added to annotations and resolver
        Json.prettyPrint(models);
        SerializationMatchers.assertEqualsToJson(models, json);
    }

    /**
     * simple converter to rename models and field names into snake_case
     */
    class SnakeCaseConverter implements ModelConverter {
        final Set<String> primitives = Sets.newHashSet("string", "integer", "number", "boolean", "long");


        @Override
        public Schema resolve(AnnotatedType type, ModelConverterContext context, Iterator<ModelConverter> chain) {
            if (chain.hasNext()) {
                final ModelConverter converter = chain.next();
                final Schema model = converter.resolve(type, context, chain);
                if (model != null) {
                    final Map<String, Schema> properties = model.getProperties();
                    final Map<String, Schema> updatedProperties = new LinkedHashMap<String, Schema>();
                    if (properties != null) {
                        for (String key : properties.keySet()) {
                            String convertedKey = toSnakeCase(key);
                            Schema prop = properties.get(key);
                            if (prop.get$ref() != null) {
                                Pair<String, String> refName = extractSimpleName(prop.get$ref());
                                if (!StringUtils.isBlank(refName.getRight())) { // skip if didn't resolve simple name
                                    prop.set$ref(constructRef(toSnakeCase(refName.getLeft()), refName.getRight()));
                                }
                            }
                            updatedProperties.put(convertedKey, prop);
                        }
                        model.getProperties().clear();
                        model.setProperties(updatedProperties);
                    }
                    if (model.getName() != null) {
                        String prevName = model.getName();
                        model.setName(toSnakeCase(model.getName()));
                        context.defineModel(model.getName(), model, type, prevName);
                    }

                    return model;
                }
            }
            return null;
        }

        private String toSnakeCase(String str) {
            if (StringUtils.isBlank(str)) {
                return str;
            }
            String o = str.replaceAll("[A-Z\\d]", "_" + "$0").toLowerCase();
            if (o.startsWith("_")) {
                return o.substring(1);
            } else {
                return o;
            }
        }
    }

    @XmlRootElement(name = "snakeCaseModel")
    class SnakeCaseModel {
        public Bar bar = null;
        public String title = null;
    }

    class Bar {
        public String foo = null;
    }
}

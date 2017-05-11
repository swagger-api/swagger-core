package io.swagger.model.override;

import com.google.common.collect.Sets;
import io.swagger.converter.ModelConverter;
import io.swagger.converter.ModelConverterContext;
import io.swagger.converter.ModelConverters;
import io.swagger.matchers.SerializationMatchers;
import io.swagger.oas.models.media.Schema;
import io.swagger.util.Json;
import org.testng.annotations.Test;

import javax.xml.bind.annotation.XmlRootElement;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

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
                "      \"title\" : \"bar\",\n" +
                "      \"type\":\"object\"," +
                "      \"properties\":{" +
                "         \"foo\":{" +
                "            \"title\" : \"foo\"," +
                "            \"type\":\"string\"" +
                "         }" +
                "      }" +
                "   }," +
                "   \"snake_case_model\":{" +
                "      \"type\":\"object\"," +
                "      \"properties\":{" +
                "         \"bar\":{" +
                "            \"$ref\":\"#/definitions/bar\"" +
                "         }," +
                "         \"title\":{" +
                "            \"type\":\"string\"" +
                "         }" +
                "      }," +
                "      \"xml\":{" +
                "         \"name\":\"snakeCaseModel\"" +
                "      }" +
                "   }" +
                "}";
        Json.prettyPrint(models);
        SerializationMatchers.assertEqualsToJson(models.get("snake_case_model"), json);
    }

    @XmlRootElement(name = "snakeCaseModel")
    class SnakeCaseModel {
        public Bar bar = null;
        public String title = null;
    }

    /**
     * simple converter to rename models and field names into snake_case
     */
    class SnakeCaseConverter implements ModelConverter {
        final Set<String> primitives = Sets.newHashSet("string", "integer", "number", "boolean", "long");

        @Override
        public Schema resolve(Type type, ModelConverterContext context, Annotation[] annotations, Iterator<ModelConverter> chain) {
            if (chain.hasNext()) {
                final ModelConverter converter = chain.next();
                return converter.resolve(type, context, annotations, chain);
            }
            return null;
        }

        @Override
        public Schema resolve(Type type, ModelConverterContext context, Iterator<ModelConverter> chain) {
            if (chain.hasNext()) {
                final ModelConverter converter = chain.next();
                final Schema model = converter.resolve(type, context, chain);
                if (model != null) {
                    final Map<String, Schema> properties = model.getProperties();
                    final Map<String, Schema> updatedProperties = new LinkedHashMap<String, Schema>();
                    for (String key : properties.keySet()) {
                        String convertedKey = toSnakeCase(key);
                        Schema prop = properties.get(key);
                        if (prop.get$ref() != null) {
                            // TODO
//                            prop.set$ref(toSnakeCase(ref.getSimpleRef()));
                        }
                        updatedProperties.put(convertedKey, prop);
                    }
                    model.getProperties().clear();
                    model.setProperties(updatedProperties);
                    if (model instanceof Schema) {
                        Schema impl = (Schema) model;
                        String prevName = impl.getTitle();
                        impl.setTitle(toSnakeCase(impl.getTitle()));
                        context.defineModel(impl.getTitle(), impl, type, prevName);
                    }
                    return model;
                }
            }
            return null;
        }

        private String toSnakeCase(String str) {
            String o = str.replaceAll("[A-Z\\d]", "_"+ "$0").toLowerCase();
            if (o.startsWith("_")) {
                return o.substring(1);
            } else {
                return o;
            }
        }
    }

    class Bar {
        public String foo = null;
    }
}

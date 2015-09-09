package io.swagger.model.override;

import io.swagger.converter.ModelConverter;
import io.swagger.converter.ModelConverterContext;
import io.swagger.converter.ModelConverters;
import io.swagger.matchers.SerializationMatchers;
import io.swagger.models.Model;
import io.swagger.models.ModelImpl;
import io.swagger.models.properties.Property;
import io.swagger.models.properties.RefProperty;

import com.google.common.collect.Sets;

import org.testng.annotations.Test;

import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

import javax.xml.bind.annotation.XmlRootElement;

public class SnakeCaseConverterTest {

    @Test(description = "it should change naming style")
    public void testConvert() {
        // add the custom converter
        final SnakeCaseConverter snakeCaseConverter = new SnakeCaseConverter();
        final ModelConverters converters = new ModelConverters();

        converters.addConverter(snakeCaseConverter);

        final Map<String, Model> models = converters.readAll(SnakeCaseModel.class);
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
        SerializationMatchers.assertEqualsToJson(models, json);
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
        public Property resolveProperty(Type type, ModelConverterContext context, Annotation[] annotations, Iterator<ModelConverter> chain) {
            if (chain.hasNext()) {
                final ModelConverter converter = chain.next();
                return converter.resolveProperty(type, context, annotations, chain);
            }
            return null;
        }

        @Override
        public Model resolve(Type type, ModelConverterContext context, Iterator<ModelConverter> chain) {
            if (chain.hasNext()) {
                final ModelConverter converter = chain.next();
                final Model model = converter.resolve(type, context, chain);
                if (model != null) {
                    final Map<String, Property> properties = model.getProperties();
                    final Map<String, Property> updatedProperties = new LinkedHashMap<String, Property>();
                    for (String key : properties.keySet()) {
                        String convertedKey = toSnakeCase(key);
                        Property prop = properties.get(key);
                        if (prop instanceof RefProperty) {
                            RefProperty ref = (RefProperty) prop;
                            ref.set$ref(toSnakeCase(ref.getSimpleRef()));
                        }
                        updatedProperties.put(convertedKey, prop);
                    }
                    model.getProperties().clear();
                    model.setProperties(updatedProperties);
                    if (model instanceof ModelImpl) {
                        ModelImpl impl = (ModelImpl) model;
                        String prevName = impl.getName();
                        impl.setName(toSnakeCase(impl.getName()));
                        context.defineModel(impl.getName(), impl, type, prevName);
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

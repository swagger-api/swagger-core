package io.swagger.model.override;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.annotations.ApiModel;
import io.swagger.converter.ModelConverters;
import io.swagger.jackson.ModelResolver;
import io.swagger.jackson.TypeNameResolver;
import io.swagger.models.Model;
import io.swagger.models.properties.Property;
import io.swagger.models.properties.RefProperty;
import io.swagger.util.Json;
import org.apache.commons.lang3.StringUtils;
import org.testng.annotations.Test;

import java.util.Map;
import java.util.Set;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;

public class CustomResolverTest {

    @Test(description = "it should ignore properties with type Bar")
    public void testCustomConverter() {
        // add the custom converter
        final ModelConverters converters = new ModelConverters();
        converters.addConverter(new CustomConverter(Json.mapper()));

        Map<String, Model> models = converters.readAll(Foo.class);
        Model model = models.get("io.swagger.model.override.CustomResolverTest$Foo");
        assertNotNull(model);
        assertEquals(model.getProperties().size(), 2);

        final RefProperty barProperty = (RefProperty) model.getProperties().get("bar");
        assertEquals(barProperty.get$ref(), "#/definitions/io.swagger.model.override.CustomResolverTest$Bar");

        final Property titleProperty = model.getProperties().get("title");
        assertNotNull(titleProperty);

        model = models.get("io.swagger.model.override.CustomResolverTest$Bar");
        assertNotNull(model);

    }

    class CustomConverter extends ModelResolver {

        public CustomConverter(ObjectMapper mapper) {
            super(mapper, new QualifiedTypeNameResolver());
        }
    }

    class QualifiedTypeNameResolver extends TypeNameResolver {

        @Override
        protected String nameForClass(Class<?> cls, Set<Options> options) {
            String className = cls.getName().startsWith("java.") ? cls.getSimpleName() : cls.getName();
            if (options.contains(Options.SKIP_API_MODEL)) {
                return className;
            }
            final ApiModel model = cls.getAnnotation(ApiModel.class);
            final String modelName = model == null ? null : StringUtils.trimToNull(model.value());
            return modelName == null ? className : modelName;
        }
    }

    class Foo {
        public Bar bar = null;
        public String title = null;
    }

    class Bar {
        public String foo = null;
    }
}

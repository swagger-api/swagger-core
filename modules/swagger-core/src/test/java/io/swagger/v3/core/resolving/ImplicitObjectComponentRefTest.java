package io.swagger.v3.core.resolving;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.core.converter.AnnotatedType;
import io.swagger.v3.core.converter.ModelConverterContextImpl;
import io.swagger.v3.core.jackson.ModelResolver;
import io.swagger.v3.core.util.Json;
import io.swagger.v3.core.util.Json31;
import io.swagger.v3.oas.annotations.media.Schema;
import tools.jackson.databind.JsonNode;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertTrue;

/**
 * A type without properties swagger-core can infer (here its content is only exposed through
 * {@code @JsonAnyGetter}) is defined as a component but has no {@code type} in OpenAPI 3.1. Where it is
 * used as a map value, an array item or a property, it must be referenced via {@code $ref} instead of
 * being inlined, as it already is in OpenAPI 3.0.
 */
public class ImplicitObjectComponentRefTest {

    private static final String TRANSLATION_REF = "#/components/schemas/Translation";

    @DataProvider
    public Object[][] specVersions() {
        return new Object[][]{{false}, {true}};
    }

    @Test(dataProvider = "specVersions")
    public void implicitObjectComponentIsReferenced(boolean openapi31) {
        final ModelResolver modelResolver = new ModelResolver(openapi31 ? Json31.mapper() : Json.mapper())
                .openapi31(openapi31);
        final ModelConverterContextImpl context = new ModelConverterContextImpl(modelResolver);

        context.resolve(new AnnotatedType(Translations.class));

        final Map<String, io.swagger.v3.oas.models.media.Schema> models = context.getDefinedModels();
        final io.swagger.v3.oas.models.media.Schema<?> translation = models.get("Translation");
        assertNotNull(translation, "Translation should be defined as a component");
        assertEquals(translation.getDescription(), "Translations in a specific language");

        final io.swagger.v3.oas.models.media.Schema<?> translations = models.get("Translations");
        assertNotNull(translations, "Translations should be defined as a component");
        final Map<String, io.swagger.v3.oas.models.media.Schema> properties = translations.getProperties();

        // map value
        final io.swagger.v3.oas.models.media.Schema<?> lang = properties.get("lang");
        assertNotNull(lang, "lang property should be resolved");
        assertEquals(lang.getDescription(), "The keys are language codes");
        assertTrue(lang.getAdditionalProperties() instanceof io.swagger.v3.oas.models.media.Schema,
                "lang should be a map schema");
        assertEquals(((io.swagger.v3.oas.models.media.Schema<?>) lang.getAdditionalProperties()).get$ref(),
                TRANSLATION_REF, "map values should reference the Translation component");

        // array items
        final io.swagger.v3.oas.models.media.Schema<?> list = properties.get("list");
        assertNotNull(list, "list property should be resolved");
        assertNotNull(list.getItems(), "list should be an array schema");
        assertEquals(list.getItems().get$ref(), TRANSLATION_REF,
                "array items should reference the Translation component");

        // property
        final io.swagger.v3.oas.models.media.Schema<?> single = properties.get("single");
        assertNotNull(single, "single property should be resolved");
        assertEquals(single.get$ref(), TRANSLATION_REF,
                "the property should reference the Translation component");
    }


    @Test(dataProvider = "specVersions")
    public void emptyBeanAndJsonNodeAreReferenced(boolean openapi31) {
        final ModelResolver modelResolver = new ModelResolver(openapi31 ? Json31.mapper() : Json.mapper())
                .openapi31(openapi31);
        final ModelConverterContextImpl context = new ModelConverterContextImpl(modelResolver);

        context.resolve(new AnnotatedType(Untyped.class));

        final Map<String, io.swagger.v3.oas.models.media.Schema> models = context.getDefinedModels();
        assertNotNull(models.get("EmptyBean"), "EmptyBean should be defined as a component");
        assertNotNull(models.get("JsonNode"), "JsonNode should be defined as a component");

        final Map<String, io.swagger.v3.oas.models.media.Schema> properties = models.get("Untyped").getProperties();
        assertEquals(properties.get("empty").get$ref(), "#/components/schemas/EmptyBean");
        assertEquals(properties.get("node").get$ref(), "#/components/schemas/JsonNode");
        assertEquals(properties.get("emptyList").getItems().get$ref(), "#/components/schemas/EmptyBean");
        assertEquals(((io.swagger.v3.oas.models.media.Schema<?>) properties.get("emptyMap").getAdditionalProperties())
                .get$ref(), "#/components/schemas/EmptyBean");
    }


    @Test
    public void annotatedPropertyKeepsReference31() {
        final ModelResolver modelResolver = new ModelResolver(Json31.mapper()).openapi31(true);
        final ModelConverterContextImpl context = new ModelConverterContextImpl(modelResolver);

        context.resolve(new AnnotatedType(Annotated.class));

        final io.swagger.v3.oas.models.media.Schema<?> described =
                (io.swagger.v3.oas.models.media.Schema<?>) context.getDefinedModels().get("Annotated")
                        .getProperties().get("described");
        assertEquals(described.get$ref(), TRANSLATION_REF);
        assertEquals(described.getDescription(), "Property description");
    }

    public static final class EmptyBean {
    }

    public static final class Untyped {
        public EmptyBean empty;

        public JsonNode node;

        public List<EmptyBean> emptyList;

        public Map<String, EmptyBean> emptyMap;
    }

    public static final class Annotated {
        @Schema(description = "Property description")
        public Translation described;
    }

    @Schema(description = "Translations in a specific language")
    public static final class Translation {
        private final Map<String, String> translations;

        public Translation(Map<String, String> translations) {
            this.translations = translations;
        }

        @JsonAnyGetter
        Map<String, String> getTranslations() {
            return translations;
        }
    }

    public static final class Translations {
        private final Map<Locale, Translation> byLanguage = new HashMap<>();

        public List<Translation> list;

        public Translation single;

        @JsonProperty("lang")
        @Schema(description = "The keys are language codes")
        public Map<Locale, Translation> getByLanguage() {
            return byLanguage;
        }
    }
}

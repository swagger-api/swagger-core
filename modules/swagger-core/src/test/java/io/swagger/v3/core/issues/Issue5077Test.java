package io.swagger.v3.core.issues;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.v3.core.converter.ModelConverters;
import io.swagger.v3.core.converter.ResolvedSchema;
import io.swagger.v3.core.util.Json;
import io.swagger.v3.core.util.Json31;
import io.swagger.v3.core.util.ResourceUtils;
import org.testng.annotations.Test;

import javax.annotation.Nullable;
import java.io.IOException;
import java.util.List;

import static org.testng.Assert.assertEquals;

/**
 * Reproduces GitHub Issue #5077
 * Issue using Nullable annotation for OAS 3.1
 *
 * <p>Tests that @Nullable annotation does not affect the item in a list:
 *
 * <p>Note: This test uses javax.annotation.Nullable which is automatically transformed to
 * jakarta.annotation.Nullable in the swagger-core-jakarta module via the Eclipse Transformer.
 *
 */
public class Issue5077Test {

    @Test
    public void testOAS31() throws IOException {
        ResolvedSchema schema = ModelConverters.getInstance(true)
                .readAllAsResolvedSchema(ModelWithObjectAndList.class);

        String expectedJson = ResourceUtils.loadClassResource(getClass(), "specFiles/NullableFieldsOAS31.json");
        String actualJson = Json31.pretty(schema);
        ObjectMapper mapper = new ObjectMapper();
        JsonNode expectedNode = mapper.readTree(expectedJson);
        JsonNode actualNode = mapper.readTree(actualJson);
        assertEquals(actualNode, expectedNode);
    }

    @Test
    public void testOAS30() throws IOException {
        ResolvedSchema schema = ModelConverters.getInstance()
                .readAllAsResolvedSchema(ModelWithObjectAndList.class);

        String expectedJson = ResourceUtils.loadClassResource(getClass(), "specFiles/NullableFieldsOAS30.json");
        String actualJson = Json.pretty(schema);
        ObjectMapper mapper = new ObjectMapper();
        JsonNode expectedNode = mapper.readTree(expectedJson);
        JsonNode actualNode = mapper.readTree(actualJson);
        assertEquals(actualNode, expectedNode);
    }

    public static class ModelWithObjectAndList {

        @Nullable
        private String nullableString;

        private String requiredString;

        @Nullable
        private Model nullableModel;

        @Nullable
        private List<String> strings;

        @Nullable
        private List<Model> models;

        public String getNullableString() {
            return nullableString;
        }

        public void setNullableString(String nullableString) {
            this.nullableString = nullableString;
        }

        public String getRequiredString() {
            return requiredString;
        }

        public void setRequiredString(String requiredString) {
            this.requiredString = requiredString;
        }

        public Model getNullableModel() {
            return nullableModel;
        }

        public void setNullableModel(Model model) {
            this.nullableModel = model;
        }

        public List<Model> getModels() {
            return models;
        }

        public void setModels(List<Model> models) {
            this.models = models;
        }

        @Nullable
        public List<String> getStrings() {
            return strings;
        }

        public void setStrings(@Nullable List<String> strings) {
            this.strings = strings;
        }
    }

    public static class Model {
        private String field;

        public String getField() {
            return field;
        }

        public void setField(String field) {
            this.field = field;
        }
    }

}

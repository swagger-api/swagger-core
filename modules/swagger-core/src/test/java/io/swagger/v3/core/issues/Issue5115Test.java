package io.swagger.v3.core.issues;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.v3.core.converter.ModelConverters;
import io.swagger.v3.core.converter.ResolvedSchema;
import io.swagger.v3.core.util.Json;
import io.swagger.v3.core.util.Json31;
import io.swagger.v3.core.util.ResourceUtils;
import io.swagger.v3.oas.annotations.media.Schema;
import org.testng.annotations.Test;

import javax.annotation.Nullable;
import java.io.IOException;

import static org.testng.Assert.assertEquals;

/**
 * Reproduces GitHub Issue #5115
 * Issue using Nullable annotation on Model-fields
 *
 * <p>Tests that @Nullable annotation does not affect an object in an invalid manner. Any nullability that is configured
 * with a schema annotation (nullable = true for OAS30 and null in types for OAS31) is retained.
 *
 * <p>Note: This test uses javax.annotation.Nullable which is automatically transformed to
 * jakarta.annotation.Nullable in the swagger-core-jakarta module via the Eclipse Transformer.
 */
public class Issue5115Test {

    @Test
    public void testObjectDoesNotGetInvalidNullableSchemaOAS31() throws IOException {
        ResolvedSchema schema = ModelConverters.getInstance(true)
                .readAllAsResolvedSchema(ModelWithObject.class);

        String expectedJson = ResourceUtils.loadClassResource(getClass(), "specFiles/NullableObjectFieldsOAS31.json");
        String actualJson = Json31.pretty(schema);
        ObjectMapper mapper = new ObjectMapper();
        JsonNode expectedNode = mapper.readTree(expectedJson);
        JsonNode actualNode = mapper.readTree(actualJson);
        assertEquals(actualNode, expectedNode);
    }

    @Test
    public void testObjectDoesNotGetInvalidNullableSchemaOAS30() throws IOException {
        ResolvedSchema schema = ModelConverters.getInstance()
                .readAllAsResolvedSchema(ModelWithObject.class);

        String expectedJson = ResourceUtils.loadClassResource(getClass(), "specFiles/NullableObjectFieldsOAS30.json");
        String actualJson = Json.pretty(schema);
        ObjectMapper mapper = new ObjectMapper();
        JsonNode expectedNode = mapper.readTree(expectedJson);
        JsonNode actualNode = mapper.readTree(actualJson);
        assertEquals(actualNode, expectedNode);
    }

    @Test
    public void testObjectKeepsInvalidNullableSchemaIfSetInSchemaAnnotationOAS31() throws IOException {
        ResolvedSchema schema = ModelConverters.getInstance(true)
                .readAllAsResolvedSchema(ModelWithObjectThatHasNullableInSchemaAnnotationOAS31.class);

        String expectedJson = ResourceUtils.loadClassResource(getClass(), "specFiles/NullableObjectFieldsSchemaAnnotationOAS31.json");
        String actualJson = Json31.pretty(schema);
        ObjectMapper mapper = new ObjectMapper();
        JsonNode expectedNode = mapper.readTree(expectedJson);
        JsonNode actualNode = mapper.readTree(actualJson);
        assertEquals(actualNode, expectedNode);
    }

    @Test
    public void testObjectKeepsInvalidNullableSchemaIfSetInSchemaAnnotationOAS30() throws IOException {
        ResolvedSchema schema = ModelConverters.getInstance()
                .readAllAsResolvedSchema(ModelWithObjectThatHasNullableInSchemaAnnotationOAS30.class);

        String expectedJson = ResourceUtils.loadClassResource(getClass(), "specFiles/NullableObjectFieldsSchemaAnnotationOAS30.json");
        String actualJson = Json.pretty(schema);
        ObjectMapper mapper = new ObjectMapper();
        JsonNode expectedNode = mapper.readTree(expectedJson);
        JsonNode actualNode = mapper.readTree(actualJson);
        assertEquals(actualNode, expectedNode);
    }

    public static class ModelWithObject {

        @Nullable
        private Model nullableModel;

        private Model model;

        @Nullable
        public Model getNullableModel() {
            return nullableModel;
        }

        public void setNullableModel(@Nullable Model model) {
            this.nullableModel = model;
        }

        public Model getModel() {
            return model;
        }

        public void setModel(Model model) {
            this.model = model;
        }
    }

    public static class ModelWithObjectThatHasNullableInSchemaAnnotationOAS30 {

        // This nullable is lost since the fields are read top to bottom and model does not define nullable
        @Schema(nullable = true)
        private NestedModel nullableModel;

        private NestedModel model;

        @Schema(nullable = true)
        private NestedModel2 nullableModel2;

        public NestedModel getNullableModel() {
            return nullableModel;
        }

        public void setNullableModel(NestedModel model) {
            this.nullableModel = model;
        }

        public NestedModel getModel() {
            return model;
        }

        public void setModel(NestedModel model) {
            this.model = model;
        }

        public NestedModel2 getNullableModel2() {
            return nullableModel2;
        }

        public void setNullableModel2(NestedModel2 nullableModel2) {
            this.nullableModel2 = nullableModel2;
        }
    }

    public static class ModelWithObjectThatHasNullableInSchemaAnnotationOAS31 {

        @Schema(types = {"object", "null"})
        private Model nullableModel;

        private Model model;

        public Model getNullableModel() {
            return nullableModel;
        }

        public void setNullableModel(Model model) {
            this.nullableModel = model;
        }

        public Model getModel() {
            return model;
        }

        public void setModel(Model model) {
            this.model = model;
        }
    }

    public static class Model {

        private NestedModel nestedModel;

        @Nullable
        private NestedModel nullableNestedModel;

        @Nullable
        private NestedModel2 nullableNestedModel2;

        private NestedModel2 nestedModel2;

        public NestedModel getNestedModel() {
            return nestedModel;
        }

        public void setNestedModel(NestedModel nestedModel) {
            this.nestedModel = nestedModel;
        }

        @Nullable
        public NestedModel getNullableNestedModel() {
            return nullableNestedModel;
        }

        public void setNullableNestedModel(@Nullable NestedModel nullableNestedModel) {
            this.nullableNestedModel = nullableNestedModel;
        }

        @Nullable
        public NestedModel2 getNullableNestedModel2() {
            return nullableNestedModel2;
        }

        public void setNullableNestedModel2(@Nullable NestedModel2 nullableNestedModel2) {
            this.nullableNestedModel2 = nullableNestedModel2;
        }

        public NestedModel2 getNestedModel2() {
            return nestedModel2;
        }

        public void setNestedModel2(NestedModel2 nestedModel2) {
            this.nestedModel2 = nestedModel2;
        }
    }

    public static class NestedModel {
        private String field;

        public String getField() {
            return field;
        }

        public void setField(String field) {
            this.field = field;
        }
    }

    public static class NestedModel2 {
        private String field2;

        public String getField2() {
            return field2;
        }

        public void setField2(String field2) {
            this.field2 = field2;
        }
    }

}

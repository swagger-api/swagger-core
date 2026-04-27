package io.swagger.v3.core.issues;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.v3.core.converter.ModelConverters;
import io.swagger.v3.core.converter.ResolvedSchema;
import io.swagger.v3.core.util.Json;
import io.swagger.v3.core.util.Json31;
import io.swagger.v3.core.util.ResourceUtils;
import io.swagger.v3.oas.models.media.ArraySchema;
import io.swagger.v3.oas.models.media.Schema;
import org.testng.annotations.Test;

import javax.annotation.Nullable;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Stream;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertNull;
import static org.testng.Assert.assertTrue;

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

    @Test
    public void nullableSetOas31_onlyContainerIsNullable() {
        ResolvedSchema resolved = ModelConverters.getInstance(true).readAllAsResolvedSchema(NullableSetContainer.class);
        System.out.println("nullableSetOas31_onlyContainerIsNullable:\n" + Json31.pretty(resolved));
        Map<String, Schema> schemas = resolved.referencedSchemas;
        Schema<?> container = schemas.get("NullableSetContainer");
        Schema<?> tags = (Schema<?>) container.getProperties().get("tags");

        assertNotNull(tags);
        assertTrue(tags.getTypes().contains("array"), "container should be array type");
        assertTrue(tags.getTypes().contains("null"), "container should be nullable");
        assertEquals(tags.getTypes().size(), 2);

        Schema<?> items = tags.getItems();
        assertNotNull(items);
        assertTrue(items.getTypes().contains("string"), "items should be string type");
        assertFalse(items.getTypes().contains("null"), "nullable must not leak into items");
    }

    @Test
    public void nullableArrayOas31_onlyContainerIsNullable() {
        ResolvedSchema resolved = ModelConverters.getInstance(true).readAllAsResolvedSchema(NullableArrayContainer.class);
        System.out.println("nullableArrayOas31_onlyContainerIsNullable:\n" + Json31.pretty(resolved));
        Map<String, Schema> schemas = resolved.referencedSchemas;
        Schema<?> container = schemas.get("NullableArrayContainer");
        Schema<?> names = (Schema<?>) container.getProperties().get("names");

        assertNotNull(names);
        assertTrue(names.getTypes().contains("array"), "container should be array type");
        assertTrue(names.getTypes().contains("null"), "container should be nullable");
        assertEquals(names.getTypes().size(), 2);

        Schema<?> items = names.getItems();
        assertNotNull(items);
        assertTrue(items.getTypes().contains("string"), "items should be string type");
        assertFalse(items.getTypes().contains("null"), "nullable must not leak into items");
    }

    @Test
    public void nullableMapOas31_onlyContainerIsNullable() {
        ResolvedSchema resolved = ModelConverters.getInstance(true).readAllAsResolvedSchema(NullableMapContainer.class);
        System.out.println("nullableMapOas31_onlyContainerIsNullable:\n" + Json31.pretty(resolved));
        Map<String, Schema> schemas = resolved.referencedSchemas;
        Schema<?> container = schemas.get("NullableMapContainer");
        Schema<?> labels = (Schema<?>) container.getProperties().get("labels");

        assertNotNull(labels);
        assertTrue(labels.getTypes().contains("object"), "container should be object type");
        assertTrue(labels.getTypes().contains("null"), "container should be nullable");
        assertEquals(labels.getTypes().size(), 2);

        Schema<?> additionalProperties = (Schema<?>) labels.getAdditionalProperties();
        assertNotNull(additionalProperties);
        assertTrue(additionalProperties.getTypes().contains("string"), "value schema should be string type");
        assertFalse(additionalProperties.getTypes().contains("null"), "nullable must not leak into value schema");
    }

    @Test
    public void nullableNestedListOas31_onlyOuterContainerIsNullable() {
        ResolvedSchema resolved = ModelConverters.getInstance(true).readAllAsResolvedSchema(NullableNestedListContainer.class);
        System.out.println("nullableNestedListOas31_onlyOuterContainerIsNullable:\n" + Json31.pretty(resolved));
        Map<String, Schema> schemas = resolved.referencedSchemas;
        Schema<?> container = schemas.get("NullableNestedListContainer");
        Schema<?> matrix = (Schema<?>) container.getProperties().get("matrix");

        assertNotNull(matrix);
        assertTrue(matrix.getTypes().contains("array"), "outer container should be array type");
        assertTrue(matrix.getTypes().contains("null"), "outer container should be nullable");
        assertEquals(matrix.getTypes().size(), 2);

        Schema<?> inner = matrix.getItems();
        assertNotNull(inner);
        assertTrue(inner.getTypes().contains("array"), "inner container should be array type");
        assertFalse(inner.getTypes().contains("null"), "nullable must not leak into inner container");
        assertEquals(inner.getTypes().size(), 1);

        Schema<?> item = inner.getItems();
        assertNotNull(item);
        assertTrue(item.getTypes().contains("string"), "leaf items should be string type");
        assertFalse(item.getTypes().contains("null"), "nullable must not leak into leaf items");
    }

    @Test
    public void explicitItemNullabilityOas31_stillWorks() {
        ResolvedSchema resolved = ModelConverters.getInstance(true).readAllAsResolvedSchema(ExplicitNullableItemsContainer.class);
        System.out.println("explicitItemNullabilityOas31_stillWorks:\n" + Json31.pretty(resolved));
        Map<String, Schema> schemas = resolved.referencedSchemas;
        Schema<?> container = schemas.get("ExplicitNullableItemsContainer");
        Schema<?> values = (Schema<?>) container.getProperties().get("values");

        assertNotNull(values);
        assertTrue(values.getTypes().contains("array"), "container should be array type");
        assertFalse(values.getTypes().contains("null"), "non-nullable container must not have null type");
        assertEquals(values.getTypes().size(), 1);

        Schema<?> items = values.getItems();
        assertNotNull(items);
        assertTrue(items.getTypes().contains("string"), "items should be string type");
        assertTrue(items.getTypes().contains("null"), "items are explicitly nullable");
        assertEquals(items.getTypes().size(), 2);
    }

    @Test
    public void nullableArraySchemaWithImplementationOas31_onlyContainerIsNullable() {
        ResolvedSchema resolved = ModelConverters.getInstance(true).readAllAsResolvedSchema(NullableArraySchemaWithImplementationContainer.class);
        Map<String, Schema> schemas = resolved.referencedSchemas;
        Schema<?> container = schemas.get("NullableArraySchemaWithImplementationContainer");
        Schema<?> tags = (Schema<?>) container.getProperties().get("tags");

        assertNotNull(tags);
        assertTrue(tags.getTypes().contains("array"), "container should be array type");
        assertTrue(tags.getTypes().contains("null"), "container should be nullable");
        assertEquals(tags.getTypes().size(), 2);

        Schema<?> items = tags.getItems();
        assertNotNull(items);
        assertTrue(items.getTypes().contains("string"), "items should be string type");
        assertFalse(items.getTypes().contains("null"), "nullable must not leak into items");
    }

    @Test
    public void nullableSetOas30_onlyContainerIsNullable() {
        ResolvedSchema resolved = ModelConverters.getInstance(false).readAllAsResolvedSchema(NullableSetContainer.class);
        System.out.println("nullableSetOas30_onlyContainerIsNullable:\n" + Json.pretty(resolved));
        Map<String, Schema> schemas = resolved.referencedSchemas;
        Schema<?> container = schemas.get("NullableSetContainer");
        ArraySchema tags = (ArraySchema) container.getProperties().get("tags");

        assertNotNull(tags);
        assertEquals(tags.getType(), "array");
        assertTrue(Boolean.TRUE.equals(tags.getNullable()));

        Schema<?> items = tags.getItems();
        assertNotNull(items);
        assertEquals(items.getType(), "string");
        assertNull(items.getNullable(), "nullable must not leak into items");
    }

    @Test
    public void nullableMapOas30_onlyContainerIsNullable() {
        ResolvedSchema resolved = ModelConverters.getInstance(false).readAllAsResolvedSchema(NullableMapContainer.class);
        System.out.println("nullableMapOas30_onlyContainerIsNullable:\n" + Json.pretty(resolved));
        Map<String, Schema> schemas = resolved.referencedSchemas;
        Schema<?> container = schemas.get("NullableMapContainer");
        Schema<?> labels = (Schema<?>) container.getProperties().get("labels");

        assertNotNull(labels);
        assertEquals(labels.getType(), "object");
        assertTrue(Boolean.TRUE.equals(labels.getNullable()));

        Schema<?> additionalProperties = (Schema<?>) labels.getAdditionalProperties();
        assertNotNull(additionalProperties);
        assertEquals(additionalProperties.getType(), "string");
        assertNull(additionalProperties.getNullable(), "nullable must not leak into value schema");
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

    public static class NullableSetContainer {
        @Nullable
        public Set<String> tags;
    }

    public static class NullableArrayContainer {
        @Nullable
        public String[] names;
    }

    public static class NullableMapContainer {
        @Nullable
        public Map<String, String> labels;
    }

    public static class NullableNestedListContainer {
        @Nullable
        public List<List<String>> matrix;
    }

    public static class ExplicitNullableItemsContainer {
        @io.swagger.v3.oas.annotations.media.ArraySchema(schema = @io.swagger.v3.oas.annotations.media.Schema(types = { "string", "null" }))
        public List<String> values;
    }

    public static class NullableStreamContainer {
        @Nullable
        @io.swagger.v3.oas.annotations.media.ArraySchema(schema = @io.swagger.v3.oas.annotations.media.Schema(implementation = String.class))
        public Stream<String> tags;
    }

    public static class NullableArraySchemaWithImplementationContainer {
        @Nullable
        @io.swagger.v3.oas.annotations.media.ArraySchema(schema = @io.swagger.v3.oas.annotations.media.Schema(implementation = String.class))
        public List<Object> tags;
    }

    public static class NullableArraySchemaWithRefContainer {
        @Nullable
        @io.swagger.v3.oas.annotations.media.ArraySchema(schema = @io.swagger.v3.oas.annotations.media.Schema(ref = "#/components/schemas/Model"))
        public Object models;
    }

    public static class NullableRefStreamContainer {
        @Nullable
        @io.swagger.v3.oas.annotations.media.ArraySchema(schema = @io.swagger.v3.oas.annotations.media.Schema(implementation = Model.class))
        public Stream<Model> models;
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

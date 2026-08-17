package io.swagger.v3.core.resolving;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import io.swagger.v3.core.converter.AnnotatedType;
import io.swagger.v3.core.converter.ModelConverterContextImpl;
import io.swagger.v3.core.jackson.ModelResolver;
import io.swagger.v3.oas.annotations.media.DiscriminatorMapping;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.models.media.ComposedSchema;
import io.swagger.v3.oas.models.media.JsonSchema;
import org.testng.annotations.Test;

import static io.swagger.v3.core.resolving.SwaggerTestBase.mapper;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertTrue;

public class Ticket4904Test {

    @Test
    public void testComposedSchemaWithDiscriminator() {
        final ModelResolver modelResolver = new ModelResolver(mapper());
        modelResolver.setOpenapi31(true);
        final ModelConverterContextImpl context = new ModelConverterContextImpl(modelResolver);

        io.swagger.v3.oas.models.media.Schema schema = context.resolve(new AnnotatedType(ParentClass.class));

        assertNotNull(schema);
        assertTrue(schema instanceof JsonSchema, "Expected JsonSchema in OpenAPI 3.1 mode, but got: " + schema.getClass());
        JsonSchema jsonSchema = (JsonSchema) schema;
        assertNotNull(jsonSchema.getOneOf());
        assertEquals(jsonSchema.getOneOf().size(), 2);
        assertEquals(( jsonSchema.getOneOf().get(0)).get$ref(), "#/components/schemas/ChildClassA");
        assertEquals(( jsonSchema.getOneOf().get(1)).get$ref(), "#/components/schemas/ChildClassB");
        assertNotNull(jsonSchema.getDiscriminator());
        assertEquals(jsonSchema.getDiscriminator().getPropertyName(), "objectType");
        assertNotNull(jsonSchema.getDiscriminator().getMapping());
        assertEquals(jsonSchema.getDiscriminator().getMapping().size(), 2);
        assertEquals(jsonSchema.getDiscriminator().getMapping().get("A"), "#/components/schemas/ChildClassA");
        assertEquals(jsonSchema.getDiscriminator().getMapping().get("B"), "#/components/schemas/ChildClassB");
    }

    @Test
    public void testMultiElementAllOf() {
        final ModelResolver modelResolver = new ModelResolver(mapper());
        modelResolver.setOpenapi31(true);
        final ModelConverterContextImpl context = new ModelConverterContextImpl(modelResolver);

        io.swagger.v3.oas.models.media.Schema schema = context.resolve(new AnnotatedType(MultiAllOfClass.class));

        assertNotNull(schema);
        assertTrue(schema instanceof JsonSchema, "Expected JsonSchema in OpenAPI 3.1 mode, but got: " + schema.getClass());
        assertNotNull(schema.getAllOf());
        assertEquals(schema.getAllOf().size(), 3);
        assertEquals(((io.swagger.v3.oas.models.media.Schema) schema.getAllOf().get(0)).get$ref(), "#/components/schemas/ClassA");
        assertEquals(((io.swagger.v3.oas.models.media.Schema) schema.getAllOf().get(1)).get$ref(), "#/components/schemas/ClassB");
        assertEquals(((io.swagger.v3.oas.models.media.Schema) schema.getAllOf().get(2)).get$ref(), "#/components/schemas/ClassC");
    }

    @Test
    public void testMultiElementAllOfOpenApi30() {
        final ModelResolver modelResolver = new ModelResolver(mapper());
        modelResolver.setOpenapi31(false);
        final ModelConverterContextImpl context = new ModelConverterContextImpl(modelResolver);

        io.swagger.v3.oas.models.media.Schema schema = context.resolve(new AnnotatedType(MultiAllOfClass.class));

        assertNotNull(schema);
        assertTrue(schema instanceof ComposedSchema, "Expected ComposedSchema in OpenAPI 3.0 mode, but got: " + schema.getClass());
        ComposedSchema composedSchema = (ComposedSchema) schema;
        assertNotNull(composedSchema.getAllOf());
        assertEquals(composedSchema.getAllOf().size(), 3);
        assertEquals(composedSchema.getAllOf().get(0).get$ref(), "#/components/schemas/ClassA");
        assertEquals(composedSchema.getAllOf().get(1).get$ref(), "#/components/schemas/ClassB");
        assertEquals(composedSchema.getAllOf().get(2).get$ref(), "#/components/schemas/ClassC");
    }

    @Test
    public void testMixedComposition() {
        final ModelResolver modelResolver = new ModelResolver(mapper());
        modelResolver.setOpenapi31(true);
        final ModelConverterContextImpl context = new ModelConverterContextImpl(modelResolver);

        io.swagger.v3.oas.models.media.Schema schema = context.resolve(new AnnotatedType(MixedCompositionClass.class));

        assertNotNull(schema);
        assertTrue(schema instanceof JsonSchema, "Expected JsonSchema in OpenAPI 3.1 mode, but got: " + schema.getClass());

        JsonSchema jsonSchema = (JsonSchema) schema;
        assertNotNull(jsonSchema.getAllOf());
        assertEquals(jsonSchema.getAllOf().size(), 3);
        assertEquals(jsonSchema.getAllOf().get(0).get$ref(), "#/components/schemas/ClassA");
        assertEquals(jsonSchema.getAllOf().get(1).get$ref(), "#/components/schemas/ClassB");
        io.swagger.v3.oas.models.media.Schema inlineSchema = jsonSchema.getAllOf().get(2);
        assertNotNull(inlineSchema);
        assertEquals(inlineSchema.get$ref(), null);
        assertNotNull(inlineSchema.getProperties());
        assertTrue(inlineSchema.getProperties().containsKey("additionalProperty"));
        io.swagger.v3.oas.models.media.Schema additionalPropertySchema =
            (io.swagger.v3.oas.models.media.Schema) inlineSchema.getProperties().get("additionalProperty");
        assertNotNull(additionalPropertySchema);
        assertEquals(additionalPropertySchema.getDescription(), "Additional property");
    }

    @Test
    public void testAnyOfComposition() {
        final ModelResolver modelResolver = new ModelResolver(mapper());
        modelResolver.setOpenapi31(true);
        final ModelConverterContextImpl context = new ModelConverterContextImpl(modelResolver);

        io.swagger.v3.oas.models.media.Schema schema = context.resolve(new AnnotatedType(AnyOfClass.class));

        assertNotNull(schema);
        assertTrue(schema instanceof JsonSchema, "Expected JsonSchema in OpenAPI 3.1 mode, but got: " + schema.getClass());
        JsonSchema jsonSchema = (JsonSchema) schema;
        assertNotNull(jsonSchema.getAnyOf());
        assertEquals(jsonSchema.getAnyOf().size(), 3);
        assertEquals(((io.swagger.v3.oas.models.media.Schema) jsonSchema.getAnyOf().get(0)).get$ref(), "#/components/schemas/ClassA");
        assertEquals(((io.swagger.v3.oas.models.media.Schema) jsonSchema.getAnyOf().get(1)).get$ref(), "#/components/schemas/ClassB");
        assertEquals(((io.swagger.v3.oas.models.media.Schema) jsonSchema.getAnyOf().get(2)).get$ref(), "#/components/schemas/ClassC");
    }

    @Test
    public void testOneOfWithReferences() {
        final ModelResolver modelResolver = new ModelResolver(mapper());
        modelResolver.setOpenapi31(true);
        final ModelConverterContextImpl context = new ModelConverterContextImpl(modelResolver);

        io.swagger.v3.oas.models.media.Schema schema = context.resolve(new AnnotatedType(OneOfWithRefsClass.class));

        assertNotNull(schema);
        assertTrue(schema instanceof JsonSchema, "Expected JsonSchema in OpenAPI 3.1 mode, but got: " + schema.getClass());
        JsonSchema jsonSchema = (JsonSchema) schema;
        assertNotNull(jsonSchema.getOneOf());
        assertEquals(jsonSchema.getOneOf().size(), 2);
        assertEquals((jsonSchema.getOneOf().get(0)).get$ref(), "#/components/schemas/RefSubtypeA");
        assertEquals((jsonSchema.getOneOf().get(1)).get$ref(), "#/components/schemas/RefSubtypeB");
    }

    @Test
    public void testJacksonPolymorphismWithTypeInfo() {
        final ModelResolver modelResolver = new ModelResolver(mapper());
        modelResolver.setOpenapi31(true);
        final ModelConverterContextImpl context = new ModelConverterContextImpl(modelResolver);

        io.swagger.v3.oas.models.media.Schema schema = context.resolve(new AnnotatedType(JacksonPolymorphicParent.class));

        assertNotNull(schema);
        assertTrue(schema instanceof JsonSchema, "Expected JsonSchema in OpenAPI 3.1 mode, but got: " + schema.getClass());
        JsonSchema jsonSchema = (JsonSchema) schema;
        if (jsonSchema.getOneOf() != null) {
            assertEquals(jsonSchema.getOneOf().size(), 2);
            assertEquals((jsonSchema.getOneOf().get(0)).get$ref(), "#/components/schemas/JacksonChildA");
            assertEquals((jsonSchema.getOneOf().get(1)).get$ref(), "#/components/schemas/JacksonChildB");
            assertNotNull(jsonSchema.getDiscriminator());
            assertEquals(jsonSchema.getDiscriminator().getPropertyName(), "type");
        } else {
            assertNotNull(jsonSchema.getProperties());
            assertTrue(jsonSchema.getProperties().containsKey("commonProperty") ||
                      jsonSchema.getProperties().containsKey("type"),
                      "Expected to find either commonProperty or type discriminator property");
        }
    }

    @Test
    public void testComplexAllOfWithInlineProperties() {
        final ModelResolver modelResolver = new ModelResolver(mapper());
        modelResolver.setOpenapi31(true);
        final ModelConverterContextImpl context = new ModelConverterContextImpl(modelResolver);

        io.swagger.v3.oas.models.media.Schema schema = context.resolve(new AnnotatedType(ComplexAllOfClass.class));

        assertNotNull(schema);
        assertTrue(schema instanceof JsonSchema, "Expected JsonSchema in OpenAPI 3.1 mode, but got: " + schema.getClass());
        JsonSchema jsonSchema = (JsonSchema) schema;
        assertNotNull(jsonSchema.getAllOf());
        assertEquals(jsonSchema.getAllOf().size(), 3);
        assertEquals((jsonSchema.getAllOf().get(0)).get$ref(), "#/components/schemas/ClassA");
        assertEquals((jsonSchema.getAllOf().get(1)).get$ref(), "#/components/schemas/ClassB");
        io.swagger.v3.oas.models.media.Schema inlineSchema =  jsonSchema.getAllOf().get(2);
        assertNotNull(inlineSchema);
        assertEquals(inlineSchema.get$ref(), null);
        assertNotNull(inlineSchema.getProperties());
        assertTrue(inlineSchema.getProperties().containsKey("complexProperty"));
        assertTrue(inlineSchema.getProperties().containsKey("additionalField"));
    }

    @Test
    public void testNestedComposition() {
        final ModelResolver modelResolver = new ModelResolver(mapper());
        modelResolver.setOpenapi31(true);
        final ModelConverterContextImpl context = new ModelConverterContextImpl(modelResolver);

        io.swagger.v3.oas.models.media.Schema schema = context.resolve(new AnnotatedType(NestedCompositionClass.class));

        assertNotNull(schema);
        assertTrue(schema instanceof JsonSchema, "Expected JsonSchema in OpenAPI 3.1 mode, but got: " + schema.getClass());
        JsonSchema jsonSchema = (JsonSchema) schema;
        assertNotNull(jsonSchema.getOneOf());
        assertNotNull(jsonSchema.getAllOf());
        assertEquals(jsonSchema.getOneOf().size(), 2);
        assertEquals(jsonSchema.getAllOf().size(), 1);
        assertEquals((jsonSchema.getOneOf().get(0)).get$ref(), "#/components/schemas/ChildClassA");
        assertEquals((jsonSchema.getOneOf().get(1)).get$ref(), "#/components/schemas/ChildClassB");
        assertEquals((jsonSchema.getAllOf().get(0)).get$ref(), "#/components/schemas/ClassA");
    }

    @Schema(
            type = "object",
            discriminatorMapping = {
                    @DiscriminatorMapping(value = "A", schema = ChildClassA.class),
                    @DiscriminatorMapping(value = "B", schema = ChildClassB.class)
            },
            oneOf = {ChildClassA.class, ChildClassB.class},
            discriminatorProperty = "objectType"
    )
    public abstract static class ParentClass {
    }

    public static class ChildClassA extends ParentClass {
    }

    public static class ChildClassB extends ParentClass {
    }

    @Schema(allOf = {ClassA.class, ClassB.class, ClassC.class})
    public static class MultiAllOfClass {
    }

    @Schema(
            allOf = {ClassA.class, ClassB.class},
            type = "object",
            description = "Mixed composition with allOf and additional properties",
            format = "custom-format"
    )
    public static class MixedCompositionClass {
        @Schema(description = "Additional property")
        public String additionalProperty;
    }

    public static class ClassA {
        public String propertyA;
    }

    public static class ClassB {
        public String propertyB;
    }

    public static class ClassC {
        public String propertyC;
    }

    @Schema(anyOf = {ClassA.class, ClassB.class, ClassC.class})
    public static class AnyOfClass {
    }

    @Schema(oneOf = {RefSubtypeA.class, RefSubtypeB.class})
    public static class OneOfWithRefsClass {
    }

    @JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = "type")
    @JsonSubTypes({
            @JsonSubTypes.Type(value = JacksonChildA.class, name = "A"),
            @JsonSubTypes.Type(value = JacksonChildB.class, name = "B")
    })
    public static class JacksonPolymorphicParent {
        public String commonProperty;
    }

    public static class JacksonChildA extends JacksonPolymorphicParent {
        public String childAProperty;
    }

    public static class JacksonChildB extends JacksonPolymorphicParent {
        public String childBProperty;
    }

    @Schema(allOf = {ClassA.class, ClassB.class})
    public static class ComplexAllOfClass {
        @Schema(description = "Complex property with nested structure")
        public String complexProperty;

        @Schema(description = "Additional field for testing")
        public String additionalField;
    }

    @Schema(
            oneOf = {ChildClassA.class, ChildClassB.class},
            allOf = {ClassA.class}
    )
    public static class NestedCompositionClass {
    }

    // Additional classes for $ref subtypes testing
    public static class RefSubtypeA {
        public String refPropertyA;
    }

    public static class RefSubtypeB {
        public String refPropertyB;
    }

}

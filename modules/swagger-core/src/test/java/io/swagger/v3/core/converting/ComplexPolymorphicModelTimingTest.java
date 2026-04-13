package io.swagger.v3.core.converting;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.v3.core.converter.AnnotatedType;
import io.swagger.v3.core.converter.ModelConverterContextImpl;
import io.swagger.v3.core.converter.ModelConverters;
import io.swagger.v3.core.converter.ResolvedSchema;
import io.swagger.v3.core.jackson.ModelResolver;
import io.swagger.v3.core.oas.models.ModelWithArrayOfSubclasses;
import io.swagger.v3.core.oas.models.ModelWithManySubtypesAndRecursion;
import io.swagger.v3.oas.models.media.ArraySchema;
import io.swagger.v3.oas.models.media.Schema;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.AssertJUnit;
import org.testng.annotations.Test;

import java.lang.reflect.Field;
import java.util.Map;
import java.util.Set;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertTrue;

public class ComplexPolymorphicModelTimingTest {
    private static final Logger LOGGER = LoggerFactory.getLogger(ComplexPolymorphicModelTimingTest.class);


    @Test
    @SuppressWarnings("unchecked")
    public void complexPolymorphicModelNoDuplicateResolutionTest() throws Exception {
        ModelConverterContextImpl context = new ModelConverterContextImpl(new ModelResolver(new ObjectMapper()));
        context.resolve(new AnnotatedType(ModelWithManySubtypesAndRecursion.Holder.class));

        Field processedTypesField = ModelConverterContextImpl.class.getDeclaredField("processedTypes");
        processedTypesField.setAccessible(true);
        Set<AnnotatedType> processedTypes = (Set<AnnotatedType>) processedTypesField.get(context);

        Class<?>[] subtypes = {
            ModelWithManySubtypesAndRecursion.Base.class,
            ModelWithManySubtypesAndRecursion.SubA.class,
            ModelWithManySubtypesAndRecursion.SubB.class,
            ModelWithManySubtypesAndRecursion.SubC.class,
            ModelWithManySubtypesAndRecursion.SubD.class,
            ModelWithManySubtypesAndRecursion.SubE.class,
            ModelWithManySubtypesAndRecursion.SubF.class,
            ModelWithManySubtypesAndRecursion.SubG.class,
            ModelWithManySubtypesAndRecursion.SubH.class,
            ModelWithManySubtypesAndRecursion.SubI.class,
            ModelWithManySubtypesAndRecursion.SubJ.class,
        };

        for (Class<?> subtype : subtypes) {
            long count = processedTypes.stream()
                    .filter(at -> subtype.equals(at.getType()))
                    .count();
            assertEquals(count, 1,
                    subtype.getSimpleName() + " should be cached and resolved only once, not " + count + " times");
        }
    }

    @Test
    public void testSchemaStructureCorrectness() {
        ResolvedSchema resolvedSchema = ModelConverters.getInstance(false)
                .readAllAsResolvedSchema(ModelWithManySubtypesAndRecursion.Holder.class);

        assertNotNull(resolvedSchema, "ResolvedSchema should not be null");
        assertNotNull(resolvedSchema.referencedSchemas, "Referenced schemas should not be null");

        Schema baseSchema = resolvedSchema.referencedSchemas.get("Base");
        assertNotNull(baseSchema, "Base schema should be in referenced schemas");
        assertNotNull(baseSchema.getDiscriminator(), "Base schema should have discriminator");
        assertEquals(baseSchema.getDiscriminator().getPropertyName(), "name",
                "Discriminator property should be 'name'");

        Map<String, String> mappings = baseSchema.getDiscriminator().getMapping();
        assertNotNull(mappings, "Discriminator mappings should not be null");
        assertEquals(mappings.size(), 10, "Should have 10 discriminator mappings");

        String[] expectedMappings = {"a", "b", "c", "d", "e", "f", "g", "h", "i", "j"};
        String[] expectedSchemas = {"SubA", "SubB", "SubC", "SubD", "SubE", "SubF", "SubG", "SubH", "SubI", "SubJ"};

        for (int i = 0; i < expectedMappings.length; i++) {
            String mapping = expectedMappings[i];
            String schemaName = expectedSchemas[i];
            assertTrue(mappings.containsKey(mapping),
                    "Mapping for '" + mapping + "' should exist");
            assertEquals(mappings.get(mapping), "#/components/schemas/" + schemaName,
                    "Mapping for '" + mapping + "' should reference " + schemaName);

            Schema subSchema = resolvedSchema.referencedSchemas.get(schemaName);
            assertNotNull(subSchema, schemaName + " should be in referenced schemas");
        }
    }

    @Test
    @SuppressWarnings("unchecked")
    public void testNonArrayRecursiveReferences() throws Exception {
        ModelConverterContextImpl context = new ModelConverterContextImpl(new ModelResolver(new ObjectMapper()));
        context.resolve(new AnnotatedType(ModelWithManySubtypesAndRecursion.RecursiveNode.class));

        Field processedTypesField = ModelConverterContextImpl.class.getDeclaredField("processedTypes");
        processedTypesField.setAccessible(true);
        Set<AnnotatedType> processedTypes = (Set<AnnotatedType>) processedTypesField.get(context);

        long count = processedTypes.stream()
                .filter(at -> ModelWithManySubtypesAndRecursion.RecursiveNode.class.equals(at.getType()))
                .count();
        assertEquals(count, 1,
                "RecursiveNode should be cached and resolved only once despite direct recursion");

        Map<String, Schema> definedModels = context.getDefinedModels();
        Schema recursiveNodeSchema = definedModels.get("RecursiveNode");
        assertNotNull(recursiveNodeSchema, "RecursiveNode schema should exist");
        assertNotNull(recursiveNodeSchema.getProperties(), "RecursiveNode should have properties");

        Object parentPropObj = recursiveNodeSchema.getProperties().get("parent");
        assertNotNull(parentPropObj, "parent property should exist");
        assertTrue(parentPropObj instanceof Schema, "parent property should be a Schema");
        Schema parentProp = (Schema) parentPropObj;
        assertEquals(parentProp.get$ref(), "#/components/schemas/RecursiveNode",
                "parent property should reference RecursiveNode");

        Object childrenPropObj = recursiveNodeSchema.getProperties().get("children");
        assertNotNull(childrenPropObj, "children property should exist");
        assertTrue(childrenPropObj instanceof ArraySchema, "children property should be an ArraySchema");
        ArraySchema childrenProp = (ArraySchema) childrenPropObj;
        Schema items = childrenProp.getItems();
        assertNotNull(items, "children items should not be null");
        assertEquals(items.get$ref(), "#/components/schemas/RecursiveNode",
                "children items should reference RecursiveNode");
    }

    @Test
    @SuppressWarnings("unchecked")
    public void testOpenAPI31ResolveSiblingsPath() throws Exception {
        ModelResolver resolver = new ModelResolver(new ObjectMapper()).openapi31(true);
        ModelConverterContextImpl context = new ModelConverterContextImpl(resolver);
        context.resolve(new AnnotatedType(ModelWithManySubtypesAndRecursion.Holder.class));

        Field processedTypesField = ModelConverterContextImpl.class.getDeclaredField("processedTypes");
        processedTypesField.setAccessible(true);
        Set<AnnotatedType> processedTypes = (Set<AnnotatedType>) processedTypesField.get(context);

        Class<?>[] subtypes = {
            ModelWithManySubtypesAndRecursion.Base.class,
            ModelWithManySubtypesAndRecursion.SubA.class,
            ModelWithManySubtypesAndRecursion.SubB.class,
            ModelWithManySubtypesAndRecursion.SubC.class,
            ModelWithManySubtypesAndRecursion.SubD.class,
            ModelWithManySubtypesAndRecursion.SubE.class,
            ModelWithManySubtypesAndRecursion.SubF.class,
            ModelWithManySubtypesAndRecursion.SubG.class,
            ModelWithManySubtypesAndRecursion.SubH.class,
            ModelWithManySubtypesAndRecursion.SubI.class,
            ModelWithManySubtypesAndRecursion.SubJ.class,
        };

        for (Class<?> subtype : subtypes) {
            long count = processedTypes.stream()
                    .filter(at -> subtype.equals(at.getType()))
                    .count();
            assertEquals(count, 1,
                    subtype.getSimpleName() + " should be cached in OAS 3.1 mode, not " + count + " times");
        }

        Map<String, Schema> definedModels = context.getDefinedModels();
        Schema baseSchema = definedModels.get("Base");
        assertNotNull(baseSchema, "Base schema should exist in OAS 3.1 mode");

        final long durationOAS31 = measureTiming(1,
                () -> ModelConverters.getInstance(true)
                        .readAllAsResolvedSchema(ModelWithManySubtypesAndRecursion.Holder.class));
        LOGGER.debug("OAS 3.1 duration: " + durationOAS31 + "ms");

        assertTrue(durationOAS31 < 5000,
                "OAS 3.1 resolution should complete in reasonable time: " + durationOAS31 + "ms");
    }

    @Test
    public void testDiscriminatorMappingIntegrity() {
        ResolvedSchema resolvedSchema = ModelConverters.getInstance(false)
                .readAllAsResolvedSchema(ModelWithManySubtypesAndRecursion.Holder.class);

        Schema baseSchema = resolvedSchema.referencedSchemas.get("Base");
        assertNotNull(baseSchema.getDiscriminator(), "Base should have discriminator");

        Map<String, String> mappings = baseSchema.getDiscriminator().getMapping();

        for (char c = 'a'; c <= 'j'; c++) {
            String key = String.valueOf(c);
            assertTrue(mappings.containsKey(key),
                    "Discriminator should contain mapping for '" + key + "'");

            String ref = mappings.get(key);
            assertNotNull(ref, "Mapping for '" + key + "' should not be null");
            assertTrue(ref.startsWith("#/components/schemas/"),
                    "Mapping should be a valid $ref: " + ref);

            String schemaName = ref.substring("#/components/schemas/".length());
            assertNotNull(resolvedSchema.referencedSchemas.get(schemaName),
                    "Schema '" + schemaName + "' referenced in discriminator mapping should exist");
        }
    }

    @Test
    @SuppressWarnings("unchecked")
    public void testContextPropagatedThroughArraySchemaAllOf() throws Exception {
        ModelResolver resolver = new ModelResolver(new ObjectMapper()).openapi31(true);
        ModelConverterContextImpl context = new ModelConverterContextImpl(resolver);
        context.resolve(new AnnotatedType(ModelWithManySubtypesAndRecursion.HolderWithAllOfInArraySchema.class));

        Field processedTypesField = ModelConverterContextImpl.class.getDeclaredField("processedTypes");
        processedTypesField.setAccessible(true);
        Set<AnnotatedType> processedTypes = (Set<AnnotatedType>) processedTypesField.get(context);

        long count = processedTypes.stream()
                .filter(at -> ModelWithManySubtypesAndRecursion.AllOfTarget.class.equals(at.getType()))
                .count();
        assertEquals(count, 1,
                "AllOfTarget referenced via arraySchema.allOf should be resolved through context.resolve() " +
                "and appear in processedTypes exactly once, but was found " + count + " times. " +
                "This indicates context was not propagated through the 6-arg getSchemaFromAnnotation overload.");
    }


    @Test
    @SuppressWarnings("unchecked")
    public void testContextPropagatedThroughItemsSchemaAllOf() throws Exception {
        ModelResolver resolver = new ModelResolver(new ObjectMapper()).openapi31(true);
        ModelConverterContextImpl context = new ModelConverterContextImpl(resolver);
        context.resolve(new AnnotatedType(ModelWithManySubtypesAndRecursion.HolderWithAllOfInItemsSchema.class));

        Field processedTypesField = ModelConverterContextImpl.class.getDeclaredField("processedTypes");
        processedTypesField.setAccessible(true);
        Set<AnnotatedType> processedTypes = (Set<AnnotatedType>) processedTypesField.get(context);

        long count = processedTypes.stream()
                .filter(at -> ModelWithManySubtypesAndRecursion.AllOfTarget.class.equals(at.getType()))
                .count();
        assertEquals(count, 1,
                "AllOfTarget referenced via @ArraySchema(schema = @Schema(allOf = {...})) should be resolved " +
                "through context.resolve() and appear in processedTypes exactly once, but was found " + count + " times.");
    }
}

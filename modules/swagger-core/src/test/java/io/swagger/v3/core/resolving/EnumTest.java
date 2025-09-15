package io.swagger.v3.core.resolving;

import com.google.common.base.Functions;
import com.google.common.collect.Collections2;
import io.swagger.v3.core.converter.AnnotatedType;
import io.swagger.v3.core.converter.ModelConverterContextImpl;
import io.swagger.v3.core.jackson.ModelResolver;
import io.swagger.v3.oas.models.media.Schema;
import io.swagger.v3.oas.models.media.StringSchema;
import org.testng.annotations.Test;

import java.util.*;
import java.util.stream.Collectors;

import static org.testng.Assert.*;
import static org.testng.AssertJUnit.assertFalse;

public class EnumTest extends SwaggerTestBase {

    @Test
    public void testEnum() {
        final ModelResolver modelResolver = new ModelResolver(mapper());
        final ModelConverterContextImpl context = new ModelConverterContextImpl(modelResolver);

        final Schema model = context.resolve((new AnnotatedType().type(Currency.class)));
        assertNotNull(model);
        assertTrue(model instanceof StringSchema);
        final StringSchema strModel = (StringSchema) model;
        assertNotNull(strModel.getEnum());
        final Collection<String> modelValues =
                new ArrayList<String>(Collections2.transform(Arrays.asList(Currency.values()), Functions.toStringFunction()));
        assertEquals(strModel.getEnum(), modelValues);

        final Schema property = context.resolve(new AnnotatedType().type(Currency.class).schemaProperty(true));
        assertNotNull(property);
        assertTrue(property instanceof StringSchema);
        final StringSchema strProperty = (StringSchema) property;
        assertNotNull(strProperty.getEnum());
        final Collection<String> values =
                new ArrayList<>(Collections2.transform(Arrays.asList(Currency.values()), Functions.toStringFunction()));
        assertEquals(strProperty.getEnum(), values);
    }

    @Test
    public void testEnumGenerics() {
        final ModelResolver modelResolver = new ModelResolver(mapper());
        final ModelConverterContextImpl context = new ModelConverterContextImpl(modelResolver);

        final Schema model = context.resolve((new AnnotatedType().type(Contract.class)));
        assertBasicModelStructure(model, "Contract");
        assertPropertyExists(model, "type");
    }

    @Test
    public void testEnumPropertyWithSchemaAnnotation() {
        final ModelResolver modelResolver = new ModelResolver(mapper()).openapi31(true);
        final ModelConverterContextImpl context = new ModelConverterContextImpl(modelResolver);

        final Schema model = context.resolve(new AnnotatedType().type(ClassWithEnumAsRefProperty.class));
        assertBasicModelStructure(model, "ClassWithEnumAsRefProperty");
        assertPropertyExists(model, "enumWithSchemaProperty");

        final Schema enumPropertySchema = (Schema) model.getProperties().get("enumWithSchemaProperty");
        assertEnumAsRefProperty(enumPropertySchema, "#/components/schemas/EnumWithSchemaProperty");
        assertEquals(enumPropertySchema.getDescription(), "Property description");
    }

    @Test
    public void testEnumPropertyWithGlobalSwitchOnlyOpenApi31() {
        ModelResolver.enumsAsRef = true;
        final ModelResolver modelResolver = new ModelResolver(mapper()).openapi31(true);
        final ModelConverterContextImpl context = new ModelConverterContextImpl(modelResolver);

        final Schema model = context.resolve(new AnnotatedType().type(ClassWithPlainEnum.class));
        assertBasicModelStructure(model, "ClassWithPlainEnum");
        assertPropertyExists(model, "plainEnum");

        final Schema enumPropertySchema = (Schema) model.getProperties().get("plainEnum");
        assertNotNull(enumPropertySchema.get$ref());
        assertNull(enumPropertySchema.getEnum());
        assertEquals(enumPropertySchema.getDescription(), "Plain enum property");

        assertEnumComponentExists(context, ClassWithPlainEnum.PlainEnum.values(), null);

        // Reset the static field
        ModelResolver.enumsAsRef = false;
    }

    @Test
    public void testArrayOfEnumWithSchemaAnnotationOpenApi31() {
        final ModelResolver modelResolver = new ModelResolver(mapper()).openapi31(true);
        final ModelConverterContextImpl context = new ModelConverterContextImpl(modelResolver);

        final Schema model = context.resolve(new AnnotatedType().type(ClassWithEnumArray.class));
        assertBasicModelStructure(model, "ClassWithEnumArray");
        assertPropertyExists(model, "enumArray");

        final Schema arrayPropertySchema = (Schema) model.getProperties().get("enumArray");
        assertArrayWithEnumRef(arrayPropertySchema);

        assertEnumComponentExists(context, ClassWithEnumArray.ArrayEnum.values(), "Enum description");
    }

    @Test
    public void testArrayOfEnumWithSchemaAnnotationOpenApi30() {
        final ModelResolver modelResolver = new ModelResolver(mapper()).openapi31(false);
        final ModelConverterContextImpl context = new ModelConverterContextImpl(modelResolver);

        final Schema model = context.resolve(new AnnotatedType().type(ClassWithEnumArray.class));
        assertBasicModelStructure(model, "ClassWithEnumArray");
        assertPropertyExists(model, "enumArray");

        final Schema arrayPropertySchema = (Schema) model.getProperties().get("enumArray");
        assertArrayWithEnumRef(arrayPropertySchema);

        assertEnumComponentExists(context, ClassWithEnumArray.ArrayEnum.values(), "Enum description");
    }

    @Test
    public void testControlTestNoRefOpenApi31() {
        ModelResolver.enumsAsRef = false;
        final ModelResolver modelResolver = new ModelResolver(mapper()).openapi31(true);
        final ModelConverterContextImpl context = new ModelConverterContextImpl(modelResolver);

        final Schema model = context.resolve(new AnnotatedType().type(ClassWithPlainEnum.class));
        assertBasicModelStructure(model, "ClassWithPlainEnum");
        assertPropertyExists(model, "plainEnum");

        final Schema enumPropertySchema = (Schema) model.getProperties().get("plainEnum");
        assertInlineEnumProperty(enumPropertySchema);

        // Apply broad assertions - verify no components are created for inline enums
        Map<String, Schema> components = context.getDefinedModels();
        if (components != null && !components.isEmpty()) {
            Set<String> expected = Arrays.stream(ClassWithPlainEnum.PlainEnum.values())
                    .map(Enum::name)
                    .collect(Collectors.toSet());
            Schema enumComponent = findEnumComponent(components, expected);
            assertNull(enumComponent, "No enum component should exist for inline enums");
        }
    }

    @Test
    public void testControlTestNoRefOpenApi30() {
        ModelResolver.enumsAsRef = false;
        final ModelResolver modelResolver = new ModelResolver(mapper()).openapi31(false);
        final ModelConverterContextImpl context = new ModelConverterContextImpl(modelResolver);

        final Schema model = context.resolve(new AnnotatedType().type(ClassWithPlainEnum.class));
        assertBasicModelStructure(model, "ClassWithPlainEnum");
        assertPropertyExists(model, "plainEnum");

        final Schema enumPropertySchema = (Schema) model.getProperties().get("plainEnum");
        assertInlineEnumProperty(enumPropertySchema);

        // Apply broad assertions - verify no components are created for inline enums
        Map<String, Schema> components = context.getDefinedModels();
        if (components != null && !components.isEmpty()) {
            Set<String> expected = Arrays.stream(ClassWithPlainEnum.PlainEnum.values())
                    .map(Enum::name)
                    .collect(Collectors.toSet());
            Schema enumComponent = findEnumComponent(components, expected);
            assertNull(enumComponent, "No enum component should exist for inline enums");
        }
    }

    @Test
    public void testEnumWithAllOfSchemaResolutionOpenApi30() {
        final ModelResolver modelResolver = new ModelResolver(mapper())
                .openapi31(false)
                .schemaResolution(Schema.SchemaResolution.ALL_OF);
        final ModelConverterContextImpl context = new ModelConverterContextImpl(modelResolver);

        final Schema model = context.resolve(new AnnotatedType().type(ClassWithEnumAsRefProperty.class));
        assertBasicModelStructure(model, "ClassWithEnumAsRefProperty");
        assertPropertyExists(model, "enumWithSchemaProperty");

        final Schema enumPropertySchema = (Schema) model.getProperties().get("enumWithSchemaProperty");

        boolean hasEnumRef = false;
        for (Object allOfItem : enumPropertySchema.getAllOf()) {
            if (allOfItem instanceof Schema) {
                Schema allOfSchema = (Schema) allOfItem;
                if ("#/components/schemas/EnumWithSchemaProperty".equals(allOfSchema.get$ref())) {
                    hasEnumRef = true;
                    break;
                }
            }
        }
        assertTrue(hasEnumRef, "AllOf should contain reference to enum component");
        assertEnumComponentExists(context, ClassWithEnumAsRefProperty.EnumWithSchemaProperty.values(), "Enum description");
    }

    private void assertBasicModelStructure(Schema model, String expectedName) {
        assertNotNull(model);
        assertEquals(model.getName(), expectedName);
    }

    private void assertPropertyExists(Schema model, String propertyName) {
        assertTrue(model.getProperties().containsKey(propertyName));
        assertNotNull(model.getProperties().get(propertyName));
    }

    private void assertEnumComponentExists(ModelConverterContextImpl context, Enum<?>[] enumValues, String expectedDescription) {
        Map<String, Schema> components = context.getDefinedModels();
        assertNotNull(components);
        assertFalse(components.isEmpty());

        Set<String> expected = Arrays.stream(enumValues)
                .map(Enum::name)
                .collect(Collectors.toCollection(LinkedHashSet::new));

        Schema enumComponent = findEnumComponent(components, expected);
        assertNotNull(enumComponent);
        assertEquals(enumComponent.getDescription(), expectedDescription);
    }

    private void assertEnumComponentExistsWithDefault(ModelConverterContextImpl context, Enum<?>[] enumValues, String expectedDescription, String expectedDefault) {
        assertEnumComponentExists(context, enumValues, expectedDescription);

        Map<String, Schema> components = context.getDefinedModels();
        Set<String> expected = Arrays.stream(enumValues)
                .map(Enum::name)
                .collect(Collectors.toCollection(LinkedHashSet::new));

        Schema enumComponent = findEnumComponent(components, expected);
        assertEquals(enumComponent.getDefault(), expectedDefault);
    }

    private Schema findEnumComponent(Map<String, Schema> components, Set<String> expectedValues) {
        return components.values().stream()
                .filter(Objects::nonNull)
                .filter(s -> s.getEnum() != null)
                .filter(s -> {
                    List<?> ev = s.getEnum();
                    Set<String> vals = ev.stream().map(Object::toString).collect(Collectors.toSet());
                    return vals.containsAll(expectedValues) && expectedValues.containsAll(vals);
                })
                .findFirst()
                .orElse(null);
    }

    private void assertEnumAsRefProperty(Schema propertySchema, String expectedRef) {
        assertEquals(propertySchema.get$ref(), expectedRef);
        assertNull(propertySchema.getEnum());
    }

    private void assertInlineEnumProperty(Schema propertySchema) {
        assertNotNull(propertySchema.getEnum());
        assertNull(propertySchema.get$ref());
    }

    private void assertArrayWithEnumRef(Schema arrayPropertySchema) {
        assertNotNull(arrayPropertySchema.getItems());
        assertNotNull(arrayPropertySchema.getItems().get$ref());
    }


    public static class ClassWithEnumAsRefProperty {

        @io.swagger.v3.oas.annotations.media.Schema(enumAsRef = true, description = "Property description", maximum = "1923234")
        public final EnumWithSchemaProperty enumWithSchemaProperty;

        public ClassWithEnumAsRefProperty(EnumWithSchemaProperty enumWithSchemaProperty) {
            this.enumWithSchemaProperty = enumWithSchemaProperty;
        }

        @io.swagger.v3.oas.annotations.media.Schema(description = "Enum description")
        public enum EnumWithSchemaProperty {
            VALUE1,
            VALUE2
        }
    }

    public static class ClassWithEnumArray {

        @io.swagger.v3.oas.annotations.media.ArraySchema(schema = @io.swagger.v3.oas.annotations.media.Schema(enumAsRef = true, description = "Property Description"))
        public final ArrayEnum[] enumArray;

        public ClassWithEnumArray(ArrayEnum[] enumArray) {
            this.enumArray = enumArray;
        }

        @io.swagger.v3.oas.annotations.media.Schema(description = "Enum description")
        public enum ArrayEnum {
            FIRST,
            SECOND,
            THIRD
        }
    }

    public static class ClassWithPlainEnum {

        @io.swagger.v3.oas.annotations.media.Schema(description = "Plain enum property")
        public final PlainEnum plainEnum;

        public ClassWithPlainEnum(PlainEnum plainEnum) {
            this.plainEnum = plainEnum;
        }

        public enum PlainEnum {
            ONE,
            TWO,
            THREE
        }
    }

    public enum Currency {
        USA, CANADA
    }

    public static class Contract {

        private Enum<?> type;

        public Enum<?> getType() {
            return type;
        }

        public Contract setType(Enum<?> type) {
            this.type = type;
            return this;
        }
    }
}

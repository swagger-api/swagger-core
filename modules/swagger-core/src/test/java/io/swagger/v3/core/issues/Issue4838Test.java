package io.swagger.v3.core.issues;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.v3.core.converter.AnnotatedType;
import io.swagger.v3.core.converter.ModelConverterContextImpl;
import io.swagger.v3.core.jackson.ModelResolver;
import io.swagger.v3.core.util.Configuration;
import io.swagger.v3.core.util.Json;
import io.swagger.v3.core.util.Json31;
import io.swagger.v3.oas.annotations.media.Schema;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.testng.annotations.Test;

import java.math.BigDecimal;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertNull;
import static org.testng.Assert.assertTrue;

public class Issue4838Test {

    @Test
    public void defaultValueShouldBeEmptyForStringFieldInOas30() {
        final ModelConverterContextImpl context = getModelConverterContext(Json.mapper(), false);

        final io.swagger.v3.oas.models.media.Schema model = context
                .resolve(new AnnotatedType(NullableStringModel.class));

        io.swagger.v3.oas.models.media.Schema nullableStringField =
                (io.swagger.v3.oas.models.media.Schema) model.getProperties().get("nullableStringField");

        assertNotNull(nullableStringField);
        assertTrue(nullableStringField.getNullable());
        assertNotNull(nullableStringField.getDefault());
        assertEquals(nullableStringField.getDefault(), "");
    }

    @Test
    public void defaultValueShouldBeNullForIntegerFieldInOas30() {
        final ModelConverterContextImpl context = getModelConverterContext(Json.mapper(), false);

        final io.swagger.v3.oas.models.media.Schema model = context
                .resolve(new AnnotatedType(NullableIntegerModel.class));

        io.swagger.v3.oas.models.media.Schema nullableIntegerField =
                (io.swagger.v3.oas.models.media.Schema) model.getProperties().get("nullableIntegerField");

        assertNotNull(nullableIntegerField);
        assertTrue(nullableIntegerField.getNullable());
        assertNull(nullableIntegerField.getExample());
        assertNull(nullableIntegerField.getDefault());
    }


    @Test
    public void defaultValueShouldBeProvidedFromBigDecimalSchemaForOas30() {
        final ModelConverterContextImpl context = getModelConverterContext(Json.mapper(), false);

        final io.swagger.v3.oas.models.media.Schema model = context
                .resolve(new AnnotatedType(NullableBigDecimalModel.class));

        io.swagger.v3.oas.models.media.Schema nullableBigDecimalField =
                (io.swagger.v3.oas.models.media.Schema) model.getProperties().get("nullableBigDecimalField");

        System.out.println(nullableBigDecimalField);
        assertNotNull(nullableBigDecimalField);
        assertTrue(nullableBigDecimalField.getNullable());
        assertNull(nullableBigDecimalField.getExample());
        assertEquals(nullableBigDecimalField.getDefault().toString(), "123");
    }

    @Test
    public void defaultValueShouldBeNullProvidedFromBooleanSchemaForOas30() {
        final ModelConverterContextImpl context = getModelConverterContext(Json.mapper(), false);

        final io.swagger.v3.oas.models.media.Schema model = context
                .resolve(new AnnotatedType(NullableBooleanModel.class));

        io.swagger.v3.oas.models.media.Schema nullableBooleanField =
                (io.swagger.v3.oas.models.media.Schema) model.getProperties().get("nullableBooleanField");

        assertNotNull(nullableBooleanField);
        assertTrue(nullableBooleanField.getNullable());
        assertNull(nullableBooleanField.getExample());
        assertNull(nullableBooleanField.getDefault());
    }

    @Test
    public void defaultValueShouldBeEmptyForStringFieldInOas31() {
        final ModelConverterContextImpl context = getModelConverterContext(Json31.mapper(), true);

        final io.swagger.v3.oas.models.media.Schema model = context
                .resolve(new AnnotatedType(NullableStringModel.class));

        io.swagger.v3.oas.models.media.Schema nullableStringField =
                (io.swagger.v3.oas.models.media.Schema) model.getProperties().get("nullableStringField");

        assertNotNull(nullableStringField);
        assertTrue(isNullableInOAS31(nullableStringField));
        assertNotNull(nullableStringField.getDefault());
        assertEquals(nullableStringField.getDefault(), "");
    }

    @Test
    public void defaultValueShouldBeNullForIntegerFieldInOas31() {
        final ModelConverterContextImpl context = getModelConverterContext(Json31.mapper(), true);

        final io.swagger.v3.oas.models.media.Schema model = context
                .resolve(new AnnotatedType(NullableIntegerModel.class));

        io.swagger.v3.oas.models.media.Schema nullableIntegerField =
                (io.swagger.v3.oas.models.media.Schema) model.getProperties().get("nullableIntegerField");

        assertNotNull(nullableIntegerField);
        assertTrue(isNullableInOAS31(nullableIntegerField));
        assertNull(nullableIntegerField.getExample());
        assertNull(nullableIntegerField.getDefault());
    }

    @Test
    public void defaultValueShouldBeProvidedFromBigDecimalSchemaForOas31() {
        final ModelConverterContextImpl context = getModelConverterContext(Json31.mapper(), true);

        final io.swagger.v3.oas.models.media.Schema model = context
                .resolve(new AnnotatedType(NullableBigDecimalModel.class));

        io.swagger.v3.oas.models.media.Schema nullableBigDecimalField =
                (io.swagger.v3.oas.models.media.Schema) model.getProperties().get("nullableBigDecimalField");

        assertNotNull(nullableBigDecimalField);
        assertTrue(isNullableInOAS31(nullableBigDecimalField));
        assertNull(nullableBigDecimalField.getExample());
        assertEquals(nullableBigDecimalField.getDefault(), 123);
    }


    @Test
    public void defaultValueShouldBeNullProvidedFromBooleanSchemaForOas31() {
        final ModelConverterContextImpl context = getModelConverterContext(Json31.mapper(), true);

        final io.swagger.v3.oas.models.media.Schema model = context
                .resolve(new AnnotatedType(NullableBooleanModel.class));

        io.swagger.v3.oas.models.media.Schema nullableBooleanField =
                (io.swagger.v3.oas.models.media.Schema) model.getProperties().get("nullableBooleanField");

        assertNotNull(nullableBooleanField);
        assertTrue(isNullableInOAS31(nullableBooleanField));
        assertNull(nullableBooleanField.getExample());
        assertNull(nullableBooleanField.getDefault());
    }

    private static @NonNull ModelConverterContextImpl getModelConverterContext(ObjectMapper mapper, boolean openAPI31) {
        final ModelResolver modelResolver = new ModelResolver(mapper);
        Configuration configuration = new Configuration();
        configuration.setOpenAPI31(openAPI31);
        modelResolver.setConfiguration(configuration);
        return new ModelConverterContextImpl(modelResolver);
    }

    private boolean isNullableInOAS31(io.swagger.v3.oas.models.media.Schema schema) {
        return schema.getTypes() != null && schema.getTypes().contains("null");
    }

    public static class NullableStringModel {
        @Schema(nullable = true, defaultValue = "")
        private String nullableStringField;

        public String getNullableStringField() {
            return nullableStringField;
        }

        public void setNullableStringField(String nullableStringField) {
            this.nullableStringField = nullableStringField;
        }
    }

    public static class NullableIntegerModel {
        @Schema(nullable = true, defaultValue = "null")
        private Integer nullableIntegerField;

        public Integer getNullableIntegerField() {
            return nullableIntegerField;
        }

        public void setNullableIntegerField(Integer nullableIntegerField) {
            this.nullableIntegerField = nullableIntegerField;
        }
    }

    public static class NullableBigDecimalModel {
        @Schema(nullable = true, defaultValue = "123")
        private BigDecimal nullableBigDecimalField;

        public BigDecimal getNullableBigDecimalField() {
            return nullableBigDecimalField;
        }

        public void setNullableBigDecimalField(BigDecimal nullableBigDecimalField) {
            this.nullableBigDecimalField = nullableBigDecimalField;
        }
    }

    public static class NullableBooleanModel {
        @Schema(nullable = true)
        private Boolean nullableBooleanField;

        public Boolean getNullableBooleanField() {
            return nullableBooleanField;
        }

        public void setNullableBooleanField(Boolean nullableBooleanField) {
            this.nullableBooleanField = nullableBooleanField;
        }
    }

}

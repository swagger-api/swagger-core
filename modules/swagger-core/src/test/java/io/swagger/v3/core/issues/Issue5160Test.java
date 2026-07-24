package io.swagger.v3.core.issues;

import io.swagger.v3.core.converter.AnnotatedType;
import io.swagger.v3.core.converter.ModelConverterContextImpl;
import io.swagger.v3.core.jackson.ModelResolver;
import io.swagger.v3.core.util.Configuration;
import io.swagger.v3.core.util.Json;
import io.swagger.v3.core.util.Json31;
import io.swagger.v3.oas.annotations.media.Schema;
import org.testng.annotations.Test;

import javax.annotation.Nullable;

import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertNotEquals;
import static org.testng.Assert.assertNull;
import static org.testng.Assert.assertTrue;
import static org.testng.Assert.assertEquals;

/**
 * Tests for GitHub Issue #5160 -
 * Schema.NullableMode enum mirroring RequiredMode / AccessMode.
 *
 * Validates the precedence chain:
 * 1. nullableMode = NULLABLE / NOT_NULLABLE wins over everything
 * 2. legacy nullable = true (deprecated) wins over auto-detection
 * 3. nullableMode = AUTO + no legacy nullable → consult @Nullable heuristics
 *
 * @see <a href="https://github.com/swagger-api/swagger-core/issues/5160">...</a>
 */
public class Issue5160Test {

    @Test
    public void testModeNullableForcesNullableOAS30() {
        io.swagger.v3.oas.models.media.Schema model = resolve30(NullableModeModel.class);
        io.swagger.v3.oas.models.media.Schema field =
                (io.swagger.v3.oas.models.media.Schema) model.getProperties().get("modeNullable");
        assertNotNull(field);
        assertEquals(field.getNullable(), Boolean.TRUE,
                "nullableMode=NULLABLE must force nullable=true in OAS 3.0");
    }

    @Test
    public void testModeNullableForcesNullableOAS31() {
        io.swagger.v3.oas.models.media.Schema model = resolve31(NullableModeModel.class);
        io.swagger.v3.oas.models.media.Schema field =
                (io.swagger.v3.oas.models.media.Schema) model.getProperties().get("modeNullable");
        assertNotNull(field);
        assertNotNull(field.getTypes(),
                "nullableMode=NULLABLE must produce a types array in OAS 3.1");
        assertTrue(field.getTypes().contains("null"),
                "nullableMode=NULLABLE must include 'null' in types array");
    }

    @Test
    public void testModeNotNullableOverridesNullableAnnotationOAS30() {
        io.swagger.v3.oas.models.media.Schema model = resolve30(NullableModeModel.class);
        io.swagger.v3.oas.models.media.Schema field =
                (io.swagger.v3.oas.models.media.Schema) model.getProperties().get("modeNotNullableWithAnnotation");
        assertNotNull(field);
        // Stricter than `!= true`: nullable must be unset (null), not explicitly false,
        // so the spec output omits the field rather than emitting `"nullable": false`.
        assertNull(field.getNullable(),
                "nullableMode=NOT_NULLABLE must clear nullable to null in OAS 3.0 (not Boolean.FALSE)");
    }

    @Test
    public void testModeNotNullableOverridesNullableAnnotationOAS31() {
        io.swagger.v3.oas.models.media.Schema model = resolve31(NullableModeModel.class);
        io.swagger.v3.oas.models.media.Schema field =
                (io.swagger.v3.oas.models.media.Schema) model.getProperties().get("modeNotNullableWithAnnotation");
        assertNotNull(field);
        if (field.getTypes() != null) {
            assertFalse(field.getTypes().contains("null"),
                    "nullableMode=NOT_NULLABLE must override @Nullable auto-detection in OAS 3.1");
        }
    }

    @Test
    public void testModeAutoWithAnnotationStillDetectsNullableOAS30() {
        io.swagger.v3.oas.models.media.Schema model = resolve30(NullableModeModel.class);
        io.swagger.v3.oas.models.media.Schema field =
                (io.swagger.v3.oas.models.media.Schema) model.getProperties().get("modeAutoWithAnnotation");
        assertNotNull(field);
        assertEquals(field.getNullable(), Boolean.TRUE,
                "nullableMode=AUTO must let @Nullable auto-detection apply in OAS 3.0");
    }

    @Test
    public void testModeAutoWithAnnotationStillDetectsNullableOAS31() {
        io.swagger.v3.oas.models.media.Schema model = resolve31(NullableModeModel.class);
        io.swagger.v3.oas.models.media.Schema field =
                (io.swagger.v3.oas.models.media.Schema) model.getProperties().get("modeAutoWithAnnotation");
        assertNotNull(field);
        assertNotNull(field.getTypes());
        assertTrue(field.getTypes().contains("null"),
                "nullableMode=AUTO must let @Nullable auto-detection apply in OAS 3.1");
    }

    @Test
    public void testModeAutoWithoutAnyNullableSignalIsNotNullableOAS30() {
        io.swagger.v3.oas.models.media.Schema model = resolve30(NullableModeModel.class);
        io.swagger.v3.oas.models.media.Schema field =
                (io.swagger.v3.oas.models.media.Schema) model.getProperties().get("modeAutoNoSignal");
        assertNotNull(field);
        assertNotEquals(field.getNullable(), Boolean.TRUE,
                "nullableMode=AUTO with no signals must not be nullable");
    }

    @Test
    public void testModeNotNullableWithoutAnnotationIsNotNullableOAS30() {
        io.swagger.v3.oas.models.media.Schema model = resolve30(NullableModeModel.class);
        io.swagger.v3.oas.models.media.Schema field =
                (io.swagger.v3.oas.models.media.Schema) model.getProperties().get("modeNotNullableOnly");
        assertNotNull(field);
        assertNull(field.getNullable(),
                "nullableMode=NOT_NULLABLE with no @Nullable must leave nullable unset (not Boolean.FALSE)");
    }

    @Test
    public void testLegacyNullableTrueStillWorksOAS30() {
        io.swagger.v3.oas.models.media.Schema model = resolve30(NullableModeModel.class);
        io.swagger.v3.oas.models.media.Schema field =
                (io.swagger.v3.oas.models.media.Schema) model.getProperties().get("legacyNullable");
        assertNotNull(field);
        assertEquals(field.getNullable(), Boolean.TRUE,
                "Legacy nullable=true must still work for backward compatibility");
    }

    @Test
    public void testLegacyNullableTrueStillWorksOAS31() {
        io.swagger.v3.oas.models.media.Schema model = resolve31(NullableModeModel.class);
        io.swagger.v3.oas.models.media.Schema field =
                (io.swagger.v3.oas.models.media.Schema) model.getProperties().get("legacyNullable");
        assertNotNull(field);
        assertNotNull(field.getTypes());
        assertTrue(field.getTypes().contains("null"),
                "Legacy nullable=true must still produce types array in OAS 3.1");
    }

    private io.swagger.v3.oas.models.media.Schema resolve30(Class<?> clazz) {
        ModelResolver modelResolver = new ModelResolver(Json.mapper());
        Configuration configuration = new Configuration();
        configuration.setOpenAPI31(false);
        modelResolver.setConfiguration(configuration);
        ModelConverterContextImpl context = new ModelConverterContextImpl(modelResolver);
        io.swagger.v3.oas.models.media.Schema model = context.resolve(new AnnotatedType(clazz));
        assertNotNull(model);
        assertNotNull(model.getProperties());
        return model;
    }

    private io.swagger.v3.oas.models.media.Schema resolve31(Class<?> clazz) {
        ModelResolver modelResolver = new ModelResolver(Json31.mapper());
        Configuration configuration = new Configuration();
        configuration.setOpenAPI31(true);
        modelResolver.setConfiguration(configuration);
        ModelConverterContextImpl context = new ModelConverterContextImpl(modelResolver);
        io.swagger.v3.oas.models.media.Schema model = context.resolve(new AnnotatedType(clazz));
        assertNotNull(model);
        assertNotNull(model.getProperties());
        return model;
    }

    public static class NullableModeModel {
        @Schema(nullableMode = Schema.NullableMode.NULLABLE)
        private String modeNullable;

        @Nullable
        @Schema(nullableMode = Schema.NullableMode.NOT_NULLABLE)
        private String modeNotNullableWithAnnotation;

        @Schema(nullableMode = Schema.NullableMode.NOT_NULLABLE)
        private String modeNotNullableOnly;

        @Nullable
        @Schema(description = "auto with annotation")
        private String modeAutoWithAnnotation;

        @Schema(description = "auto with no nullable signal")
        private String modeAutoNoSignal;

        @SuppressWarnings("deprecation")
        @Schema(nullable = true)
        private String legacyNullable;

        public String getModeNullable() { return modeNullable; }
        public void setModeNullable(String s) { this.modeNullable = s; }

        public String getModeNotNullableWithAnnotation() { return modeNotNullableWithAnnotation; }
        public void setModeNotNullableWithAnnotation(String s) { this.modeNotNullableWithAnnotation = s; }

        public String getModeNotNullableOnly() { return modeNotNullableOnly; }
        public void setModeNotNullableOnly(String s) { this.modeNotNullableOnly = s; }

        public String getModeAutoWithAnnotation() { return modeAutoWithAnnotation; }
        public void setModeAutoWithAnnotation(String s) { this.modeAutoWithAnnotation = s; }

        public String getModeAutoNoSignal() { return modeAutoNoSignal; }
        public void setModeAutoNoSignal(String s) { this.modeAutoNoSignal = s; }

        public String getLegacyNullable() { return legacyNullable; }
        public void setLegacyNullable(String s) { this.legacyNullable = s; }
    }
}

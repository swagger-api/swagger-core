package io.swagger.v3.core.resolving;

import io.swagger.v3.core.converter.ModelConverters;
import io.swagger.v3.oas.models.media.IntegerSchema;
import io.swagger.v3.oas.models.media.Schema;
import org.testng.annotations.Test;

import javax.validation.Constraint;
import javax.validation.OverridesAttribute;
import javax.validation.Payload;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.Map;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;

public class ComposedConstraintMetaAnnotationTest {

    @Min(0)
    @Max(999)
    @Target({ElementType.FIELD, ElementType.PARAMETER, ElementType.ANNOTATION_TYPE})
    @Retention(RetentionPolicy.RUNTIME)
    @Constraint(validatedBy = {})
    public @interface ValidStoreId {
        String message() default "Invalid store ID";
        Class<?>[] groups() default {};
        Class<? extends Payload>[] payload() default {};
    }

    @Size(min = 1, max = 50)
    @Target({ElementType.FIELD, ElementType.PARAMETER})
    @Retention(RetentionPolicy.RUNTIME)
    @Constraint(validatedBy = {})
    public @interface ValidName {
        String message() default "Invalid name";
        Class<?>[] groups() default {};
        Class<? extends Payload>[] payload() default {};
    }

    @Pattern(regexp = "^[a-z0-9._%+\\-]+@[a-z0-9.\\-]+\\.[a-z]{2,}$")
    @Target({ElementType.FIELD, ElementType.PARAMETER})
    @Retention(RetentionPolicy.RUNTIME)
    @Constraint(validatedBy = {})
    public @interface ValidEmail {
        String message() default "Invalid email";
        Class<?>[] groups() default {};
        Class<? extends Payload>[] payload() default {};
    }

    /**
     * Mimics how Hibernate Validator's @Range works: meta-annotations @Min/@Max carry default
     * values, while the actual per-use values are meant to be applied via @OverridesAttribute.
     * Our implementation reads meta-annotations from the annotation *type definition*, so it
     * always sees the defaults (min=0, max=Long.MAX_VALUE) — not whatever the caller passes
     * as @ValidRange(min=5, max=50). This is a known limitation documented by the test below.
     */
    @Min(0)
    @Max(Long.MAX_VALUE)
    @Target({ElementType.FIELD, ElementType.PARAMETER})
    @Retention(RetentionPolicy.RUNTIME)
    @Constraint(validatedBy = {})
    public @interface ValidRange {
        @OverridesAttribute(constraint = Min.class, name = "value")
        long min() default 0;

        @OverridesAttribute(constraint = Max.class, name = "value")
        long max() default Long.MAX_VALUE;

        String message() default "Out of range";
        Class<?>[] groups() default {};
        Class<? extends Payload>[] payload() default {};
    }

    @ValidStoreId
    @Target({ElementType.FIELD, ElementType.PARAMETER})
    @Retention(RetentionPolicy.RUNTIME)
    @Constraint(validatedBy = {})
    public @interface ValidStoreIdNested {
        String message() default "Invalid store ID (nested)";
        Class<?>[] groups() default {};
        Class<? extends Payload>[] payload() default {};
    }

    static class TestStoreDto {
        @Min(0)
        @Max(999)
        @NotNull
        private Short storeId;

        @ValidStoreId
        @NotNull
        private Short metaStoreId;

        @ValidName
        private String name;

        @ValidEmail
        private String email;

        @ValidRange(min = 5, max = 50)
        private Short rangeField;

        @ValidStoreIdNested
        private Short nestedStoreId;

        @ValidStoreId
        @Max(100)
        private Short priorityStoreId;

        public Short getStoreId() { return storeId; }
        public void setStoreId(Short storeId) { this.storeId = storeId; }
        public Short getMetaStoreId() { return metaStoreId; }
        public void setMetaStoreId(Short metaStoreId) { this.metaStoreId = metaStoreId; }
        public String getName() { return name; }
        public void setName(String name) { this.name = name; }
        public String getEmail() { return email; }
        public void setEmail(String email) { this.email = email; }
        public Short getRangeField() { return rangeField; }
        public void setRangeField(Short rangeField) { this.rangeField = rangeField; }
        public Short getNestedStoreId() { return nestedStoreId; }
        public void setNestedStoreId(Short nestedStoreId) { this.nestedStoreId = nestedStoreId; }
        public Short getPriorityStoreId() { return priorityStoreId; }
        public void setPriorityStoreId(Short priorityStoreId) { this.priorityStoreId = priorityStoreId; }
    }

    @Test
    public void readsComposedMinMaxConstraintOnDtoField() {
        Map<String, Schema> schemas = ModelConverters.getInstance().readAll(TestStoreDto.class);
        Schema model = schemas.get("TestStoreDto");
        assertNotNull(model, "Model should be resolved");
        Schema meta = (Schema) model.getProperties().get("metaStoreId");
        assertNotNull(meta, "metaStoreId property should exist");
        assertEquals(((IntegerSchema) meta).getMinimum().intValue(), 0);
        assertEquals(((IntegerSchema) meta).getMaximum().intValue(), 999);
    }

    @Test
    public void dtoFieldParityWithDirectAnnotations() {
        Map<String, Schema> schemas = ModelConverters.getInstance().readAll(TestStoreDto.class);
        Schema model = schemas.get("TestStoreDto");
        Schema direct = (Schema) model.getProperties().get("storeId");
        Schema meta = (Schema) model.getProperties().get("metaStoreId");
        assertEquals(meta.getMinimum(), direct.getMinimum(), "minimum should match direct annotation");
        assertEquals(meta.getMaximum(), direct.getMaximum(), "maximum should match direct annotation");
    }

    @Test
    public void readsComposedSizeConstraintOnDtoField() {
        Map<String, Schema> schemas = ModelConverters.getInstance().readAll(TestStoreDto.class);
        Schema model = schemas.get("TestStoreDto");
        Schema name = (Schema) model.getProperties().get("name");
        assertNotNull(name, "name property should exist");
        assertEquals((int) name.getMinLength(), 1);
        assertEquals((int) name.getMaxLength(), 50);
    }

    @Test
    public void readsComposedPatternConstraintOnDtoField() {
        Map<String, Schema> schemas = ModelConverters.getInstance().readAll(TestStoreDto.class);
        Schema model = schemas.get("TestStoreDto");
        Schema email = (Schema) model.getProperties().get("email");
        assertNotNull(email, "email property should exist");
        assertNotNull(email.getPattern(), "pattern should be set");
    }

    @Test
    public void readsDeepNestedComposedConstraintOnDtoField() {
        Map<String, Schema> schemas = ModelConverters.getInstance().readAll(TestStoreDto.class);
        Schema model = schemas.get("TestStoreDto");
        Schema nested = (Schema) model.getProperties().get("nestedStoreId");
        assertNotNull(nested, "nestedStoreId property should exist");
        assertEquals(((IntegerSchema) nested).getMinimum().intValue(), 0);
        assertEquals(((IntegerSchema) nested).getMaximum().intValue(), 999);
    }

    @Test
    public void directAnnotationTakesPriorityOverMetaAnnotation() {
        Map<String, Schema> schemas = ModelConverters.getInstance().readAll(TestStoreDto.class);
        Schema model = schemas.get("TestStoreDto");
        Schema priority = (Schema) model.getProperties().get("priorityStoreId");
        assertNotNull(priority, "priorityStoreId property should exist");
        assertEquals(((IntegerSchema) priority).getMinimum().intValue(), 0, "min from @ValidStoreId meta should be set");
        assertEquals(((IntegerSchema) priority).getMaximum().intValue(), 100, "direct @Max(100) should win over @ValidStoreId's @Max(999)");
    }

    /**
     * Documents a known limitation: for @Range-style constraints that rely on @OverridesAttribute
     * to propagate per-use values (e.g. @ValidRange(min=5, max=50)) into their meta-annotations
     * (@Min/@Max), our implementation reads constraints from the annotation *type definition*
     * and therefore always sees the default values (min=0, max=Long.MAX_VALUE), not the
     * caller-supplied ones. Handling @OverridesAttribute is not yet supported.
     */
    @Test
    public void rangeStyleConstraintUsesDefaultsNotOverriddenValues() {
        Map<String, Schema> schemas = ModelConverters.getInstance().readAll(TestStoreDto.class);
        Schema model = schemas.get("TestStoreDto");
        Schema range = (Schema) model.getProperties().get("rangeField");
        assertNotNull(range, "rangeField property should exist");
        // We pick up the *default* values from @Min(0) and @Max(Long.MAX_VALUE) on the type
        // definition of @ValidRange, NOT the caller-supplied @ValidRange(min=5, max=50).
        assertEquals(range.getMinimum().longValue(), 0L,
                "expected default @Min(0) from type definition, not overridden min=5");
        assertEquals(range.getMaximum().longValue(), Long.MAX_VALUE,
                "expected default @Max(Long.MAX_VALUE) from type definition, not overridden max=50");
    }
}

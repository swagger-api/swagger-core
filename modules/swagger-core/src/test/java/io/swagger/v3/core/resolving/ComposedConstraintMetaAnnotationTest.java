package io.swagger.v3.core.resolving;

import io.swagger.v3.core.converter.ModelConverters;
import io.swagger.v3.oas.models.media.IntegerSchema;
import io.swagger.v3.oas.models.media.Schema;
import org.testng.annotations.Test;

import javax.validation.Constraint;
import javax.validation.Payload;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
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
    @Target({ElementType.FIELD, ElementType.PARAMETER})
    @Retention(RetentionPolicy.RUNTIME)
    @Constraint(validatedBy = {})
    public @interface ValidStoreId {
        String message() default "Invalid store ID";
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

        public Short getStoreId() {
            return storeId;
        }

        public void setStoreId(Short storeId) {
            this.storeId = storeId;
        }

        public Short getMetaStoreId() {
            return metaStoreId;
        }

        public void setMetaStoreId(Short metaStoreId) {
            this.metaStoreId = metaStoreId;
        }
    }

    @Test
    public void readsComposedConstraintOnDtoField() {
        Map<String, Schema> schemas = ModelConverters.getInstance().readAll(TestStoreDto.class);
        Schema model = schemas.get("TestStoreDto");
        assertNotNull(model, "Model should be resolved");
        Schema meta = (Schema) model.getProperties().get("metaStoreId");
        assertNotNull(meta, "metaStoreId property should exist");
        // Should carry over min/max from composed constraint
        assertEquals(((IntegerSchema) meta).getMinimum().intValue(), 0);
        assertEquals(((IntegerSchema) meta).getMaximum().intValue(), 999);
    }
}


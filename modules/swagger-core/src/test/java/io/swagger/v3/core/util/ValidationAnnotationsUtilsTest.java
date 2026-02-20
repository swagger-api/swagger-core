package io.swagger.v3.core.util;

import io.swagger.v3.oas.models.media.ArraySchema;
import io.swagger.v3.oas.models.media.IntegerSchema;
import io.swagger.v3.oas.models.media.NumberSchema;
import io.swagger.v3.oas.models.media.ObjectSchema;
import io.swagger.v3.oas.models.media.Schema;
import io.swagger.v3.oas.models.media.StringSchema;
import org.testng.annotations.Test;

import javax.validation.constraints.*;
import java.math.BigDecimal;

import static org.testng.Assert.*;

public class ValidationAnnotationsUtilsTest {

    private Size createSizeAnnotation(final int min, final int max) {
        return new Size() {
            @Override
            public Class<?>[] groups() {
                return new Class[0];
            }

            @Override
            public String message() {
                return "";
            }

            @Override
            public Class<? extends javax.validation.Payload>[] payload() {
                return new Class[0];
            }

            @Override
            public int min() {
                return min;
            }

            @Override
            public int max() {
                return max;
            }

            @Override
            public Class<? extends java.lang.annotation.Annotation> annotationType() {
                return Size.class;
            }
        };
    }

    private Min createMinAnnotation(final long value) {
        return new Min() {
            @Override
            public Class<?>[] groups() {
                return new Class[0];
            }

            @Override
            public String message() {
                return "";
            }

            @Override
            public Class<? extends javax.validation.Payload>[] payload() {
                return new Class[0];
            }

            @Override
            public long value() {
                return value;
            }

            @Override
            public Class<? extends java.lang.annotation.Annotation> annotationType() {
                return Min.class;
            }
        };
    }

    private Max createMaxAnnotation(final long value) {
        return new Max() {
            @Override
            public Class<?>[] groups() {
                return new Class[0];
            }

            @Override
            public String message() {
                return "";
            }

            @Override
            public Class<? extends javax.validation.Payload>[] payload() {
                return new Class[0];
            }

            @Override
            public long value() {
                return value;
            }

            @Override
            public Class<? extends java.lang.annotation.Annotation> annotationType() {
                return Max.class;
            }
        };
    }

    private DecimalMin createDecimalMinAnnotation(final String value, final boolean inclusive) {
        return new DecimalMin() {
            @Override
            public Class<?>[] groups() {
                return new Class[0];
            }

            @Override
            public String message() {
                return "";
            }

            @Override
            public Class<? extends javax.validation.Payload>[] payload() {
                return new Class[0];
            }

            @Override
            public String value() {
                return value;
            }

            @Override
            public boolean inclusive() {
                return inclusive;
            }

            @Override
            public Class<? extends java.lang.annotation.Annotation> annotationType() {
                return DecimalMin.class;
            }
        };
    }

    private DecimalMax createDecimalMaxAnnotation(final String value, final boolean inclusive) {
        return new DecimalMax() {
            @Override
            public Class<?>[] groups() {
                return new Class[0];
            }

            @Override
            public String message() {
                return "";
            }

            @Override
            public Class<? extends javax.validation.Payload>[] payload() {
                return new Class[0];
            }

            @Override
            public String value() {
                return value;
            }

            @Override
            public boolean inclusive() {
                return inclusive;
            }

            @Override
            public Class<? extends java.lang.annotation.Annotation> annotationType() {
                return DecimalMax.class;
            }
        };
    }

    private Pattern createPatternAnnotation(final String regexp) {
        return new Pattern() {
            @Override
            public Class<?>[] groups() {
                return new Class[0];
            }

            @Override
            public String message() {
                return "";
            }

            @Override
            public Class<? extends javax.validation.Payload>[] payload() {
                return new Class[0];
            }

            @Override
            public String regexp() {
                return regexp;
            }

            @Override
            public Pattern.Flag[] flags() {
                return new Pattern.Flag[0];
            }

            @Override
            public Class<? extends java.lang.annotation.Annotation> annotationType() {
                return Pattern.class;
            }
        };
    }

    private Email createEmailAnnotation() {
        return new Email() {
            @Override
            public Class<?>[] groups() {
                return new Class[0];
            }

            @Override
            public String message() {
                return "";
            }

            @Override
            public Class<? extends javax.validation.Payload>[] payload() {
                return new Class[0];
            }

            @Override
            public String regexp() {
                return "";
            }

            @Override
            public Pattern.Flag[] flags() {
                return new Pattern.Flag[0];
            }

            @Override
            public Class<? extends java.lang.annotation.Annotation> annotationType() {
                return Email.class;
            }
        };
    }


    @Test
    public void testApplyNotEmptyConstraintOnArraySchema() {
        Schema schema = new ArraySchema();
        boolean modified = ValidationAnnotationsUtils.applyNotEmptyConstraint(schema, null, null);
        
        assertTrue(modified);
        assertEquals(schema.getMinItems(), Integer.valueOf(1));
    }


    @Test
    public void testApplyNotEmptyConstraintOnStringSchema() {
        Schema schema = new StringSchema();
        boolean modified = ValidationAnnotationsUtils.applyNotEmptyConstraint(schema, null, null);
        
        assertTrue(modified);
        assertEquals(schema.getMinLength(), Integer.valueOf(1));
    }


    @Test
    public void testApplyNotEmptyConstraintOnObjectSchema() {
        Schema schema = new ObjectSchema();
        boolean modified = ValidationAnnotationsUtils.applyNotEmptyConstraint(schema, null, null);
        
        assertTrue(modified);
        assertEquals(schema.getMinProperties(), Integer.valueOf(1));
    }

    @Test
    public void testApplyNotEmptyConstraintOnNumberSchema() {
        Schema schema = new NumberSchema();
        boolean modified = ValidationAnnotationsUtils.applyNotEmptyConstraint(schema, null, null);
        
        assertFalse(modified);
        assertNull(schema.getMinProperties());
        assertNull(schema.getMinLength());
        assertNull(schema.getMinItems());
    }


    @Test
    public void testApplyNotBlankConstraintOnStringSchema() {
        Schema schema = new StringSchema();
        boolean modified = ValidationAnnotationsUtils.applyNotBlankConstraint(schema, null);
        
        assertTrue(modified);
        assertEquals(schema.getMinLength(), Integer.valueOf(1));
    }

    @Test
    public void testApplyNotBlankConstraintOnNonStringSchema() {
        Schema schema = new NumberSchema();
        boolean modified = ValidationAnnotationsUtils.applyNotBlankConstraint(schema, null);
        
        assertFalse(modified);
        assertNull(schema.getMinLength());
    }


    @Test
    public void testApplyMinConstraintOnNumberSchema() {
        Schema schema = new NumberSchema();
        Min minAnnotation = createMinAnnotation(10);
        
        boolean modified = ValidationAnnotationsUtils.applyMinConstraint(schema, minAnnotation);
        
        assertTrue(modified);
        assertEquals(schema.getMinimum(), new BigDecimal(10));
    }

    @Test
    public void testApplyMinConstraintOnIntegerSchema() {
        Schema schema = new IntegerSchema();
        Min minAnnotation = createMinAnnotation(5);
        
        boolean modified = ValidationAnnotationsUtils.applyMinConstraint(schema, minAnnotation);
        
        assertTrue(modified);
        assertEquals(schema.getMinimum(), new BigDecimal(5));
    }

    @Test
    public void testApplyMinConstraintOnStringSchema() {
        Schema schema = new StringSchema();
        Min minAnnotation = createMinAnnotation(10);
        
        boolean modified = ValidationAnnotationsUtils.applyMinConstraint(schema, minAnnotation);
        
        assertFalse(modified);
        assertNull(schema.getMinimum());
    }


    @Test
    public void testApplyMaxConstraintOnNumberSchema() {
        Schema schema = new NumberSchema();
        Max maxAnnotation = createMaxAnnotation(100);
        
        boolean modified = ValidationAnnotationsUtils.applyMaxConstraint(schema, maxAnnotation);
        
        assertTrue(modified);
        assertEquals(schema.getMaximum(), new BigDecimal(100));
    }

    @Test
    public void testApplyMaxConstraintOnIntegerSchema() {
        Schema schema = new IntegerSchema();
        Max maxAnnotation = createMaxAnnotation(50);
        
        boolean modified = ValidationAnnotationsUtils.applyMaxConstraint(schema, maxAnnotation);
        
        assertTrue(modified);
        assertEquals(schema.getMaximum(), new BigDecimal(50));
    }

    @Test
    public void testApplyMaxConstraintOnStringSchema() {
        Schema schema = new StringSchema();
        Max maxAnnotation = createMaxAnnotation(100);
        
        boolean modified = ValidationAnnotationsUtils.applyMaxConstraint(schema, maxAnnotation);
        
        assertFalse(modified);
        assertNull(schema.getMaximum());
    }

    @Test
    public void testApplySizeConstraintOnNumberSchemaWithCustomValues() {
        Schema schema = new NumberSchema();
        Size sizeAnnotation = createSizeAnnotation(10, 100);
        
        boolean modified = ValidationAnnotationsUtils.applySizeConstraint(schema, sizeAnnotation);
        
        assertTrue(modified);
        assertEquals(schema.getMinimum(), new BigDecimal(10));
        assertEquals(schema.getMaximum(), new BigDecimal(100));
    }


    @Test
    public void testApplySizeConstraintOnStringSchemaWithCustomValues() {
        Schema schema = new StringSchema();
        Size sizeAnnotation = createSizeAnnotation(5, 50);
        
        boolean modified = ValidationAnnotationsUtils.applySizeConstraint(schema, sizeAnnotation);
        
        assertTrue(modified);
        assertEquals(schema.getMinLength(), Integer.valueOf(5));
        assertEquals(schema.getMaxLength(), Integer.valueOf(50));
    }

    @Test
    public void testApplySizeConstraintOnArraySchemaWithCustomValues() {
        Schema schema = new ArraySchema();
        Size sizeAnnotation = createSizeAnnotation(1, 10);
        
        boolean modified = ValidationAnnotationsUtils.applySizeConstraint(schema, sizeAnnotation);
        
        assertTrue(modified);
        assertEquals(schema.getMinItems(), Integer.valueOf(1));
        assertEquals(schema.getMaxItems(), Integer.valueOf(10));
    }

    @Test
    public void testApplySizeConstraintOnObjectSchema() {
        Schema schema = new ObjectSchema();
        Size sizeAnnotation = createSizeAnnotation(1, 10);
        
        boolean modified = ValidationAnnotationsUtils.applySizeConstraint(schema, sizeAnnotation);
        
        assertFalse(modified);
    }


    @Test
    public void testApplyDecimalMinConstraintOnNumberSchemaInclusive() {
        Schema schema = new NumberSchema();
        DecimalMin minAnnotation = createDecimalMinAnnotation("10.5", true);
        
        boolean modified = ValidationAnnotationsUtils.applyDecimalMinConstraint(schema, minAnnotation);
        
        assertTrue(modified);
        assertEquals(schema.getMinimum(), new BigDecimal("10.5"));
        assertFalse(schema.getExclusiveMinimum());
    }

    @Test
    public void testApplyDecimalMinConstraintOnNumberSchemaExclusive() {
        Schema schema = new NumberSchema();
        DecimalMin minAnnotation = createDecimalMinAnnotation("10.5", false);
        
        boolean modified = ValidationAnnotationsUtils.applyDecimalMinConstraint(schema, minAnnotation);
        
        assertTrue(modified);
        assertEquals(schema.getMinimum(), new BigDecimal("10.5"));
        assertTrue(schema.getExclusiveMinimum());
    }

    @Test
    public void testApplyDecimalMinConstraintOnStringSchema() {
        Schema schema = new StringSchema();
        DecimalMin minAnnotation = createDecimalMinAnnotation("10.5", true);
        
        boolean modified = ValidationAnnotationsUtils.applyDecimalMinConstraint(schema, minAnnotation);
        
        assertFalse(modified);
        assertNull(schema.getMinimum());
    }


    @Test
    public void testApplyDecimalMaxConstraintOnNumberSchemaInclusive() {
        Schema schema = new NumberSchema();
        DecimalMax maxAnnotation = createDecimalMaxAnnotation("100.5", true);
        
        boolean modified = ValidationAnnotationsUtils.applyDecimalMaxConstraint(schema, maxAnnotation);
        
        assertTrue(modified);
        assertEquals(schema.getMaximum(), new BigDecimal("100.5"));
        assertFalse(schema.getExclusiveMaximum());
    }

    @Test
    public void testApplyDecimalMaxConstraintOnNumberSchemaExclusive() {
        Schema schema = new NumberSchema();
        DecimalMax maxAnnotation = createDecimalMaxAnnotation("100.5", false);
        
        boolean modified = ValidationAnnotationsUtils.applyDecimalMaxConstraint(schema, maxAnnotation);
        
        assertTrue(modified);
        assertEquals(schema.getMaximum(), new BigDecimal("100.5"));
        assertTrue(schema.getExclusiveMaximum());
    }

    @Test
    public void testApplyDecimalMaxConstraintOnStringSchema() {
        Schema schema = new StringSchema();
        DecimalMax maxAnnotation = createDecimalMaxAnnotation("100.5", true);
        
        boolean modified = ValidationAnnotationsUtils.applyDecimalMaxConstraint(schema, maxAnnotation);
        
        assertFalse(modified);
        assertNull(schema.getMaximum());
    }


    @Test
    public void testApplyPatternConstraintOnStringSchema() {
        Schema schema = new StringSchema();
        Pattern patternAnnotation = createPatternAnnotation("^[A-Z]+$");
        
        boolean modified = ValidationAnnotationsUtils.applyPatternConstraint(schema, patternAnnotation);
        
        assertTrue(modified);
        assertEquals(schema.getPattern(), "^[A-Z]+$");
    }

    @Test
    public void testApplyPatternConstraintOnArraySchemaWithStringItems() {
        Schema schema = new ArraySchema();
        schema.setItems(new StringSchema());
        Pattern patternAnnotation = createPatternAnnotation("^[0-9]+$");
        
        boolean modified = ValidationAnnotationsUtils.applyPatternConstraint(schema, patternAnnotation);
        
        assertTrue(modified);
        assertNull(schema.getPattern());
        assertEquals(schema.getItems().getPattern(), "^[0-9]+$");
    }

    @Test
    public void testApplyPatternConstraintOnArraySchemaWithNumberItems() {
        Schema schema = new ArraySchema();
        schema.setItems(new NumberSchema());
        Pattern patternAnnotation = createPatternAnnotation("^[0-9]+$");
        
        boolean modified = ValidationAnnotationsUtils.applyPatternConstraint(schema, patternAnnotation);
        
        assertFalse(modified);
        assertNull(schema.getItems().getPattern());
    }

    @Test
    public void testApplyPatternConstraintOnNumberSchema() {
        Schema schema = new NumberSchema();
        Pattern patternAnnotation = createPatternAnnotation("^[0-9]+$");
        
        boolean modified = ValidationAnnotationsUtils.applyPatternConstraint(schema, patternAnnotation);
        
        assertFalse(modified);
        assertNull(schema.getPattern());
    }


    @Test
    public void testApplyEmailConstraintOnStringSchema() {
        Schema schema = new StringSchema();
        Email emailAnnotation = createEmailAnnotation();
        
        boolean modified = ValidationAnnotationsUtils.applyEmailConstraint(schema, emailAnnotation);
        
        assertTrue(modified);
        assertEquals(schema.getFormat(), "email");
    }

    @Test
    public void testApplyEmailConstraintOnArraySchemaWithStringItems() {
        Schema schema = new ArraySchema();
        schema.setItems(new StringSchema());
        Email emailAnnotation = createEmailAnnotation();
        
        boolean modified = ValidationAnnotationsUtils.applyEmailConstraint(schema, emailAnnotation);
        
        assertTrue(modified);
        assertNull(schema.getFormat());
        assertEquals(schema.getItems().getFormat(), "email");
    }

    @Test
    public void testApplyEmailConstraintOnArraySchemaWithNumberItems() {
        Schema schema = new ArraySchema();
        schema.setItems(new NumberSchema());
        Email emailAnnotation = createEmailAnnotation();
        
        boolean modified = ValidationAnnotationsUtils.applyEmailConstraint(schema, emailAnnotation);
        
        assertFalse(modified);
        assertNull(schema.getItems().getFormat());
    }

    @Test
    public void testApplyEmailConstraintOnNumberSchema() {
        Schema schema = new NumberSchema();
        Email emailAnnotation = createEmailAnnotation();
        
        boolean modified = ValidationAnnotationsUtils.applyEmailConstraint(schema, emailAnnotation);
        
        assertFalse(modified);
        assertNull(schema.getFormat());
    }
}

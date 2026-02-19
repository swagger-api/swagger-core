package io.swagger.v3.core.converting;

import io.swagger.v3.core.converter.AnnotatedType;
import io.swagger.v3.core.converter.ModelConverter;
import io.swagger.v3.core.converter.ModelConverterContext;
import io.swagger.v3.core.converter.ModelConverterContextImpl;
import io.swagger.v3.oas.models.media.Schema;
import org.testng.annotations.Test;

import java.util.Iterator;

import static org.testng.Assert.assertEquals;

public class AnnotatedTypeParentCachingTest {

    private static class ParentAwareConverter implements ModelConverter {
        @Override
        public Schema resolve(AnnotatedType type, ModelConverterContext context, Iterator<ModelConverter> chain) {
            if (String.class.equals(type.getType()) && type.isSchemaProperty() && "fizz".equals(type.getPropertyName())) {
                Schema schema = new Schema();
                Schema parent = type.getParent();
                if (parent != null && "ParentA".equals(parent.getName())) {
                    schema.setDeprecated(true);
                } else {
                    schema.setDeprecated(false);
                }
                return schema;
            }
            return null;
        }
    }

    @Test(description = "cache key should differ for same property under different parent schemas")
    public void testParentAwareSchemaPropertyCaching() {
        ModelConverterContextImpl context = new ModelConverterContextImpl(new ParentAwareConverter());

        Schema parentA = new Schema().name("ParentA");
        Schema parentB = new Schema().name("ParentB");

        AnnotatedType typeInParentA = new AnnotatedType(String.class)
                .schemaProperty(true)
                .propertyName("fizz")
                .parent(parentA);

        AnnotatedType typeInParentB = new AnnotatedType(String.class)
                .schemaProperty(true)
                .propertyName("fizz")
                .parent(parentB);

        Schema schemaFromParentA = context.resolve(typeInParentA);
        Schema schemaFromParentB = context.resolve(typeInParentB);

        assertEquals(schemaFromParentA.getDeprecated(), Boolean.TRUE);
        assertEquals(schemaFromParentB.getDeprecated(), Boolean.FALSE);
    }
}

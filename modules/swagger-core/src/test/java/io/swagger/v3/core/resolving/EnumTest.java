package io.swagger.v3.core.resolving;

import com.google.common.base.Functions;
import com.google.common.collect.Collections2;
import io.swagger.v3.core.converter.AnnotatedType;
import io.swagger.v3.core.converter.ModelConverterContextImpl;
import io.swagger.v3.core.jackson.ModelResolver;
import io.swagger.v3.oas.models.media.Schema;
import io.swagger.v3.oas.models.media.StringSchema;
import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertTrue;

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
        assertNotNull(model);
        assertEquals(model.getName(), "Contract");
        assertTrue(model.getProperties().containsKey("type"));
        assertNotNull(model.getProperties().get("type"));
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

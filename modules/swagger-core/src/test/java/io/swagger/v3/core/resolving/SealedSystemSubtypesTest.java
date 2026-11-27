package io.swagger.v3.core.resolving;

import io.swagger.v3.core.converter.AnnotatedType;
import io.swagger.v3.core.converter.ModelConverterContextImpl;
import io.swagger.v3.core.jackson.ModelResolver;
import io.swagger.v3.core.util.Json;
import io.swagger.v3.core.util.Json31;
import io.swagger.v3.core.util.PrimitiveType;
import io.swagger.v3.oas.models.media.Schema;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.util.AbstractSet;
import java.util.Arrays;
import java.util.Collections;
import java.util.EnumMap;
import java.util.EnumSet;
import java.util.Iterator;
import java.util.Map;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertTrue;

/**
 * Subtypes that do not resolve to a schema, such as the package-private JDK implementations of
 * {@link EnumSet} (sealed since JDK 19, with the permitted subclasses discovered by Jackson 3),
 * must be skipped by {@code ModelResolver.resolveSubtypes} instead of causing a NullPointerException.
 */
public class SealedSystemSubtypesTest {

    @DataProvider
    public Object[][] specVersions() {
        return new Object[][]{{false}, {true}};
    }

    @Test(dataProvider = "specVersions")
    public void enumSetPropertyResolvesToArrayOfEnumItems(boolean openapi31) {
        final ModelConverterContextImpl context = newContext(openapi31);

        context.resolve(new AnnotatedType(EnumSetDto.class));

        final Map<String, Schema> models = context.getDefinedModels();
        final Schema<?> dto = models.get("EnumSetDto");
        assertNotNull(dto, "EnumSetDto model should be resolved");

        final Schema<?> extensions = dto.getProperties().get("extensions");
        assertEnumSetSchema(extensions, openapi31);

        final Schema<?> availableSizes = dto.getProperties().get("availableSizes");
        assertNotNull(availableSizes, "availableSizes property should be resolved");
        assertTrue(availableSizes.getAdditionalProperties() instanceof Schema,
                "availableSizes should be a map schema");
        assertEnumSetSchema((Schema<?>) availableSizes.getAdditionalProperties(), openapi31);

        assertFalse(models.containsKey("RegularEnumSet") || models.containsKey("JumboEnumSet"),
                "JDK EnumSet implementations must not be exposed as models: " + models.keySet());
    }

    /**
     * Simulates the sealed {@link EnumSet} situation independently of the JDK running the tests, including
     * JDK 17 where {@code EnumSet} is not sealed: a sealed set whose only permitted subclass is treated as a
     * system type, so it does not resolve to a schema.
     */
    @Test(dataProvider = "specVersions")
    public void unresolvableSealedSubtypeIsSkipped(boolean openapi31) {
        final String hiddenSetPrefix = HiddenSet.class.getName();
        PrimitiveType.systemPrefixes().add(hiddenSetPrefix);
        try {
            final ModelConverterContextImpl context = newContext(openapi31);

            context.resolve(new AnnotatedType(SealedSetDto.class));

            final Map<String, Schema> models = context.getDefinedModels();
            final Schema<?> dto = models.get("SealedSetDto");
            assertNotNull(dto, "SealedSetDto model should be resolved");
            assertNotNull(dto.getProperties().get("values"), "values property should be resolved");
            assertFalse(models.containsKey("HiddenSet"),
                    "an unresolvable subtype must not be defined as a model: " + models.keySet());
        } finally {
            PrimitiveType.systemPrefixes().remove(hiddenSetPrefix);
        }
    }

    private static ModelConverterContextImpl newContext(boolean openapi31) {
        final ModelResolver modelResolver = new ModelResolver(openapi31 ? Json31.mapper() : Json.mapper())
                .openapi31(openapi31);
        return new ModelConverterContextImpl(modelResolver);
    }

    private static void assertEnumSetSchema(Schema<?> schema, boolean openapi31) {
        assertNotNull(schema, "EnumSet schema should be resolved");
        if (openapi31) {
            assertTrue(schema.getTypes() != null && schema.getTypes().contains("array"),
                    "EnumSet should resolve to an array schema");
        } else {
            assertEquals(schema.getType(), "array", "EnumSet should resolve to an array schema");
        }
        assertEquals(schema.getUniqueItems(), Boolean.TRUE, "EnumSet items should be unique");
        assertNotNull(schema.getItems(), "EnumSet should have items");
        assertEquals(schema.getItems().getEnum(), Arrays.asList("A", "B", "C", "D"),
                "EnumSet items should list the enum constants");
    }

    enum Ext { A, B, C, D }

    enum Size { S, M, L }

    static class EnumSetDto {
        public EnumSet<Ext> extensions;
        public EnumMap<Size, EnumSet<Ext>> availableSizes;
    }

    static class SealedSetDto {
        public SealedSet<Ext> values;
    }

    static sealed abstract class SealedSet<E> extends AbstractSet<E> permits HiddenSet {
    }

    static final class HiddenSet<E> extends SealedSet<E> {
        @Override
        public Iterator<E> iterator() {
            return Collections.emptyIterator();
        }

        @Override
        public int size() {
            return 0;
        }
    }
}

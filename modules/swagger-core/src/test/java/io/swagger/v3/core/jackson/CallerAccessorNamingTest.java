package io.swagger.v3.core.jackson;

import tools.jackson.databind.cfg.MapperConfig;
import tools.jackson.databind.introspect.AccessorNamingStrategy;
import tools.jackson.databind.introspect.AnnotatedClass;
import tools.jackson.databind.introspect.AnnotatedField;
import tools.jackson.databind.introspect.AnnotatedMethod;
import tools.jackson.databind.introspect.DefaultAccessorNamingStrategy;
import io.swagger.v3.core.converter.AnnotatedType;
import io.swagger.v3.core.converter.ModelConverterContextImpl;
import io.swagger.v3.core.util.ObjectMapperFactory;
import io.swagger.v3.oas.models.media.Schema;
import org.testng.annotations.Test;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

/**
 * {@code AbstractModelConverter} used to overwrite the {@code AccessorNamingStrategy.Provider} of
 * every mapper handed to it, so a caller could not influence how {@link ModelResolver} derives
 * property names. A strategy the caller configured is honoured by their mapper at serialization
 * time, so discarding it here made the schema's property names diverge from the JSON the mapper
 * actually produces.
 */
public class CallerAccessorNamingTest {

    public static class BooleanStatusBean {

        public boolean isActive() {
            return true;
        }

        public String getName() {
            return null;
        }
    }

    /**
     * A caller-supplied strategy that keeps the {@code is} prefix instead of stripping it, as a
     * representative deviation from the default bean-convention naming.
     */
    public static class KeepIsPrefixProvider extends DefaultAccessorNamingStrategy.Provider {

        @Override
        public AccessorNamingStrategy forPOJO(MapperConfig<?> config, AnnotatedClass targetClass) {
            final AccessorNamingStrategy delegate = super.forPOJO(config, targetClass);
            return new AccessorNamingStrategy() {
                @Override
                public String findNameForIsGetter(AnnotatedMethod method, String name) {
                    return delegate.findNameForIsGetter(method, name) == null ? null : name;
                }

                @Override
                public String findNameForRegularGetter(AnnotatedMethod method, String name) {
                    return delegate.findNameForRegularGetter(method, name);
                }

                @Override
                public String findNameForMutator(AnnotatedMethod method, String name) {
                    return delegate.findNameForMutator(method, name);
                }

                @Override
                public String modifyFieldName(AnnotatedField field, String name) {
                    return delegate.modifyFieldName(field, name);
                }
            };
        }
    }

    @Test
    public void testCallerSuppliedAccessorNamingIsHonored() {
        ModelResolver modelResolver = new ModelResolver(
                ObjectMapperFactory.createJson().rebuild().accessorNaming(new KeepIsPrefixProvider()).build());
        ModelConverterContextImpl context = new ModelConverterContextImpl(modelResolver);

        Schema<?> schema = modelResolver.resolve(new AnnotatedType(BooleanStatusBean.class), context, null);

        assertTrue(schema.getProperties().containsKey("isActive"),
                "expected isActive but got " + schema.getProperties().keySet());
        assertTrue(schema.getProperties().containsKey("name"),
                "expected name but got " + schema.getProperties().keySet());
        assertEquals(schema.getProperties().size(), 2, "unexpected properties: " + schema.getProperties().keySet());
    }

    @Test
    public void testDefaultAccessorNamingStillStripsTheIsPrefix() {
        ModelResolver modelResolver = new ModelResolver(ObjectMapperFactory.createJson());
        ModelConverterContextImpl context = new ModelConverterContextImpl(modelResolver);

        Schema<?> schema = modelResolver.resolve(new AnnotatedType(BooleanStatusBean.class), context, null);

        assertTrue(schema.getProperties().containsKey("active"),
                "expected active but got " + schema.getProperties().keySet());
        assertTrue(schema.getProperties().containsKey("name"),
                "expected name but got " + schema.getProperties().keySet());
        assertEquals(schema.getProperties().size(), 2, "unexpected properties: " + schema.getProperties().keySet());
    }
}

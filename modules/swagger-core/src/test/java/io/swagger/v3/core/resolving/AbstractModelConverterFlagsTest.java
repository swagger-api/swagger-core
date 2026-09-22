package io.swagger.v3.core.resolving;

import io.swagger.v3.core.converter.AnnotatedType;
import io.swagger.v3.core.converter.ModelConverterContextImpl;
import io.swagger.v3.core.jackson.ModelResolver;
import io.swagger.v3.oas.models.media.Schema;
import org.testng.annotations.Test;
import tools.jackson.databind.ObjectMapper;
import tools.jackson.databind.MapperFeature;
import tools.jackson.databind.cfg.MapperConfig;
import tools.jackson.databind.introspect.AccessorNamingStrategy;
import tools.jackson.databind.introspect.AnnotatedClass;
import tools.jackson.databind.introspect.DefaultAccessorNamingStrategy;

import java.util.List;

import static org.testng.Assert.*;

public class AbstractModelConverterFlagsTest {

    @Test(description = "ModelResolver must preserve the caller's enabled alphabetical ordering policy")
    public void testEnabledCallerPropertyOrderPolicyIsPreserved() {
        ObjectMapper mapper = new ObjectMapper().rebuild()
                .enable(MapperFeature.SORT_PROPERTIES_ALPHABETICALLY)
                .build();
        InspectableModelResolver resolver = new InspectableModelResolver(mapper);
        ModelConverterContextImpl context = new ModelConverterContextImpl(List.of(resolver));
        Schema<?> schema = context.resolve(new AnnotatedType(OrderedBean.class));
        assertNotNull(schema);
        var keys = schema.getProperties().keySet().stream().toList();
        assertTrue(keys.indexOf("apple") < keys.indexOf("zebra"),
                "properties must follow the caller's alphabetical policy; got: " + keys);
        assertTrue(resolver.mapper().isEnabled(MapperFeature.SORT_PROPERTIES_ALPHABETICALLY));
    }

    @Test(description = "ModelResolver must preserve the caller's disabled alphabetical ordering policy")
    public void testDisabledCallerPropertyOrderPolicyIsPreserved() {
        ObjectMapper mapper = new ObjectMapper().rebuild()
                .disable(MapperFeature.SORT_PROPERTIES_ALPHABETICALLY)
                .build();
        InspectableModelResolver resolver = new InspectableModelResolver(mapper);
        ModelConverterContextImpl context = new ModelConverterContextImpl(List.of(resolver));
        Schema<?> schema = context.resolve(new AnnotatedType(OrderedBean.class));
        assertNotNull(schema);
        var keys = schema.getProperties().keySet().stream().toList();
        assertTrue(keys.indexOf("zebra") < keys.indexOf("apple"),
                "properties must follow the caller's declaration-order policy; got: " + keys);
        assertFalse(resolver.mapper().isEnabled(MapperFeature.SORT_PROPERTIES_ALPHABETICALLY));
    }

    @Test(description = "custom accessor naming must discover a getter without the get prefix")
    public void testCustomAccessorNamingPreserved() {
        AccessorNamingStrategy.Provider custom = new DefaultAccessorNamingStrategy.Provider() {
            @Override
            public AccessorNamingStrategy forPOJO(MapperConfig<?> config, AnnotatedClass valueClass) {
                return new DefaultAccessorNamingStrategy.Provider()
                        .withGetterPrefix("")
                        .forPOJO(config, valueClass);
            }
        };
        ObjectMapper mapper = new ObjectMapper().rebuild()
                .accessorNaming(custom)
                .build();
        InspectableModelResolver resolver = new InspectableModelResolver(mapper);
        ModelConverterContextImpl context = new ModelConverterContextImpl(List.of(resolver));
        Schema<?> schema = context.resolve(new AnnotatedType(PrefixlessGetterBean.class));
        assertNotNull(schema, "schema should resolve with custom accessor naming");
        assertTrue(schema.getProperties().containsKey("bare"),
                "custom accessor naming must discover bare() as a property");
        assertSame(resolver.mapper().serializationConfig().getAccessorNaming(), custom,
                "ModelResolver must keep the exact caller-supplied accessor naming provider");
    }

    static class InspectableModelResolver extends ModelResolver {
        InspectableModelResolver(ObjectMapper mapper) {
            super(mapper);
        }

        ObjectMapper mapper() {
            return _mapper;
        }
    }

    static class OrderedBean {
        private String zebra;
        private String apple;

        public String getZebra() { return zebra; }
        public void setZebra(String zebra) { this.zebra = zebra; }
        public String getApple() { return apple; }
        public void setApple(String apple) { this.apple = apple; }
    }

    static class PrefixlessGetterBean {
        private String value;

        public String bare() { return value; }
    }

}

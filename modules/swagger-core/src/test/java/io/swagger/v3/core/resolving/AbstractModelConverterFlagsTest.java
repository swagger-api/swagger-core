package io.swagger.v3.core.resolving;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.core.converter.AnnotatedType;
import io.swagger.v3.core.converter.ModelConverterContextImpl;
import io.swagger.v3.core.jackson.ModelResolver;
import io.swagger.v3.oas.models.media.Schema;
import org.testng.annotations.Test;
import tools.jackson.databind.ObjectMapper;
import tools.jackson.databind.MapperFeature;
import tools.jackson.databind.introspect.AccessorNamingStrategy;
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

    @Test(description = "custom accessor naming must not be overwritten — fields without get prefix still discovered")
    public void testCustomAccessorNamingPreserved() {
        AccessorNamingStrategy.Provider custom = new DefaultAccessorNamingStrategy.Provider() {
        };
        ObjectMapper mapper = new ObjectMapper().rebuild()
                .accessorNaming(custom)
                .build();
        InspectableModelResolver resolver = new InspectableModelResolver(mapper);
        ModelConverterContextImpl context = new ModelConverterContextImpl(List.of(resolver));
        Schema<?> schema = context.resolve(new AnnotatedType(OrderedBean.class));
        assertNotNull(schema, "schema should resolve even with custom accessor naming");
        assertSame(resolver.mapper().serializationConfig().getAccessorNaming(), custom,
                "ModelResolver must keep the exact caller-supplied accessor naming provider");
    }

    @Test(description = "the default resolver naming policy must keep Swagger $ref accessors without restoring Jackson 2 acronym mangling")
    public void testSwaggerRefAndJackson3AcronymNamesAreBothPreserved() throws Exception {
        InspectableModelResolver resolver = new InspectableModelResolver(new ObjectMapper());
        ModelConverterContextImpl context = new ModelConverterContextImpl(List.of(resolver));
        Schema<?> schema = context.resolve(new AnnotatedType(AccessorBean.class));
        var serialized = resolver.mapper().readTree(resolver.mapper().writeValueAsString(new AccessorBean()));

        assertNotNull(schema);
        assertNotNull(schema.getProperties());
        assertTrue(serialized.has("$ref"), "the resolver's rebuilt mapper must keep get$ref() visible");
        assertTrue(schema.getProperties().containsKey("URL"), "getURL() must use the Jackson 3 acronym name");
        assertFalse(schema.getProperties().containsKey("url"), "Jackson 2 acronym mangling must not be restored");
        assertTrue(schema.getProperties().containsKey("explicitName"), "@JsonProperty must win in schema naming");
        assertFalse(schema.getProperties().containsKey("implicitName"), "implicit schema name must be replaced");
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

    static class AccessorBean {
        private String reference;
        private String URL;

        public String get$ref() { return reference; }
        public void set$ref(String reference) { this.reference = reference; }
        public String getURL() { return URL; }
        public void setURL(String URL) { this.URL = URL; }
        @JsonProperty("explicitName")
        public String getImplicitName() { return "explicit"; }
    }
}

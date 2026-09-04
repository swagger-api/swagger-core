package io.swagger.v3.core.resolving;

import io.swagger.v3.core.converter.AnnotatedType;
import io.swagger.v3.core.converter.ModelConverterContextImpl;
import io.swagger.v3.core.jackson.ModelResolver;
import io.swagger.v3.oas.models.media.Schema;
import org.testng.annotations.Test;
import tools.jackson.databind.ObjectMapper;
import tools.jackson.databind.introspect.AccessorNamingStrategy;
import tools.jackson.databind.introspect.DefaultAccessorNamingStrategy;

import java.util.List;

import static org.testng.Assert.*;

public class AbstractModelConverterFlagsTest {

    @Test(description = "ModelResolver(new ObjectMapper()) must not sort properties alphabetically")
    public void testPropertyOrderPreservedWithRawObjectMapper() {
        ModelResolver resolver = new ModelResolver(new ObjectMapper());
        ModelConverterContextImpl context = new ModelConverterContextImpl(List.of(resolver));
        Schema<?> schema = context.resolve(new AnnotatedType(OrderedBean.class));
        assertNotNull(schema);
        var keys = schema.getProperties().keySet().stream().toList();
        assertTrue(keys.indexOf("zebra") < keys.indexOf("apple"),
                "properties must follow declaration order, not alphabetical; got: " + keys);
    }

    @Test(description = "custom accessor naming must not be overwritten — fields without get prefix still discovered")
    public void testCustomAccessorNamingPreserved() {
        AccessorNamingStrategy.Provider custom = new DefaultAccessorNamingStrategy.Provider() {
        };
        ObjectMapper mapper = new ObjectMapper().rebuild()
                .accessorNaming(custom)
                .build();
        ModelResolver resolver = new ModelResolver(mapper);
        ModelConverterContextImpl context = new ModelConverterContextImpl(List.of(resolver));
        Schema<?> schema = context.resolve(new AnnotatedType(OrderedBean.class));
        assertNotNull(schema, "schema should resolve even with custom accessor naming");
    }

    static class OrderedBean {
        private String zebra;
        private String apple;

        public String getZebra() { return zebra; }
        public void setZebra(String zebra) { this.zebra = zebra; }
        public String getApple() { return apple; }
        public void setApple(String apple) { this.apple = apple; }
    }
}

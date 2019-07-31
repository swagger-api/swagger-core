package io.swagger.v3.core.resolving;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.v3.core.converter.AnnotatedType;
import io.swagger.v3.core.converter.ModelConverterContextImpl;
import io.swagger.v3.core.jackson.ModelResolver;
import io.swagger.v3.core.matchers.SerializationMatchers;
import io.swagger.v3.oas.models.media.Schema;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import static org.testng.Assert.assertNotNull;

public class Ticket3030Test extends SwaggerTestBase {

    private ModelResolver modelResolver;
    private ModelConverterContextImpl context;

    @BeforeTest
    public void setup() {
        modelResolver = new ModelResolver(new ObjectMapper());
        context = new ModelConverterContextImpl(modelResolver);
    }



    @Test
    public void testTicket3030() throws Exception {
        final Schema model = context.resolve(new AnnotatedType(Child.class));
        assertNotNull(model);
        String yaml = "Child:\n" +
                "  type: object\n" +
                "  allOf:\n" +
                "  - $ref: '#/components/schemas/Parent'\n" +
                "  - type: object\n" +
                "    properties:\n" +
                "      property:\n" +
                "        type: string\n" +
                "Parent:\n" +
                "  type: object\n" +
                "  properties:\n" +
                "    sharedProperty:\n" +
                "      type: string";

        SerializationMatchers.assertEqualsToYaml(context.getDefinedModels(), yaml);
    }

    @io.swagger.v3.oas.annotations.media.Schema(subTypes = {Child.class})
    static class Parent {
        public String sharedProperty;
    }

    @io.swagger.v3.oas.annotations.media.Schema(allOf = Parent.class)
    static class Child extends Parent {
        public String property;
    }
}

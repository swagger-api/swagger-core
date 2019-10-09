package io.swagger.v3.core.resolving;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import io.swagger.v3.core.converter.AnnotatedType;
import io.swagger.v3.core.converter.ModelConverterContextImpl;
import io.swagger.v3.core.jackson.ModelResolver;
import io.swagger.v3.core.matchers.SerializationMatchers;
import io.swagger.v3.oas.models.media.Schema;

import static org.testng.Assert.assertNotNull;

public class Ticket2189Test extends SwaggerTestBase {

    private ModelResolver modelResolver;
    private ModelConverterContextImpl context;

    @BeforeTest
    public void setup() {
        modelResolver = new ModelResolver(new ObjectMapper());
        context = new ModelConverterContextImpl(modelResolver);
    }

    @Test
    public void testTicket2189() {
        final Schema model = context.resolve(new AnnotatedType(BaseClass.class));
        assertNotNull(model);
        String yaml = "BaseClass:\n" +
                      "  type: object\n" +
                      "  properties:\n" +
                      "    property:\n" +
                      "      type: string\n" +
                      "    type:\n" +
                      "      type: string\n" +
                      "SubClass:\n" +
                      "  type: object\n" +
                      "  allOf:\n" +
                      "  - $ref: '#/components/schemas/BaseClass'\n" +
                      "  - type: object\n" +
                      "    properties:\n" +
                      "      subClassProperty:\n" +
                      "        type: string";

        SerializationMatchers.assertEqualsToYaml(context.getDefinedModels(), yaml);
    }

    static class SubClass extends BaseClass {
        public String subClassProperty;
    }

    @JsonTypeInfo(use = JsonTypeInfo.Id.NAME)
    @JsonSubTypes({
        @JsonSubTypes.Type(value = SubClass.class, name = "SubClass"),
        @JsonSubTypes.Type(value = BaseClass.class, name = "BaseClass"),
    })
    static class BaseClass {
        public String property;
        public String type;
    }
}

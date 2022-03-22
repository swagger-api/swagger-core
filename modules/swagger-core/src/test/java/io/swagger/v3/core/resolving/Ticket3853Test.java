package io.swagger.v3.core.resolving;

import static org.testng.Assert.assertNotNull;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.v3.core.converter.AnnotatedType;
import io.swagger.v3.core.converter.ModelConverterContextImpl;
import io.swagger.v3.core.jackson.ModelResolver;
import io.swagger.v3.core.jackson.TypeNameResolver;
import io.swagger.v3.core.matchers.SerializationMatchers;
import io.swagger.v3.oas.models.media.Schema;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

public class Ticket3853Test extends SwaggerTestBase {

    private ModelResolver modelResolver;
    private ModelConverterContextImpl context;

    @BeforeTest
    public void setup() {
        modelResolver = new ModelResolver(new ObjectMapper(), new FqnTypeNameResolver());
        context = new ModelConverterContextImpl(modelResolver);
    }

    @Test
    public void testTicket3853() {
        final Schema model = context.resolve(new AnnotatedType(BaseClass.class));

        assertNotNull(model);
        String yaml = "io.swagger.v3.core.resolving.Ticket3853Test$BaseClass:\n" +
                "  type: object\n" +
                "  properties:\n" +
                "    property:\n" +
                "      type: string\n" +
                "    type:\n" +
                "      type: string\n" +
                "io.swagger.v3.core.resolving.Ticket3853Test$SubClass:\n" +
                "  type: object\n" +
                "  allOf:\n" +
                "  - $ref: '#/components/schemas/io.swagger.v3.core.resolving.Ticket3853Test$BaseClass'\n" +
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

    static class FqnTypeNameResolver extends TypeNameResolver {

        @Override
        protected String getNameOfClass(Class<?> cls) {
            return cls.getName();
        }
    }
}

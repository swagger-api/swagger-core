package io.swagger.v3.core.resolving;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonView;
import io.swagger.v3.core.converter.AnnotatedType;
import io.swagger.v3.core.converter.ModelConverterContextImpl;
import io.swagger.v3.core.jackson.ModelResolver;
import io.swagger.v3.core.matchers.SerializationMatchers;
import org.testng.annotations.Test;

import java.lang.annotation.Annotation;

import static com.fasterxml.jackson.annotation.JsonTypeInfo.Id.NAME;

public class Ticket4239Test extends SwaggerTestBase {

    public interface Input {}

    public interface Output {}

    @JsonTypeInfo(use = NAME)
    @JsonSubTypes({
        @JsonSubTypes.Type(value = A1.class),
    })
    public static abstract class A {
        @JsonView(Input.class)
        public String a_in;
        @JsonView(Output.class)
        public String a_out;
    }

    public static class A1 extends A {
        @JsonView(Input.class)
        public String a1_in;
        @JsonView(Output.class)
        public String a1_out;
    }

    private static final JsonView VIEW_OUTPUT = new JsonView() {
        public Class<? extends Annotation> annotationType() {
            return JsonView.class;
        }
        public Class<?>[] value() {
            return new Class[] {Output.class};
        }
    };

    @Test
    public void testJsonValueSchemaAnnotation() {

        final ModelResolver modelResolver = new ModelResolver(mapper());

        ModelConverterContextImpl context = new ModelConverterContextImpl(modelResolver);

        AnnotatedType type = new AnnotatedType(Ticket4239Test.A.class).jsonViewAnnotation(VIEW_OUTPUT);

        context.resolve(type);

        SerializationMatchers.assertEqualsToYaml(context.getDefinedModels(), "A1_Output:\n" +
            "  type: object\n" +
            "  allOf:\n" +
            "  - $ref: '#/components/schemas/A_Output'\n" +
            "  - type: object\n" +
            "    properties:\n" +
            "      a1_out:\n" +
            "        type: string\n" +
            "A_Output:\n" +
            "  type: object\n" +
            "  properties:\n" +
            "    a_out:\n" +
            "      type: string\n");

    }
}

package io.swagger.v3.core.resolving;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.v3.core.converter.AnnotatedType;
import io.swagger.v3.core.converter.ModelConverterContextImpl;
import io.swagger.v3.core.jackson.ModelResolver;
import io.swagger.v3.core.matchers.SerializationMatchers;
import io.swagger.v3.oas.models.media.Schema;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import static org.testng.Assert.assertNotNull;

public class Ticket3365Test extends SwaggerTestBase {

    private ModelResolver modelResolver;
    private ModelConverterContextImpl context;

    @BeforeMethod
    public void beforeMethod() {
        modelResolver = new ModelResolver(new ObjectMapper());
        context = new ModelConverterContextImpl(modelResolver);
    }

    @Test
    public void testTicket3365() {
        Schema model = context.resolve(new AnnotatedType(ExampleWithJson.class));
        assertNotNull(model);
        SerializationMatchers.assertEqualsToYaml(model, "required:\n" +
                "- param1\n" +
                "type: object\n" +
                "properties:\n" +
                "  param1:\n" +
                "    title: Cron formatted invoice schedule\n" +
                "    type: object\n" +
                "    example:\n" +
                "      type: array\n" +
                "      items:\n" +
                "        type: string\n" +
                "        format: byte");

        model = context.resolve(new AnnotatedType(ExampleStartingWithInteger.class));
        assertNotNull(model);
        SerializationMatchers.assertEqualsToYaml(model, "required:\n" +
                "- param1\n" +
                "type: object\n" +
                "properties:\n" +
                "  param1:\n" +
                "    title: Cron formatted invoice schedule\n" +
                "    type: string\n" +
                "    example: 0 0 5 * *\n");
    }

    static class ExampleStartingWithInteger  {
        @io.swagger.v3.oas.annotations.media.Schema(
                required = true,
                title = "Cron formatted invoice schedule",
                example = "0 0 5 * *"
        )
        public String param1;
    }
    static class ExampleWithJson  {
        @io.swagger.v3.oas.annotations.media.Schema(
                required = true,
                title = "Cron formatted invoice schedule",
                example = "{\"type\": \"array\", \"items\": {\"type\" : \"string\", \"format\": \"byte\"}}"
        )
        public Object param1;
    }
}

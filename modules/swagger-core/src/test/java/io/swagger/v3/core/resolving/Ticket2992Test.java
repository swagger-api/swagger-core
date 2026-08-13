package io.swagger.v3.core.resolving;

import io.swagger.v3.core.converter.AnnotatedType;
import io.swagger.v3.core.converter.ModelConverterContextImpl;
import io.swagger.v3.core.jackson.ModelResolver;
import io.swagger.v3.core.matchers.SerializationMatchers;
import io.swagger.v3.core.resolving.resources.TestObject2992;
import io.swagger.v3.core.util.PrimitiveType;
import io.swagger.v3.oas.models.media.Schema;
import org.testng.annotations.Test;

import java.util.Map;

public class Ticket2992Test extends SwaggerTestBase {

    @Test
    public void testLocalTime() throws Exception {

        final ModelResolver modelResolver = new ModelResolver(mapper());

        ModelConverterContextImpl context = new ModelConverterContextImpl(modelResolver);

        Schema model = context
                .resolve(new AnnotatedType(TestObject2992.class));

        SerializationMatchers.assertEqualsToYaml(context.getDefinedModels(), "TestObject2992:\n" +
                "  type: object\n" +
                "  properties:\n" +
                "    name:\n" +
                "      type: string\n" +
                "    a:\n" +
                "      type: string\n" +
                "      format: time-local\n" +
                "    b:\n" +
                "      type: string\n" +
                "      format: time-local\n" +
                "    c:\n" +
                "      type: string\n" +
                "      format: time-local\n" +
                "    d:\n" +
                "      type: string\n" +
                "      format: date-time\n" +
                "    e:\n" +
                "      type: string\n" +
                "      format: date-time\n" +
                "    f:\n" +
                "      type: string\n" +
                "      format: date-time");

        // Save current state so other tests are not affected by the static customClasses map
        final Map<String, PrimitiveType> custom = PrimitiveType.customClasses();
        final PrimitiveType previous = custom.get("java.time.LocalTime");

        PrimitiveType.enablePartialTime();
        try {
            context = new ModelConverterContextImpl(modelResolver);

            context
                    .resolve(new AnnotatedType(TestObject2992.class));

            SerializationMatchers.assertEqualsToYaml(context.getDefinedModels(), "TestObject2992:\n" +
                    "  type: object\n" +
                    "  properties:\n" +
                    "    name:\n" +
                    "      type: string\n" +
                    "    a:\n" +
                    "      type: string\n" +
                    "      format: partial-time\n" +
                    "    b:\n" +
                    "      type: string\n" +
                    "      format: partial-time\n" +
                    "    c:\n" +
                    "      type: string\n" +
                    "      format: partial-time\n" +
                    "    d:\n" +
                    "      type: string\n" +
                    "      format: date-time\n" +
                    "    e:\n" +
                    "      type: string\n" +
                    "      format: date-time\n" +
                    "    f:\n" +
                    "      type: string\n" +
                    "      format: date-time");
        } finally {
            // Restore previous state so subsequent tests are not affected
            if (previous == null) custom.remove("java.time.LocalTime");
            else custom.put("java.time.LocalTime", previous);
        }
    }

}

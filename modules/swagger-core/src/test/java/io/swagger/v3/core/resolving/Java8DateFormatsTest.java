package io.swagger.v3.core.resolving;

import io.swagger.v3.core.converter.AnnotatedType;
import io.swagger.v3.core.converter.ModelConverterContextImpl;
import io.swagger.v3.core.jackson.ModelResolver;
import io.swagger.v3.core.matchers.SerializationMatchers;
import io.swagger.v3.core.resolving.resources.TestObjectJava8Dates;
import io.swagger.v3.core.resolving.resources.TestObject2992;
import io.swagger.v3.core.util.PrimitiveType;
import org.testng.annotations.Test;

import java.util.Map;

/**
 * Verifies Java 8 date/time type → OpenAPI format mappings (issue #5172).
 *
 * Default behaviour (backward-compatible):
 *   OffsetTime  → "time"     (fixed; was incorrectly a complex object)
 *   Duration    → "duration" (fixed; was incorrectly a complex object)
 *   LocalDateTime → "date-time"    (unchanged for compatibility)
 *   LocalTime     → complex object (unchanged; call enableJava8Formats() to opt in)
 *
 * Opt-in via PrimitiveType.enableJava8Formats():
 *   LocalDateTime → "date-time-local"
 *   LocalTime     → "time-local"
 */
public class Java8DateFormatsTest extends SwaggerTestBase {

    @Test
    public void testDefaultFormats() throws Exception {
        final ModelResolver modelResolver = new ModelResolver(mapper());
        final ModelConverterContextImpl context = new ModelConverterContextImpl(modelResolver);

        context.resolve(new AnnotatedType(TestObjectJava8Dates.class));

        SerializationMatchers.assertEqualsToYaml(context.getDefinedModels(), "TestObjectJava8Dates:\n" +
                "  type: object\n" +
                "  properties:\n" +
                "    localDateTime:\n" +
                "      type: string\n" +
                "      format: date-time\n" +
                "    offsetDateTime:\n" +
                "      type: string\n" +
                "      format: date-time\n" +
                "    zonedDateTime:\n" +
                "      type: string\n" +
                "      format: date-time\n" +
                "    instant:\n" +
                "      type: string\n" +
                "      format: date-time\n" +
                "    localDate:\n" +
                "      type: string\n" +
                "      format: date\n" +
                "    offsetTime:\n" +
                "      type: string\n" +
                "      format: time\n" +
                "    duration:\n" +
                "      type: string\n" +
                "      format: duration");
    }

    @Test
    public void testEnableJava8Formats() throws Exception {
        // Save current state so other tests are not affected by the static customClasses map
        final Map<String, PrimitiveType> custom = PrimitiveType.customClasses();
        final PrimitiveType prevLocalDateTime = custom.get("java.time.LocalDateTime");
        final PrimitiveType prevLocalTime     = custom.get("java.time.LocalTime");

        PrimitiveType.enableJava8Formats();
        try {
            final ModelResolver modelResolver = new ModelResolver(mapper());
            final ModelConverterContextImpl context = new ModelConverterContextImpl(modelResolver);

            context.resolve(new AnnotatedType(TestObject2992.class));

            // LocalDateTime → "date-time-local", LocalTime → "time-local" after opt-in
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
                    "      format: date-time-local\n" +
                    "    e:\n" +
                    "      type: string\n" +
                    "      format: date-time-local\n" +
                    "    f:\n" +
                    "      type: string\n" +
                    "      format: date-time-local");
        } finally {
            // Restore previous state so subsequent tests are not affected
            if (prevLocalDateTime == null) custom.remove("java.time.LocalDateTime");
            else custom.put("java.time.LocalDateTime", prevLocalDateTime);
            if (prevLocalTime == null) custom.remove("java.time.LocalTime");
            else custom.put("java.time.LocalTime", prevLocalTime);
        }
    }
}

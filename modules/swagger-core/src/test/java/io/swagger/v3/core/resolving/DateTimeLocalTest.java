package io.swagger.v3.core.resolving;

import io.swagger.v3.core.converter.AnnotatedType;
import io.swagger.v3.core.converter.ModelConverterContextImpl;
import io.swagger.v3.core.jackson.ModelResolver;
import io.swagger.v3.core.matchers.SerializationMatchers;
import org.testng.annotations.Test;

import java.time.LocalDateTime;

public class DateTimeLocalTest extends SwaggerTestBase {
    @Test
    public void testLocalDateTimeDefaultMapping() {
        final ModelResolver modelResolver = new ModelResolver(mapper());
        ModelConverterContextImpl context = new ModelConverterContextImpl(modelResolver);

        context.resolve(new AnnotatedType(TestObjectDateTimeLocal.class));

        SerializationMatchers.assertEqualsToYaml(context.getDefinedModels(), "TestObjectDateTimeLocal:\n" +
                "  type: object\n" +
                "  properties:\n" +
                "    localDateTime:\n" +
                "      type: string\n" +
                "      format: date-time-local");
    }

    static class TestObjectDateTimeLocal {
        private LocalDateTime localDateTime;

        public LocalDateTime getLocalDateTime() {
            return localDateTime;
        }

        public void setLocalDateTime(LocalDateTime localDateTime) {
            this.localDateTime = localDateTime;
        }
    }
}

package io.swagger.v3.core.resolving;

import io.swagger.v3.core.converter.ModelConverters;
import io.swagger.v3.oas.models.media.DurationSchema;
import io.swagger.v3.oas.models.media.Schema;
import org.joda.time.Duration;
import org.testng.annotations.Test;

import java.util.Map;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

public class JodaDurationConverterTest {

    @Test
    public void testJodaDuration() {
        final Map<String, Schema> models = ModelConverters.getInstance().read(ModelWithJodaDuration.class);
        assertEquals(models.size(), 1);

        final Schema model = models.get("ModelWithJodaDuration");

        final Schema durationProperty = (Schema) model.getProperties().get("measuredDuration");
        assertTrue(durationProperty instanceof DurationSchema);
        assertTrue(model.getRequired().contains("measuredDuration"));
        assertEquals(durationProperty.getDescription(), "Time something took to do");

    }

    class ModelWithJodaDuration {

        @io.swagger.v3.oas.annotations.media.Schema(description = "Time something took to do", required = true)
        public Duration measuredDuration;
    }

}

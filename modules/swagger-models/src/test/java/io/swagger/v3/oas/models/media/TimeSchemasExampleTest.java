package io.swagger.v3.oas.models.media;

import org.testng.annotations.Test;

import java.time.Duration;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotEquals;
import static org.testng.Assert.assertNull;

public class TimeSchemasExampleTest {

    @Test
    public void durationExampleIsKeptAsDeclared() {
        assertEquals(new DurationSchema().example("P7D").getExample(), "P7D");
        assertEquals(new DurationSchema().example("PT2H0M").getExample(), "PT2H0M");
    }

    @Test
    public void timeLocalExampleIsKeptAsDeclared() {
        assertEquals(new TimeLocalSchema().example("10:15:00").getExample(), "10:15:00");
    }

    @Test
    public void timeExampleIsKeptAsDeclared() {
        assertEquals(new TimeSchema().example("10:15:00+01:00").getExample(), "10:15:00+01:00");
    }

    @Test
    public void invalidExampleIsStillRejected() {
        assertNull(new DurationSchema().example("not-a-duration").getExample());
        assertNull(new TimeLocalSchema().example("not-a-time").getExample());
        assertNull(new TimeSchema().example("not-a-time").getExample());
    }

    @Test
    public void typedExampleIsReturnedAsIs() {
        Duration duration = Duration.ofDays(7);
        assertEquals(new DurationSchema().example(duration).getExample(), duration);
    }

    @Test
    public void replacingStringExampleWithTypedValueDropsTheString() {
        DurationSchema schema = new DurationSchema();
        schema.setExample("P7D");
        schema.setExample(Duration.ofHours(2));
        assertEquals(schema.getExample(), Duration.ofHours(2));
    }

    @Test
    public void schemasWithDifferentlyWrittenExamplesAreNotEqual() {
        assertNotEquals(new DurationSchema().example("P7D"), new DurationSchema().example("PT168H"));
        assertEquals(new DurationSchema().example("P7D"), new DurationSchema().example("P7D"));
        assertEquals(new DurationSchema().example("P7D").hashCode(), new DurationSchema().example("P7D").hashCode());
    }

}

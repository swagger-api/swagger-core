package io.swagger.v3.core.resolving.resources;

import java.time.Duration;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.OffsetTime;
import java.time.ZonedDateTime;

// LocalTime is intentionally excluded: its default behaviour depends on whether
// enablePartialTime() has been called by a preceding test (shared static state).
// LocalTime opt-in behaviour is covered in Java8DateFormatsTest#testEnableJava8Formats.
public class TestObjectJava8Dates {

    private LocalDateTime localDateTime;
    private OffsetDateTime offsetDateTime;
    private ZonedDateTime zonedDateTime;
    private Instant instant;
    private LocalDate localDate;
    private OffsetTime offsetTime;
    private Duration duration;

    public LocalDateTime getLocalDateTime() { return localDateTime; }
    public void setLocalDateTime(LocalDateTime localDateTime) { this.localDateTime = localDateTime; }

    public OffsetDateTime getOffsetDateTime() { return offsetDateTime; }
    public void setOffsetDateTime(OffsetDateTime offsetDateTime) { this.offsetDateTime = offsetDateTime; }

    public ZonedDateTime getZonedDateTime() { return zonedDateTime; }
    public void setZonedDateTime(ZonedDateTime zonedDateTime) { this.zonedDateTime = zonedDateTime; }

    public Instant getInstant() { return instant; }
    public void setInstant(Instant instant) { this.instant = instant; }

    public LocalDate getLocalDate() { return localDate; }
    public void setLocalDate(LocalDate localDate) { this.localDate = localDate; }

    public OffsetTime getOffsetTime() { return offsetTime; }
    public void setOffsetTime(OffsetTime offsetTime) { this.offsetTime = offsetTime; }

    public Duration getDuration() { return duration; }
    public void setDuration(Duration duration) { this.duration = duration; }
}

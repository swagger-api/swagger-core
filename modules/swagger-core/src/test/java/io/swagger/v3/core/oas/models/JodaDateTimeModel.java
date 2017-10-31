package io.swagger.v3.core.oas.models;

import org.joda.time.DateTime;

public class JodaDateTimeModel {
    private DateTime createdAt;

    public DateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(DateTime createdAt) {
        this.createdAt = createdAt;
    }
}
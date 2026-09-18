package io.swagger.v3.core.util.fixtures;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class AnnotatedCreatorPrecedenceFixture {
    private final String value;

    public AnnotatedCreatorPrecedenceFixture(String implicit) {
        this.value = "implicit:" + implicit;
    }

    @JsonCreator
    public static AnnotatedCreatorPrecedenceFixture create(
            @JsonProperty("explicit") String explicit) {
        return new AnnotatedCreatorPrecedenceFixture("explicit:" + explicit, true);
    }

    private AnnotatedCreatorPrecedenceFixture(String value, boolean unused) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}

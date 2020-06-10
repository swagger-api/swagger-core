package io.swagger.v3.core.resolving.resources;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.HashSet;
import java.util.Set;

@Schema(description = "Thing")
public class MyThing {
    private final Set<MyThing> otherThings;

    public MyThing() {
        otherThings = new HashSet<>();
    }

    @Schema(description = "Other related things")
    public Set<MyThing> getOtherThings() {
        return otherThings;
    }
}


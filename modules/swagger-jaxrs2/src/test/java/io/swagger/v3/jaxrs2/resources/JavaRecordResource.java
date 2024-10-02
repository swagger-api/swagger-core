package io.swagger.v3.jaxrs2.resources;

public record JavaRecordResource(
        @io.swagger.v3.oas.annotations.media.Schema(description = "Testing of Java Record Processing") String test,
        boolean isLatest,
        String id,
        Integer age
) {
}

package io.swagger.v3.core.resolving.resources;

import io.swagger.v3.oas.annotations.media.Schema;

public class TestObjectTicket4247 {

    @Schema(
            oneOf = { String.class, Number.class }
    )
    public Object value;

}

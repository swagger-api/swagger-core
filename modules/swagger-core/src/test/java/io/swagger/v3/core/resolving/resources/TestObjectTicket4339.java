package io.swagger.v3.core.resolving.resources;

import io.swagger.v3.core.oas.models.Address;
import io.swagger.v3.oas.annotations.media.Schema;

import java.math.BigDecimal;
import java.util.List;

public class TestObjectTicket4339 {
    // type: string
    @Schema(nullable = true, example = "null", defaultValue = "null")
    public String string;

    // type: integer
    @Schema(nullable = true, example = "null", defaultValue = "null")
    public Integer integer;

    // type: number
    @Schema(nullable = true, example = "null", defaultValue = "null")
    public BigDecimal number;

    // type: boolean
    @Schema(nullable = true, example = "null", defaultValue = "null")
    public Boolean aBoolean;

    // type: object
    @Schema(nullable = true, example = "null", defaultValue = "null")
    public Address address;

    // type: array
    @Schema(nullable = true, example = "null", defaultValue = "null")
    public List<TestObjectTicket4339> testObjectTicket4339List;
}

package io.swagger.v3.jaxrs2.resources;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import java.math.BigDecimal;

public class Ticket4804CustomClass {

    public static interface ValidationGroup {

    }

    public static interface OtherValidationGroup {

    }

    @NotNull
    public String nonGroupValidatedField;

    @Min(value = 1, groups = ValidationGroup.class)
    public Integer singleGroupValidatedField;

    @DecimalMin(value = "1.0", groups = {ValidationGroup.class, OtherValidationGroup.class})
    public BigDecimal multipleGroupValidatedField;

    @Pattern(regexp = ".*", groups = OtherValidationGroup.class)
    public String otherGroupValidatedField;

    @NotEmpty(groups = ValidationGroup.class)
    public String singleGroupValidatedField2;
}

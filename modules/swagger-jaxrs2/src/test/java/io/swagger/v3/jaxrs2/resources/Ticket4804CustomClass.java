package io.swagger.v3.jaxrs2.resources;

import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
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

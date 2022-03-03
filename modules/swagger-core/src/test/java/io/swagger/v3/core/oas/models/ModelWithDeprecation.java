package io.swagger.v3.core.oas.models;

import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;

public class ModelWithDeprecation {
    public String fullName;
    @Deprecated
    public String lastName;
    private String firstName;
    @Schema(deprecated = true)
    public String middleName;

    @Deprecated
    public String getFirstName() {
        return firstName;
    }
}

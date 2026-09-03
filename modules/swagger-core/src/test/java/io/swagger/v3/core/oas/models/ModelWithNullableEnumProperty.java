package io.swagger.v3.core.oas.models;

import javax.annotation.Nullable;

public class ModelWithNullableEnumProperty {
    private TestEnum e;

    @Nullable
    public TestEnum getEnumValue() {
        return e;
    }

    public void setEnumValue(TestEnum e) {
        this.e = e;
    }
}

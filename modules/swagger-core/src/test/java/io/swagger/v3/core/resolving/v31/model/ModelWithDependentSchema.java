package io.swagger.v3.core.resolving.v31.model;

import io.swagger.v3.oas.annotations.StringToClassMapItem;
import io.swagger.v3.oas.annotations.media.DependentSchema;
import io.swagger.v3.oas.annotations.media.DependentSchemas;
import io.swagger.v3.oas.annotations.media.Schema;

@DependentSchemas(
        value = {
                @DependentSchema(
                        name = "value",
                        schema = @Schema(
                                properties = {
                                        @StringToClassMapItem(
                                                key = "enable",
                                                value = ModelWithDependentSchema.BooleanFakeClass.class
                                        )
                                }
                        )
                )
        }
)
public class ModelWithDependentSchema {

    private String name;
    private int value;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    class BooleanFakeClass {
        private boolean type;

        public boolean isType() {
            return type;
        }

        public void setType(boolean type) {
            this.type = type;
        }
    }
}

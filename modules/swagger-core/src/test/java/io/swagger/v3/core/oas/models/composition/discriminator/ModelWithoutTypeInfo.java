package io.swagger.v3.core.oas.models.composition.discriminator;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

@JsonTypeInfo(use= JsonTypeInfo.Id.NONE)
@JsonSubTypes({
        @JsonSubTypes.Type(value = ModelWithoutTypeInfo.First.class),
        @JsonSubTypes.Type(value = ModelWithoutTypeInfo.Second.class, name = "SecondType"),
})
public class ModelWithoutTypeInfo {

    public static class First extends ModelWithoutTypeInfo {
        private String name;

        public String getName() { return name; }

        public void setName(String name) { this.name = name; }
    }

    public static class Second extends ModelWithoutTypeInfo {
        private String value;

        public String getValue() { return value; }

        public void setValue(String value) { this.value = value; }
    }
}

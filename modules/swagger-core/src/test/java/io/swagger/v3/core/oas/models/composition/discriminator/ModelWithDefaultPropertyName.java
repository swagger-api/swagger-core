package io.swagger.v3.core.oas.models.composition.discriminator;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonTypeName;

@JsonTypeInfo(use= JsonTypeInfo.Id.SIMPLE_NAME, include = JsonTypeInfo.As.PROPERTY)
@JsonSubTypes({
        @JsonSubTypes.Type(value = ModelWithDefaultPropertyName.First.class),
        @JsonSubTypes.Type(value = ModelWithDefaultPropertyName.Second.class, name = "SecondType"),
})
public class ModelWithDefaultPropertyName {

    public static class First extends ModelWithDefaultPropertyName {
        private String name;

        public String getName() { return name; }

        public void setName(String name) { this.name = name; }
    }

    public static class Second extends ModelWithDefaultPropertyName {
        private String value;

        public String getValue() { return value; }

        public void setValue(String value) { this.value = value; }
    }
}

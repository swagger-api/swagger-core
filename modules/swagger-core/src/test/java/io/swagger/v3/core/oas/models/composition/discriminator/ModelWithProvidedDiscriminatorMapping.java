package io.swagger.v3.core.oas.models.composition.discriminator;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import io.swagger.v3.oas.annotations.media.DiscriminatorMapping;
import io.swagger.v3.oas.annotations.media.Schema;

@JsonTypeInfo(use= JsonTypeInfo.Id.SIMPLE_NAME, include = JsonTypeInfo.As.PROPERTY, property="kind")
@JsonSubTypes({
    @JsonSubTypes.Type(value = ModelWithProvidedDiscriminatorMapping.First.class),
    @JsonSubTypes.Type(value = ModelWithProvidedDiscriminatorMapping.Second.class),
})
@Schema(
    discriminatorMapping = {
        @DiscriminatorMapping(schema = ModelWithProvidedDiscriminatorMapping.First.class, value = "FirstOne"),
        @DiscriminatorMapping(schema = ModelWithProvidedDiscriminatorMapping.Second.class, value = "SecondOne"),
    }
)
public class ModelWithProvidedDiscriminatorMapping {

    public static class First extends ModelWithProvidedDiscriminatorMapping {
        private String name;

        public String getName() { return name; }

        public void setName(String name) { this.name = name; }
    }

    public static class Second extends ModelWithProvidedDiscriminatorMapping {
        private String value;

        public String getValue() { return value; }

        public void setValue(String value) { this.value = value; }
    }
}

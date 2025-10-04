package io.swagger.v3.core.oas.models.composition;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

@JsonTypeInfo(use= JsonTypeInfo.Id.SIMPLE_NAME, include = JsonTypeInfo.As.PROPERTY, property="kind")
@JsonSubTypes({
        @JsonSubTypes.Type(value = ModelWithDiscriminatorMapping.First.class),
        @JsonSubTypes.Type(value = ModelWithDiscriminatorMapping.Second.class, name = "second"),
})
public class ModelWithDiscriminatorMapping {

    static class First extends ModelWithDiscriminatorMapping {
        private String name;

        String getName() { return name; }

        void setName(String name) { this.name = name; }
    }

    static class Second extends ModelWithDiscriminatorMapping {
        private String value;

        String getValue() { return value; }

        void setValue(String mountain) { this.value = mountain; }
    }
}

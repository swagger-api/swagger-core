package io.swagger.v3.core.oas.models.composition;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonTypeName;

@JsonTypeInfo(use= JsonTypeInfo.Id.SIMPLE_NAME, include = JsonTypeInfo.As.PROPERTY, property="kind")
@JsonSubTypes({
        @JsonSubTypes.Type(value = ModelWithDiscriminatorMapping.First.class),
        @JsonSubTypes.Type(value = ModelWithDiscriminatorMapping.Second.class, name = "SecondType"),
        @JsonSubTypes.Type(value = ModelWithDiscriminatorMapping.Third.class)
})
public class ModelWithDiscriminatorMapping {

    public static class First extends ModelWithDiscriminatorMapping {
        private String name;

        public String getName() { return name; }

        public void setName(String name) { this.name = name; }
    }

    public static class Second extends ModelWithDiscriminatorMapping {
        private String value;

        public String getValue() { return value; }

        public void setValue(String value) { this.value = value; }
    }

    @JsonTypeName("ThirdType")
    public static class Third extends ModelWithDiscriminatorMapping {
        private String value;

        public String getValue() { return value; }

        public void setValue(String value) { this.value = value; }
    }
}

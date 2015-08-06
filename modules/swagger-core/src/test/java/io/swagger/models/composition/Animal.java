package io.swagger.models.composition;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonSubTypes.Type;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.EXTERNAL_PROPERTY,
        property = "type")
@JsonSubTypes({
        @Type(value = Human.class, name = "human"),
        @Type(value = Pet.class, name = "pet")
})
public interface Animal {
    String getName();

    void setName(String name);

    String getType();

    void setType(String type);
}

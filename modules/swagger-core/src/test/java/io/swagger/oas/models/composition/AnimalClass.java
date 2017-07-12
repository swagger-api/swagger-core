package io.swagger.oas.models.composition;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonSubTypes.Type;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.EXTERNAL_PROPERTY,
        property = "type")
@JsonSubTypes({
        @Type(value = HumanClass.class, name = "human"),
        @Type(value = PetClass.class, name = "pet")
})
public class AnimalClass {

    String type;
    String name;
    String getName() {
        return name;
    }

    void setName(String name) {
        this.name = name;
    }

    String getType(){
        return type;
    }

    void setType(String type){
        this.type = type;
    }
}

package io.swagger.v3.core.resolving.resources;

import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

@Schema(name = "testObject")
public class TestObject2616 {

    private List<AbstractObject> objects;

    public List<AbstractObject> getObjects() {return objects;}

    public void setObjects(List<AbstractObject> objects) {this.objects = objects;}
}

@ArraySchema(schema = @Schema(
        name = "objects",
        oneOf = {
                AObject.class,
                BObject.class
        }
))
abstract class AbstractObject {}

@Schema(name = "AObject")
class AObject extends AbstractObject{
    private String name;

    public String getName() {return name;}

    public void setName(String name) {this.name = name;}
}

@Schema(name = "BObject")
class BObject extends AbstractObject{
    private Integer number;

    public Integer getNumber() {return number;}

    public void setNumber(Integer number) {this.number = number;}
}

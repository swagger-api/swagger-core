package models.composition;

import com.wordnik.swagger.annotations.ApiModel;
import com.wordnik.swagger.annotations.ApiModelProperty;

@ApiModel(description = "Thing3", parent = AbstractBaseModelWithoutFields.class)
public class Thing3 extends AbstractBaseModelWithoutFields {

    @ApiModelProperty (value = "Additional field a") String a;
    @ApiModelProperty (value = "Additional field a") int x;

    public String getA() {
        return a;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public void setA(String a) {
        this.a = a;
    }
}

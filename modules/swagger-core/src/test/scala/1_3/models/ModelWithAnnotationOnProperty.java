package models;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel(description = "my annotated model")
public class ModelWithAnnotationOnProperty {
    @ApiModelProperty(value = "the count of items", position = 1)
    private Integer count;

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }
}

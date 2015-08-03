package io.swagger.models;

import io.swagger.annotations.ApiModelProperty;

import java.util.List;

/**
 * Created by simon00t on 10-6-2015.
 */
public class SubModelWithSelfReference {
    @ApiModelProperty(value = "References")
    public List<SubModelWithSelfReference> references;
}

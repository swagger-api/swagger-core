package io.swagger.models;

import io.swagger.annotations.media.OASSchema;

public class ModelWithBooleanProperty {
    // TODO
    @OASSchema//(allowableValues = "true")
    public Boolean isGreat;

    @OASSchema//(allowableValues = "1,2")
    public Integer intValue;
}

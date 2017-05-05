package io.swagger.models.composition;

import io.swagger.annotations.media.OASSchema;

@OASSchema(description = "I am an Abstract Base Model without any declared fields and with Sub-Types",
        anyOf = {Thing3.class})
public abstract class AbstractBaseModelWithoutFields {
}

package io.swagger.oas.models.composition;

import io.swagger.oas.annotations.media.Schema;

@Schema(description = "I am an Abstract Base Model without any declared fields and with Sub-Types",
        anyOf = {Thing3.class})
public abstract class AbstractBaseModelWithoutFields {
}

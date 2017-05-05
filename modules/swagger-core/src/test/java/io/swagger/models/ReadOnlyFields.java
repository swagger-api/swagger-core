package io.swagger.models;

import io.swagger.annotations.media.OASSchema;

public class ReadOnlyFields {
    @OASSchema(readOnly = true)
    public Long id;
}

package io.swagger.v3.core.jackson.mixin;

import com.fasterxml.jackson.annotation.JsonFormat;

public abstract class DateTimeSchemaMixin {

    @JsonFormat (shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ssXXX")
    public abstract Object getExample();
}

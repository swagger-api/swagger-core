package io.swagger.v3.core.jackson.mixin;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.Map;

public abstract class DateSchemaMixin {

    @JsonFormat (shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    public abstract Object getExample();

    @JsonFormat (shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    public abstract Object getDefault();

    @JsonFormat (shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    public abstract Object getEnum();

    @JsonIgnore
    public abstract Object getJsonSchemaImpl();

    @JsonIgnore
    public abstract Map<String, Object> getJsonSchema();

    @JsonIgnore
    public abstract Boolean getBooleanSchemaValue();
}

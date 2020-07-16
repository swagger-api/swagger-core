package io.swagger.v3.core.jackson.mixin;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.Map;

/*
 TODO: this handles deserialization, but not serialization of "explicit" null value (as we are serializing non null fields)
 to handle it, a custom schema serializer / JsonFilter / BeanSerializerModifier must be added, using non null for all fields except example where the flag would
 be checked to serialize or skip null example field.
 */
public abstract class SchemaMixin {

    @JsonAnyGetter
    public abstract Map<String, Object> getExtensions();

    @JsonAnySetter
    public abstract void addExtension(String name, Object value);

    @JsonIgnore
    public abstract boolean getExampleSetFlag();

    @JsonInclude(JsonInclude.Include.CUSTOM)
    public abstract Object getExample();
}

package io.swagger.v3.core.util;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import io.swagger.v3.oas.models.parameters.Parameter;

import java.io.IOException;

/**
 * Serializes {@link Parameter.StyleEnum} using its wire value, rejecting the
 * OpenAPI 3.2 'cookie' style on mappers targeting earlier spec versions so a
 * document cannot emit what the same mapper would refuse to read back.
 */
public class ParameterStyleSerializer extends StdSerializer<Parameter.StyleEnum> {

    private final boolean allowCookie;

    public ParameterStyleSerializer() {
        this(false);
    }

    public ParameterStyleSerializer(boolean allowCookie) {
        super(Parameter.StyleEnum.class);
        this.allowCookie = allowCookie;
    }

    @Override
    public void serialize(Parameter.StyleEnum value, JsonGenerator gen, SerializerProvider provider)
            throws IOException {
        if (value == Parameter.StyleEnum.COOKIE && !allowCookie) {
            throw new JsonMappingException(gen, "Parameter style 'cookie' requires OpenAPI 3.2");
        }
        gen.writeString(value.toString());
    }
}

package io.swagger.v3.core.jackson;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.ResolvableSerializer;
import io.swagger.v3.oas.models.media.Schema;
import org.apache.commons.lang3.StringUtils;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

public class SchemaSerializer extends JsonSerializer<Schema> implements ResolvableSerializer {

    private JsonSerializer<Object> defaultSerializer;

    public SchemaSerializer(JsonSerializer<Object> serializer) {
        defaultSerializer = serializer;
    }
    
    @Override
    public void resolve(SerializerProvider serializerProvider) throws JsonMappingException {
        if (defaultSerializer instanceof ResolvableSerializer) {
            ((ResolvableSerializer) defaultSerializer).resolve(serializerProvider);
        }
    }

    @Override
    public void serialize(
            Schema value, JsonGenerator jgen, SerializerProvider provider)
            throws IOException, JsonProcessingException {

        // handle ref schema serialization skipping all other props
        if (StringUtils.isBlank(value.get$ref())) {
            defaultSerializer.serialize(value, jgen, provider);
        } else {
            jgen.writeStartObject();
             jgen.writeStringField("$ref", value.get$ref());
            copyExtensions(value, jgen);
            jgen.writeEndObject();
        }
    }

    protected void copyExtensions(Schema value, JsonGenerator jgen) throws IOException {
        Map<String, Object> extensions = value.getExtensions();
        if(extensions!=null)
        {
            Set<Entry<String, Object>> extensionEntrySet = extensions.entrySet();
            for (Entry<String, Object> entry : extensionEntrySet) 
            {
                String extensionKey = entry.getKey();
                Object extensionValue = entry.getValue();
                if(extensionValue!=null)
                {
                    if(extensionValue instanceof Number)
                    {
                        if(extensionValue instanceof BigDecimal)
                        {
                            jgen.writeNumberField(extensionKey, (BigDecimal)extensionValue);
                        }
                        else if(extensionValue instanceof Double)
                        {
                            jgen.writeNumberField(extensionKey, (Double)extensionValue);
                        }
                        else if(extensionValue instanceof Float)
                        {
                            jgen.writeNumberField(extensionKey, (Float)extensionValue);
                        }
                        else if(extensionValue instanceof Long)
                        {
                            jgen.writeNumberField(extensionKey, (Long)extensionValue);
                        }
                        else if(extensionValue instanceof Integer)
                        {
                            jgen.writeNumberField(extensionKey, (Long)extensionValue);
                        }
                        else
                        {
                            //no other Number types in writeNumberField method
                            jgen.writeObjectField(extensionKey, extensionValue);
                        }
                    }
                    else if(extensionValue instanceof Boolean)
                    {
                        jgen.writeBooleanField(extensionKey, (Boolean)extensionValue);
                    }
                    else if(extensionValue instanceof String)
                    {
                        jgen.writeStringField(extensionKey, (String)extensionValue);
                    }
                    else if(extensionValue instanceof byte[])
                    {
                        jgen.writeBinaryField(extensionKey, (byte[])extensionValue);
                    }
                    else
                    {
                        jgen.writeObjectField(extensionKey, extensionValue);
                    }
                    
                }
                else
                {
                    jgen.writeNullField(extensionKey);
                }
            }
        }
    }
}
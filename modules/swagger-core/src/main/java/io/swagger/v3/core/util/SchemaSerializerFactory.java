package io.swagger.v3.core.util;

import com.fasterxml.jackson.databind.JsonSerializer;

import io.swagger.v3.core.jackson.SchemaSerializer;

public interface SchemaSerializerFactory {

   SchemaSerializer create(JsonSerializer<?> serializer);

}

package io.swagger.converter;

import io.swagger.v3.oas.models.media.Schema;

import java.util.Map;

public class ResolvedSchema {
    public Schema schema;
    public Map<String, Schema> referencedSchemas;
}

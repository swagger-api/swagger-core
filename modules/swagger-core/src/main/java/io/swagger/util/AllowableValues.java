package io.swagger.util;

import io.swagger.models.properties.PropertyBuilder;

import java.util.Map;

public interface AllowableValues {
    Map<PropertyBuilder.PropertyId, Object> asPropertyArguments();
}

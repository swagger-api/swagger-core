package io.swagger.v3.core.util;

import io.swagger.v3.oas.models.SpecVersion;

public class Parameter32Deserializer extends Parameter31Deserializer {

    @Override
    protected SpecVersion specVersion() {
        return SpecVersion.V32;
    }
}

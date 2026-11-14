package io.swagger.v3.core.util;

import io.swagger.v3.oas.models.SpecVersion;

public class Paths32Deserializer extends Paths31Deserializer {

    @Override
    protected SpecVersion specVersion() {
        return SpecVersion.V32;
    }
}

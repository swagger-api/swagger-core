package io.swagger.v3.core.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.v3.oas.models.SpecVersion;

/**
 * Maps a {@link SpecVersion} to the shared version-specific ObjectMapper singletons.
 * Unknown versions are rejected rather than silently falling back to an older dialect.
 */
final class SpecVersionMappers {

    private SpecVersionMappers() {
    }

    static ObjectMapper mapper(SpecVersion specVersion) {
        switch (specVersion) {
            case V30:
                return Json.mapper();
            case V31:
                return Json31.mapper();
            case V32:
                return Json32.mapper();
            default:
                throw new IllegalArgumentException("Unknown specVersion: " + specVersion);
        }
    }
}

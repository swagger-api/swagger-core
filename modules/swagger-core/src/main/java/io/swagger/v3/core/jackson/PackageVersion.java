package io.swagger.v3.core.jackson;

import tools.jackson.core.Version;
import tools.jackson.core.Versioned;
import tools.jackson.core.util.VersionUtil;

public final class PackageVersion implements Versioned {
    public static final Version VERSION = VersionUtil.parseVersion(
            "0.5.1-SNAPSHOT", "io.swagger", "swagger-core");

    @Override
    public Version version() {
        return VERSION;
    }
}

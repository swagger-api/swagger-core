package io.swagger.jackson;

import com.fasterxml.jackson.core.Version;
import com.fasterxml.jackson.core.Versioned;
import com.fasterxml.jackson.core.util.VersionUtil;

public final class PackageVersion implements Versioned {
    public final static Version VERSION = VersionUtil.parseVersion(
            "0.5.1-SNAPSHOT", "io.swagger", "swagger-core");

    @Override
    public Version version() {
        return VERSION;
    }
}

package io.swagger.v3.oas.integration;

import java.util.HashSet;
import java.util.Set;

public final class IgnoredPackages {

    public static final Set<String> ignored = new HashSet();

    static {
        ignored.add("io.swagger.v3.jaxrs2.integration.resources");
        ignored.add("org.glassfish.jersey");
        ignored.add("org.jboss.resteasy");
        ignored.add("com.sun.jersey");
        ignored.add("com.fasterxml.jackson");
    }

}

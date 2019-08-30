package io.swagger.v3.jaxrs2.integration;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.tuple.ImmutablePair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.swagger.v3.oas.integration.api.OpenApiConfigurationLoader;

public class WebOpenApiContext<T extends WebOpenApiContext<T>> extends JaxrsOpenApiContext<T> {

    Logger LOGGER = LoggerFactory.getLogger(WebOpenApiContext.class);

    @Override
    protected List<ImmutablePair<String, String>> getKnownLocations() {

        final List<ImmutablePair<String, String>> locations = new LinkedList<>();
        locations.addAll(super.getKnownLocations());
        return locations;
    }

    @Override
    protected Map<String, OpenApiConfigurationLoader> getLocationLoaders() {
        final Map<String, OpenApiConfigurationLoader> map = super.getLocationLoaders();
        return map;
    }

}

package io.swagger.jaxrs.config;

import io.swagger.annotations.Api;

import java.util.Collection;

/**
 * The {@code ReaderConfig} interface defines configuration settings for
 * JAX-RS annotations reader.
 */
public interface ReaderConfig {

    /**
     * Checks if all resources, but not those with the
     * {@link Api} annotation has to be processed.
     *
     * @return {@code true} if all resource has to be processed
     */
    boolean isScanAllResources();

    /**
     * Returns paths of resources to be ignored.
     *
     * @return collection of paths
     */
    Collection<String> getIgnoredRoutes();
}

package com.wordnik.swagger.jaxrs.config;

import java.util.Collection;

/**
 * The <code>ReaderConfig</code> interface defines configuration settings for
 * JAX-RS annotations reader.
 */
public interface ReaderConfig {

  /**
   * Checks if all resources, but not those with the
   * {@link com.wordnik.swagger.annotations.Api} annotation has to be processed.
   * @return <code>true</code> if all resource has to be processed
   */
  boolean isScanAllResources();

  /**
   * Returns paths of resources to be ignored.
   * @return collection of paths
   */
  Collection<String> getIgnoredRoutes();
}

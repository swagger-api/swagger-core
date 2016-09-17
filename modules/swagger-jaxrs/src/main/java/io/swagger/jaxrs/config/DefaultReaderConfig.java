package io.swagger.jaxrs.config;

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;

/**
 * The {@code ReaderConfig} interface defines configuration settings for
 * JAX-RS annotations reader.
 */
public class DefaultReaderConfig implements ReaderConfig {
    private boolean scanAllResources;
    private Collection<String> ignoredRoutes = Collections.emptySet();

    /**
     * Creates default configuration.
     */
    public DefaultReaderConfig() {
    }

    /**
     * Creates a copy of passed configuration.
     */
    public DefaultReaderConfig(ReaderConfig src) {
        if (src == null) {
            return;
        }
        setScanAllResources(src.isScanAllResources());
        setIgnoredRoutes(src.getIgnoredRoutes());
    }

    @Override
    public boolean isScanAllResources() {
        return scanAllResources;
    }

    public void setScanAllResources(boolean scanAllResources) {
        this.scanAllResources = scanAllResources;
    }

    @Override
    public Collection<String> getIgnoredRoutes() {
        return ignoredRoutes;
    }

    public void setIgnoredRoutes(Collection<String> ignoredRoutes) {
        this.ignoredRoutes = ignoredRoutes == null || ignoredRoutes.isEmpty() ? Collections.<String>emptySet()
                : Collections.unmodifiableCollection(new HashSet<String>(ignoredRoutes));
    }
}

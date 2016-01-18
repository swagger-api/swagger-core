package io.swagger.jaxrs.utils;

import io.swagger.models.Swagger;

import java.net.URI;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Utility class used to store {@link Swagger} by {@link Host}
 *
 * @author pierredeman
 */
public class SwaggerUtils {
    private static Map<Host, Swagger> swaggers = new ConcurrentHashMap<Host, Swagger>();

    /**
     * @param key The Host whose associated value is to be returned
     * @return the Swagger to which the specified Host is mapped, or {@code null} if there is no Swagger for the Host.
     * @throws NullPointerException if the specified Host is {@code null}
     */
    public static Swagger getSwagger(Host key) {
        return swaggers.get(key);
    }

    /**
     * @param key   The Host with which the specified value is to be associated
     * @param value The Swagger to be associated with the specified Host
     * @return the previous Swagger associated with Host, or
     * {@code null} if there was no mapping for Host.
     * @throws NullPointerException if the specified key or value is {@code null}
     *                              and this map does not permit {@code null} keys or values
     */
    public static Swagger putSwagger(Host key, Swagger value) {
        return swaggers.put(key, value);
    }

    public static class Host {
        final String scheme;
        final String host;
        final int port;
        final String path;

        private Host(String scheme, String host, int port, String path) {
            this.scheme = scheme;
            this.host = host;
            this.port = port;
            this.path = path;
        }

        public static Host from(final URI uri) {
            String path = uri.getPath();
            if (path.endsWith("/swagger") || path.endsWith("/swagger.json") || path.endsWith("/swagger.yaml"))
                path = path.substring(0, path.lastIndexOf('/'));
            return new Host(uri.getScheme(), uri.getHost(), uri.getPort(), path);
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            Host host1 = (Host) o;

            if (port != host1.port) return false;
            if (scheme != null ? !scheme.equals(host1.scheme) : host1.scheme != null) return false;
            if (host != null ? !host.equals(host1.host) : host1.host != null) return false;
            if (path != null ? !path.equals(host1.path) : host1.path != null) return false;

            return true;
        }

        @Override
        public int hashCode() {
            int result = scheme != null ? scheme.hashCode() : 0;
            result = 31 * result + (host != null ? host.hashCode() : 0);
            result = 31 * result + port;
            result = 31 * result + (path != null ? path.hashCode() : 0);
            return result;
        }
    }
}

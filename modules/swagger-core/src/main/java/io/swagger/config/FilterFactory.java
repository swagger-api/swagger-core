package io.swagger.config;

import io.swagger.core.filter.SwaggerSpecFilter;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class FilterFactory {
    protected static SwaggerSpecFilter DEFAULT_FILTER;
    protected static Map<String, SwaggerSpecFilter> FILTERS = new ConcurrentHashMap<String, SwaggerSpecFilter>();

    @Deprecated
    public static SwaggerSpecFilter getFilter() {
        return DEFAULT_FILTER;
    }

    @Deprecated
    public static void setFilter(SwaggerSpecFilter filter) {
        DEFAULT_FILTER = filter;
    }

    public static SwaggerSpecFilter getFilter(String host, String basePath) {
        if (DEFAULT_FILTER != null || host == null || basePath == null) {
            return DEFAULT_FILTER;
        }
        return FILTERS.get(host+basePath);
    }

    public static synchronized void setFilter(String host, String basePath, SwaggerSpecFilter filter) {
        if (DEFAULT_FILTER != null || host == null || basePath == null) {
            DEFAULT_FILTER = filter;
        }
        FILTERS.put(host + basePath, filter);
    }
}
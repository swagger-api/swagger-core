package io.swagger.config;

import io.swagger.core.filter.OpenAPISpecFilter;

public class FilterFactory {
    protected static OpenAPISpecFilter FILTER = null;

    public static OpenAPISpecFilter getFilter() {
        return FILTER;
    }

    public static void setFilter(OpenAPISpecFilter filter) {
        FILTER = filter;
    }
}
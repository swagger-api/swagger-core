package com.wordnik.swagger.config;

import com.wordnik.swagger.core.filter.*;

public class FilterFactory {
  protected static SwaggerSpecFilter FILTER = null;

  public static void setFilter(SwaggerSpecFilter filter) {
    FILTER = filter;
  }
  public static SwaggerSpecFilter getFilter() {
    return FILTER;
  }
}
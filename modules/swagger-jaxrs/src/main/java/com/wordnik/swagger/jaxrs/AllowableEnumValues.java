package com.wordnik.swagger.jaxrs;

import java.util.ArrayList;
import java.util.List;

class AllowableEnumValues implements AllowableValues {

  private final List<String> items;

  private AllowableEnumValues(List<String> items) {
    this.items = items;
  }

  public static AllowableEnumValues create(String allowableValues) {
    final List<String> items = new ArrayList<String>();
    for (String value : allowableValues.split(",")) {
      final String trimmed = value.trim();
      if (!trimmed.equals("")) {
        items.add(trimmed);
      }
    }
    return items.isEmpty() ? null : new AllowableEnumValues(items);
  }

  public List<String> getItems() {
    return items;
  }
}

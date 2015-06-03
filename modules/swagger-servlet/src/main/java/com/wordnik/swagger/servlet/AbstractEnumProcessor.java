package com.wordnik.swagger.servlet;

import java.util.List;

abstract class AbstractEnumProcessor<C> extends AbstractAllowableValuesProcessor<C, AllowableEnumValues> {

  @Override
  public void process(C container, AllowableEnumValues values) {
    final List<String> items = values.getItems();
    if (!items.isEmpty()) {
      setEnum(container, items);
    }
  }

  protected abstract void setEnum(C container, List<String> items);
}

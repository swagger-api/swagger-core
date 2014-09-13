package com.wordnik.swagger.converter;

import com.wordnik.swagger.models.properties.Property;

public interface ModelConverter {
  Property resolveProperty(Class<?> cls);
}
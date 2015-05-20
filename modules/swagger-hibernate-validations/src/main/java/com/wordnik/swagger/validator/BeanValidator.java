package com.wordnik.swagger.validator;

import com.wordnik.swagger.converter.*;
import com.wordnik.swagger.models.*;
import com.wordnik.swagger.models.properties.*;
import com.wordnik.swagger.jackson.AbstractModelConverter;
import com.wordnik.swagger.util.Json;

import org.hibernate.validator.constraints.*;

import com.fasterxml.jackson.databind.BeanDescription;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.introspect.AnnotatedMember;
import com.fasterxml.jackson.databind.introspect.AnnotatedClass;
import com.fasterxml.jackson.databind.introspect.BeanPropertyDefinition;

import java.lang.annotation.Annotation;
import java.lang.reflect.Type;

import java.util.*;

public class BeanValidator extends AbstractModelConverter implements ModelConverter {
  public BeanValidator() {
    super(Json.mapper());
  }

  @Override
  public Property resolveProperty(Type type, ModelConverterContext context, Annotation[] annotations, Iterator<ModelConverter> chain) {
    Map<String, Annotation> annos = new HashMap<String, Annotation>();
    if(annotations != null) {
      for(Annotation anno: annotations)
        annos.put(anno.annotationType().getName(), anno);
    }
    Property property = null;

    if(chain.hasNext())
      property = chain.next().resolveProperty(type, context, annotations, chain);
    if(property != null) {
      if(annos.containsKey("org.hibernate.validator.constraints.NotBlank")) {
        property.setRequired(true);
        if(property instanceof StringProperty)
          ((StringProperty)property).minLength(1);
      }
      if(annos.containsKey("org.hibernate.validator.constraints.Range")) {
        if(property instanceof AbstractNumericProperty) {
          Range range = (Range) annos.get("org.hibernate.validator.constraints.Range");
          AbstractNumericProperty ap = (AbstractNumericProperty) property;
          ap.setMinimum(new Double(range.min()));
          ap.setMaximum(new Double(range.max()));
        }
      }
      if(annos.containsKey("org.hibernate.validator.constraints.Length")) {
        if(property instanceof StringProperty) {
          Length length = (Length) annos.get("org.hibernate.validator.constraints.Length");
          StringProperty sp = (StringProperty) property;
          sp.minLength(new Integer(length.min()));
          sp.maxLength(new Integer(length.max()));
        }
      }
      if(annos.containsKey("org.hibernate.validator.constraints.Email")) {
        if(property instanceof StringProperty) {
          EmailProperty sp = new EmailProperty((StringProperty)property);
          property = sp;
        }
      }
      return property;
    }
    return super.resolveProperty(type, context, annotations, chain);
  }
}
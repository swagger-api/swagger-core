package com.wordnik.swagger.validator;

import com.wordnik.swagger.converter.*;
import com.wordnik.swagger.models.*;
import com.wordnik.swagger.models.properties.*;
import com.wordnik.swagger.jackson.AbstractModelConverter;
import com.wordnik.swagger.util.Json;

import javax.validation.constraints.NotNull;

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
    Map<String, Class<Annotation>> allAnnotations = new HashMap<String, Class<Annotation>>();
    if(annotations != null) {
      for(Annotation a: annotations) {
        Class<Annotation> anno = (Class<Annotation>)a.annotationType();
        allAnnotations.put(anno.getName(), anno);
      }
    }
    Property property = null;

    if(allAnnotations.containsKey("javax.validation.constraints.NotNull")) {
      if(chain.hasNext())
        property = chain.next().resolveProperty(type, context, annotations, chain);
      if(property != null)
        property.setRequired(true);
      return property;
    }
    /*
@Size(min = 2, max = 14)
@Min(2)
@Max(2)
@DecimalMin(value=, inclusive=)
@CreditCardNumber(ignoreNonDigitCharacters=)
@Email
@Length(min=, max=)
@NotBlank
@Range(min=, max=)

    */
    return super.resolveProperty(type, context, annotations, chain);
  }
}
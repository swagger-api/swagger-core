package com.wordnik.swagger.validator;

import com.wordnik.swagger.converter.*;
import com.wordnik.swagger.models.*;
import com.wordnik.swagger.models.properties.*;
import com.wordnik.swagger.jackson.AbstractModelConverter;
import com.wordnik.swagger.util.Json;

import javax.validation.constraints.*;

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
    System.out.println(annotations);
    Map<String, Annotation> annos = new HashMap<String, Annotation>();
    if(annotations != null) {
      for(Annotation anno: annotations) {
        // Annotation anno = (Annotation)a.annotationType();
        annos.put(anno.annotationType().getName(), anno);
        System.out.println("annos: " + anno);
      }
    }
    Property property = null;

    if(chain.hasNext())
      property = chain.next().resolveProperty(type, context, annotations, chain);
    if(property != null) {
      if(annos.containsKey("javax.validation.constraints.NotNull")) {
        property.setRequired(true);
      }
      if(annos.containsKey("javax.validation.constraints.Min")) {
        if(property instanceof AbstractNumericProperty) {
          Min min = (Min) annos.get("javax.validation.constraints.Min");
          AbstractNumericProperty ap = (AbstractNumericProperty) property;
          ap.setMinimum(new Double(min.value()));
        }
      }
      if(annos.containsKey("javax.validation.constraints.Max")) {
        if(property instanceof AbstractNumericProperty) {
          Max max = (Max) annos.get("javax.validation.constraints.Max");
          AbstractNumericProperty ap = (AbstractNumericProperty) property;
          ap.setMaximum(new Double(max.value()));
        }
      }

      return property;
    }
    /*
@Size(min = 2, max = 14)
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
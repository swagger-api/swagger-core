package com.wordnik.swagger.validator;

import com.wordnik.swagger.converter.*;
import com.wordnik.swagger.models.*;
import com.wordnik.swagger.models.properties.*;
import com.wordnik.swagger.jackson.AbstractModelConverter;
import com.wordnik.swagger.util.Json;

import javax.validation.constraints.*;
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
    System.out.println(annotations);
    Map<String, Annotation> annos = new HashMap<String, Annotation>();
    if(annotations != null) {
      for(Annotation anno: annotations)
        annos.put(anno.annotationType().getName(), anno);
    }
    Property property = null;

    if(chain.hasNext())
      property = chain.next().resolveProperty(type, context, annotations, chain);
    if(property != null) {
      if(annos.containsKey("javax.validation.constraints.NotNull")) {
        property.setRequired(true);
      }
      if(annos.containsKey("org.hibernate.validator.constraints.NotBlank")) {
        property.setRequired(true);
        if(property instanceof StringProperty)
          ((StringProperty)property).minLength(1);
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
      if(annos.containsKey("javax.validation.constraints.Size")) {
        Size size = (Size) annos.get("javax.validation.constraints.Size");
        if(property instanceof AbstractNumericProperty) {
          AbstractNumericProperty ap = (AbstractNumericProperty) property;
          ap.setMinimum(new Double(size.min()));
          ap.setMaximum(new Double(size.max()));
        }
        if(property instanceof StringProperty) {
          StringProperty sp = (StringProperty) property;
          sp.minLength(new Integer(size.min()));
          sp.maxLength(new Integer(size.max()));
        }
      }
      if(annos.containsKey("javax.validation.constraints.DecimalMin")) {
        DecimalMin min = (DecimalMin) annos.get("javax.validation.constraints.DecimalMin");
        if(property instanceof AbstractNumericProperty) {
          AbstractNumericProperty ap = (AbstractNumericProperty) property;
          if(min.inclusive())
            ap.setMinimum(new Double(min.value()));
          else
            ap.setExclusiveMinimum(new Double(min.value()));
        }
      }
      if(annos.containsKey("javax.validation.constraints.DecimalMax")) {
        DecimalMax max = (DecimalMax) annos.get("javax.validation.constraints.DecimalMax");
        if(property instanceof AbstractNumericProperty) {
          AbstractNumericProperty ap = (AbstractNumericProperty) property;
          if(max.inclusive())
            ap.setMaximum(new Double(max.value()));
          else
            ap.setExclusiveMaximum(new Double(max.value()));
        }
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
    /*
@CreditCardNumber(ignoreNonDigitCharacters=)
@Email
    */
    return super.resolveProperty(type, context, annotations, chain);
  }
}
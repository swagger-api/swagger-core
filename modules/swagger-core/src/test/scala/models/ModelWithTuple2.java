package models;

import com.wordnik.swagger.jackson.AbstractModelConverter;
import com.wordnik.swagger.annotations.ApiModelProperty;
import com.wordnik.swagger.util.Json;
import com.wordnik.swagger.models.*;
import com.wordnik.swagger.models.properties.*;
import com.wordnik.swagger.converter.*;

import org.apache.commons.lang3.tuple.Pair;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.lang.annotation.Annotation;
import java.lang.reflect.Type;

import java.util.*;

public class ModelWithTuple2 {
  @ApiModelProperty(value = "Possible values for state property of timesheet or timesheet entry", required = true)
  public Pair<String, String> timesheetStates;

  @ApiModelProperty(value = "set of pairs", required = true)
  public Set<Pair<String, String>> manyPairs;

  @ApiModelProperty(value = "set of pairs wiht complex left", required = true)
  public Set<Pair<ComplexLeft, String>> complexLeft;

  static class ComplexLeft {
    public String name;
    public Integer age;
  }

  public static class TupleModelConverter extends AbstractModelConverter implements ModelConverter {
    public TupleModelConverter(ObjectMapper mapper) {
      super(mapper);
    }

    @Override
    public Property resolveProperty(Type type, ModelConverterContext context, Annotation[] annotations, Iterator<ModelConverter> chain) {  
      JavaType _type = Json.mapper().constructType(type);
      if(_type != null){
        Class<?> cls = _type.getRawClass();
        if(Pair.class.isAssignableFrom(cls)) {
          return new MapProperty()
            .additionalProperties(new StringProperty());
        }
      }
      if(chain.hasNext())
        return chain.next().resolveProperty(type, context, annotations, chain);
      else
        return null;
    }

    @Override
    public Model resolve(Type type, ModelConverterContext context, Iterator<ModelConverter> chain) {
      JavaType _type = Json.mapper().constructType(type);
      if(_type != null){
        Class<?> cls = _type.getRawClass();
        if(Pair.class.isAssignableFrom(cls)) {
          String name = _typeName(_type); // use name from type?
          name = "MyPair";

          ModelImpl model = new ModelImpl()
            .name(name)
            .additionalProperties(new StringProperty());

          return model;
        }
      }

      if(chain.hasNext())
        return chain.next().resolve(type, context, chain);
      else
        return null;
    }
  }
}
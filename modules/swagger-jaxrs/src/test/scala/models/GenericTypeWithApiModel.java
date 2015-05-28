package models;

import com.wordnik.swagger.annotations.ApiModel;

@ApiModel("RenamedGenericType")
public class GenericTypeWithApiModel<T> {
  public T value;
}

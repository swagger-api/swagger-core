package io.swagger.models;

import io.swagger.annotations.ApiModel;

@ApiModel("RenamedGenericType")
public class GenericTypeWithApiModel<T> {
    public T value;
}

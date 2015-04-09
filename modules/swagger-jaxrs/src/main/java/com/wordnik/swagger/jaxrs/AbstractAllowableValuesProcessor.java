package com.wordnik.swagger.jaxrs;

abstract class AbstractAllowableValuesProcessor<C, V extends AllowableValues> {

  public abstract void process(C container, V values);
}

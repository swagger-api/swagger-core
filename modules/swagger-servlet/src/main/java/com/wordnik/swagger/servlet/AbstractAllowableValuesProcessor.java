package com.wordnik.swagger.servlet;

abstract class AbstractAllowableValuesProcessor<C, V extends AllowableValues> {

  public abstract void process(C container, V values);
}

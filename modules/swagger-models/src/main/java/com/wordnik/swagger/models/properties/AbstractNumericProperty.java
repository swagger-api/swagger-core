package com.wordnik.swagger.models.properties;


public abstract class AbstractNumericProperty extends AbstractProperty implements Property {
  protected Double minimum = null, maximum = null, exclusiveMinimum = null, exclusiveMaximum = null;

  public AbstractNumericProperty minimum(Double minimum) {
    this.setMinimum(minimum);
    return this;
  }
  public AbstractNumericProperty maximum(Double maximum) {
    this.setMaximum(maximum);
    return this;
  }
  public AbstractNumericProperty exclusiveMinimum(Double exclusiveMinimum) {
    this.setExclusiveMinimum(exclusiveMinimum);
    return this;
  }
  public AbstractNumericProperty exclusiveMaximum(Double exclusiveMaximum) {
    this.setExclusiveMaximum(exclusiveMaximum);
    return this;
  }

  public Double getMinimum() {
    return minimum;
  }
  public void setMinimum(Double minimum) {
    this.minimum = minimum;
  }

  public Double getMaximum() {
    return maximum;
  }
  public void setMaximum(Double maximum) {
    this.maximum = maximum;
  }

  public Double getExclusiveMinimum() {
    return exclusiveMinimum;
  }
  public void setExclusiveMinimum(Double exclusiveMinimum) {
    this.exclusiveMinimum = exclusiveMinimum;
  }

  public Double getExclusiveMaximum() {
    return exclusiveMaximum;
  }
  public void setExclusiveMaximum(Double exclusiveMaximum) {
    this.exclusiveMaximum = exclusiveMaximum;
  }
}
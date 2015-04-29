package com.wordnik.swagger.models.properties;


public abstract class AbstractNumericProperty extends AbstractProperty implements Property {
  protected Double minimum, maximum;
  protected Boolean exclusiveMinimum, exclusiveMaximum;

  public AbstractNumericProperty minimum(Double minimum) {
    this.setMinimum(minimum);
    return this;
  }
  public AbstractNumericProperty maximum(Double maximum) {
    this.setMaximum(maximum);
    return this;
  }
  public AbstractNumericProperty exclusiveMinimum(Boolean exclusiveMinimum) {
    this.setExclusiveMinimum(exclusiveMinimum);
    return this;
  }
  public AbstractNumericProperty exclusiveMaximum(Boolean exclusiveMaximum) {
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

  public Boolean getExclusiveMinimum() {
    return exclusiveMinimum;
  }
  public void setExclusiveMinimum(Boolean exclusiveMinimum) {
    this.exclusiveMinimum = exclusiveMinimum;
  }

  public Boolean getExclusiveMaximum() {
    return exclusiveMaximum;
  }
  public void setExclusiveMaximum(Boolean exclusiveMaximum) {
    this.exclusiveMaximum = exclusiveMaximum;
  }
}
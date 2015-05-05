package com.wordnik.swagger.servlet;

abstract class AbstractRangeProcessor<C> extends AbstractAllowableValuesProcessor<C, AllowableRangeValues> {

  @Override
  public void process(C container, AllowableRangeValues values) {
    final Double minimum = values.getMinimum();
    if (minimum != null) {
      setMinimum(container, minimum);
    }

    if (values.isExclusiveMinimum()) {
      setExclusiveMinimum(container, true);
    }

    final Double maximum = values.getMaximum();
    if (maximum != null) {
      setMaximum(container, maximum);
    }

    if (values.isExclusiveMaximum()) {
      setExclusiveMaximum(container, true);
    }
  }

  protected abstract void setMinimum(C container, Double value);

  protected abstract void setExclusiveMinimum(C container, Boolean value);

  protected abstract void setMaximum(C container, Double value);

  protected abstract void setExclusiveMaximum(C container, Boolean value);
}

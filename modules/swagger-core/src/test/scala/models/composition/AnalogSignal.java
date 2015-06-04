package models.composition;

import io.swagger.annotations.ApiModel;

@ApiModel(description = "AnalogSignal", parent = Signal.class)
public class AnalogSignal extends AbstractSignal implements Signal {
  private float analogValue;

  public float getAnalogValue() {
    return analogValue;
  }

  public void setAnalogValue(float analogValue) {
    this.analogValue = analogValue;
  }
}
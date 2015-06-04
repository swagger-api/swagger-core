package models.composition;

import io.swagger.annotations.ApiModel;

@ApiModel(description = "AnalogSignal", parent = DigitalSignal.class)
public class DigitalSignal extends AbstractSignal implements Signal {
  private boolean digitalValue;

  public boolean getDigitalValue() {
    return digitalValue;
  }

  public void setDigitalValue(boolean digitalValue) {
    this.digitalValue = digitalValue;
  }
}
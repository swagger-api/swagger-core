package models.composition;

public abstract class AbstractSignal {
  private int type;
  private byte[] value;

  public int getType() {
    return type;
  }

  public void setType(int type) {
    this.type = type;
  }

  public byte[] getRawValue() {
    return value;
  }

  public void setRawValue(byte[] value) {
    this.value = value;
  }
}
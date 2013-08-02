package model;

public class BaseModel<T> {
  private T t;

  public void setValue(T t) {
    this.t = t;
  }

  public T getValue() {
    return t;
  }
}
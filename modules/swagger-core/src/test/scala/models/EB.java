package models;


import javax.xml.bind.annotation.*;

@XmlRootElement
public class EB {
  private String bar;

  @XmlElement(name="foo")
  public String getBar() {
    return bar;
  }

  public void setBar(String bar) {
    this.bar = bar;
  }
}
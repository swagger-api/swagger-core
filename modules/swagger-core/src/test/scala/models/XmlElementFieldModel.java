package models;

import javax.xml.bind.annotation.*;

@XmlRootElement(name = "Pet")
public class XmlElementFieldModel {
  @XmlElement(name = "pet_name")
  public String name;

  @XmlAttribute(name = "pet_age")
  public Integer age;
}
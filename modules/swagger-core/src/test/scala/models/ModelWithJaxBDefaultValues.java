package models;

import javax.xml.bind.annotation.*;

@XmlRootElement
public class ModelWithJaxBDefaultValues {
  @XmlElement(defaultValue = "Tony")
  public String name;

  @XmlElement(defaultValue = "100")
  public Integer age;

}
package models;

import com.wordnik.swagger.annotations.*;

import javax.xml.bind.annotation.*;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
@ApiModel( value = "aaa")
public class ApiFirstRequiredFieldModel {
  @ApiModelProperty( value = "bla", required = true )
  @XmlElement(name="a")
  public String getA() {
    return "aaa";
  }

  public String getC() {
    return "kkk";
  };
}

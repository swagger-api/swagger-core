package models;

import com.wordnik.swagger.annotations.*;

import javax.xml.bind.annotation.*;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
@ApiModel( value = "aaa")
public class XmlFirstRequiredFieldModel {
  @XmlElement(name="a")
  @ApiModelProperty( value = "bla", required = true )
  public String getA() {
    return "aaa";
  }

  public String getC() {
    return "kkk";
  };
}

package models;

import com.wordnik.swagger.annotations.*;

import javax.xml.bind.annotation.*;
import java.util.List;

public class Issue534 {
  public String name;

  @XmlElementWrapper(name="order_specials")
  @XmlElement(name="order_special")
  @ApiModelProperty(hidden = true)
  public List<SpecialOrderItem> getOrder_specials() {
    return null;
  }

  public void setOrder_specials(List<SpecialOrderItem> items) {

  }
}
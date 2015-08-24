package io.swagger.models;

import io.swagger.annotations.ApiModelProperty;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import java.util.List;

public class Issue534 {
    public String name;

    @XmlElementWrapper(name = "order_specials")
    @XmlElement(name = "order_special")
    @ApiModelProperty(hidden = true)
    public List<SpecialOrderItem> getOrder_specials() {
        return null;
    }

    public void setOrder_specials(List<SpecialOrderItem> items) {

    }
}
package io.swagger.v3.core.oas.models;

import io.swagger.v3.oas.annotations.media.Schema;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import java.util.List;

public class Issue534 {
    public String name;

    @XmlElementWrapper(name = "order_specials")
    @XmlElement(name = "order_special")
    @Schema(hidden = true)
    public List<SpecialOrderItem> getOrder_specials() {
        return null;
    }

    public void setOrder_specials(List<SpecialOrderItem> items) {

    }
}
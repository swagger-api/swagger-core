package io.swagger.models;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRootName;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonTypeInfo.As;
import com.fasterxml.jackson.annotation.JsonTypeInfo.Id;
import com.fasterxml.jackson.annotation.JsonTypeName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "link")
@XmlAccessorType(XmlAccessType.PUBLIC_MEMBER)
@JsonRootName("link")
@JsonTypeName("link")
@JsonInclude(Include.NON_EMPTY)
@JsonTypeInfo(include = As.WRAPPER_OBJECT, use = Id.NAME)
@ApiModel(description = "Represents an association to another resource in the system", value = "link")
public class Link<T> {
    private String href;
    private String rel;
    private String status;

    public Link() {
    }

    @XmlElement
    @JsonProperty
    @ApiModelProperty(access = "public", required = true, position = 1)
    public String getHref() {
        return href;
    }

    public void setHref(String href) {
        this.href = href;
    }

    @XmlElement
    @JsonProperty
    @ApiModelProperty(access = "public", required = true, position = 2)
    public String getRel() {
        return rel;
    }

    public void setRel(String rel) {
        this.rel = rel;
    }

    @XmlElement
    @JsonProperty
    @ApiModelProperty(access = "public", required = false, position = 3)
    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

}
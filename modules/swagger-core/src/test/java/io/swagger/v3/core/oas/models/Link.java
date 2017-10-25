package io.swagger.v3.core.oas.models;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRootName;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonTypeInfo.As;
import com.fasterxml.jackson.annotation.JsonTypeInfo.Id;
import com.fasterxml.jackson.annotation.JsonTypeName;
import io.swagger.v3.oas.annotations.media.Schema;

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
@Schema(description = "Represents an association to another resource in the system", title = "link")
public class Link<T> {
    private String href;
    private String rel;
    private String status;

    public Link() {
    }

    @XmlElement
    @JsonProperty
    @Schema(required = true)
    public String getHref() {
        return href;
    }

    public void setHref(String href) {
        this.href = href;
    }

    @XmlElement
    @JsonProperty
    @Schema(required = true)
    public String getRel() {
        return rel;
    }

    public void setRel(String rel) {
        this.rel = rel;
    }

    @XmlElement
    @JsonProperty
    @Schema(required = true)
    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

}
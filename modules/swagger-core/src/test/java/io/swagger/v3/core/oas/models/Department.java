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

@XmlRootElement(name = "department")
@XmlAccessorType(XmlAccessType.PUBLIC_MEMBER)
@JsonRootName("department")
@JsonTypeName("department")
@JsonInclude(Include.NON_EMPTY)
@JsonTypeInfo(include = As.WRAPPER_OBJECT, use = Id.NAME)
@Schema(description = "Represents a Department in the system", title = "department")
public class Department {
    private String name;
    private String deptCode;
    private Link<Department> parent;

    public Department() {
    }

    @XmlElement
    @JsonProperty
    @Schema(required = true)
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @XmlElement
    @JsonProperty
    @Schema(required = true)
    public String getDeptCode() {
        return deptCode;
    }

    public void setDeptCode(String deptCode) {
        this.deptCode = deptCode;
    }

    @JsonProperty("parentDepartment")
    @XmlElement(name = "parentDepartment")
    @Schema(
            type = "Link",
            required = true,
            description = "This department's parent. If this is a top-level department, the parent would be the enterprise.")
    public Link<Department> getParent() {
        return parent;
    }

    public void setParent(Link<Department> parent) {
        this.parent = parent;
    }

}
package com.wordnik.swagger.sample.model.issue606;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonRootName;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.wordnik.swagger.annotations.ApiModel;
import com.wordnik.swagger.annotations.ApiModelProperty;

@XmlRootElement(name = "salary")
@XmlAccessorType(XmlAccessType.PUBLIC_MEMBER)
@JsonRootName("salary")
@JsonTypeName("salary")
@JsonInclude(Include.NON_EMPTY)
// @JsonTypeInfo(include = As.WRAPPER_OBJECT, use = Id.NAME)
@ApiModel(description = "Represents an employee's salary", value = "salary")
public class EmployeeSalary extends AbstractModel
{
    private String currency;
    private float amount;

    @ApiModelProperty(required = true)
    public String getCurrency()
    {
        return currency;
    }

    public void setCurrency( String currency )
    {
        this.currency = currency;
    }

    @ApiModelProperty(required = true)
    public float getAmount()
    {
        return amount;
    }

    public void setAmount( float amount )
    {
        this.amount = amount;
    }

}

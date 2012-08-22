package com.wordnik.swagger.core;

import javax.xml.bind.annotation.*;

import com.fasterxml.jackson.annotation.*;

import static com.fasterxml.jackson.annotation.JsonTypeInfo.*;

@JsonTypeInfo(use=Id.NAME, include=As.PROPERTY, property="valueType")
@JsonSubTypes({
    @JsonSubTypes.Type(value=DocumentationAllowableListValues.class, name="LIST"),
    @JsonSubTypes.Type(value=DocumentationAllowableRangeValues.class, name="RANGE")
})  
@XmlSeeAlso({ DocumentationAllowableListValues.class,
		DocumentationAllowableRangeValues.class })
public abstract class DocumentationAllowableValues {
	public abstract DocumentationAllowableValues copy();

	public abstract String getValueType();

	public abstract void setValueType(String valueType);
}

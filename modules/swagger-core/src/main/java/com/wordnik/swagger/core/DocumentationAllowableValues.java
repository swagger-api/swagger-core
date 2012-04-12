package com.wordnik.swagger.core;

import javax.xml.bind.annotation.XmlSeeAlso;

import org.codehaus.jackson.*;
import org.codehaus.jackson.annotate.*;
import org.codehaus.jackson.map.*;
import org.codehaus.jackson.map.annotate.*;

import static org.codehaus.jackson.annotate.JsonTypeInfo.*;

@JsonTypeInfo(use=Id.NAME, include=As.PROPERTY, property="valueType")
@JsonSubTypes({
    @JsonSubTypes.Type(value=DocumentationAllowableListValues.class, name="LIST"),
    @JsonSubTypes.Type(value=DocumentationAllowableRangeValues.class, name="RANGE")
})  
@XmlSeeAlso({ DocumentationAllowableListValues.class,
		DocumentationAllowableRangeValues.class })
public interface DocumentationAllowableValues {
	public DocumentationAllowableValues copy();

	public String getValueType();

	public void setValueType(String valueType);
}

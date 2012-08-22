package com.wordnik.swagger.core;

import com.fasterxml.jackson.databind.annotation.*;
import javax.xml.bind.annotation.*;

@JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
@XmlRootElement(name = "allowableRangeValues")
public class DocumentationAllowableRangeValues extends DocumentationAllowableValues {
	float min;
	float max;

	public DocumentationAllowableRangeValues copy() {
		return new DocumentationAllowableRangeValues(min, max);
	}
	
	public String getValueType() {
		return "RANGE";
	}
	
	public void setValueType(String valueType){}
	
	public Float getMin() {
		return min;
	}
	
	public void setMin(float min) {
		this.min = min;
	}
	
	public Float getMax() {
		return max;
	}
	
	public void setMax(float max){
		this.max = max;
	}
	
	public DocumentationAllowableRangeValues(){}
	
	public DocumentationAllowableRangeValues(float min, float max) {
		this.min = min;
		this.max = max;
	}
}

package com.wordnik.swagger.core;

import com.fasterxml.jackson.databind.annotation.*;
import javax.xml.bind.annotation.*;

import java.util.List;

@JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
@XmlRootElement(name = "allowableListValues")
public class DocumentationAllowableListValues extends DocumentationAllowableValues {
	private List<String> values = null;

	public DocumentationAllowableListValues copy() {
		if (values != null) {
			java.util.List<String> cloned = new java.util.ArrayList<String>();
			for (String v : values) {
				cloned.add(v);
			}
			return new DocumentationAllowableListValues(cloned);
		}
		return new DocumentationAllowableListValues();
	}

	public DocumentationAllowableListValues() {}

	public DocumentationAllowableListValues(List<String> values) {
		this.values = values;
	}

	public List<String> getValues() {
		return values;
	}

	public void setValues(List<String> values) {
		this.values = values;
	}

	public String getValueType() {
		return "LIST";
	}

	public void setValueType(String valueType) {
	}
}

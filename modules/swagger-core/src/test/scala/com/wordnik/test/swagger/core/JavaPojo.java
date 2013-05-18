package com.wordnik.test.swagger.core;

import javax.xml.bind.annotation.*;

@XmlRootElement(name = "JavaPojo")
@XmlAccessorType(XmlAccessType.FIELD)
public class JavaPojo {
	@XmlElement(required = true)
	private String key;
	private String status;
	private Integer order;
	private String title;
	private String hint;

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}
}
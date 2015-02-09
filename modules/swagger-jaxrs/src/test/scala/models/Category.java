package models;

import javax.xml.bind.annotation.*;

@XmlRootElement(name = "Category")
public class Category {
	private long id;
	private String name;

	@XmlElement(name = "id")
	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	@XmlElement(name = "name")
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
}
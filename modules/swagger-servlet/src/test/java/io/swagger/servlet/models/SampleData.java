package io.swagger.servlet.models;

import java.util.Date;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "SampleData")
public class SampleData {

    private Integer id;
    private String name;
    private String email;
    private Integer age;
    private Date dateOfBirth;

    public SampleData(Integer id, String name, String email, Integer age, Date dateOfBirth) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.age = age;
        this.dateOfBirth = dateOfBirth;
    }

    @XmlElement(name = "id")
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    @XmlElement(name = "name")
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @XmlElement(name = "email")
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @XmlElement(name = "age")
    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    @XmlElement(name = "dateOfBirth")
    public Date getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(Date dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }
}

package models;

import java.util.Set;

import javax.xml.bind.annotation.*;

import com.fasterxml.jackson.annotation.*;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonTypeInfo.As;
import com.fasterxml.jackson.annotation.JsonTypeInfo.Id;

import com.wordnik.swagger.annotations.*;

@XmlRootElement(name = "employee")
@XmlAccessorType(XmlAccessType.PUBLIC_MEMBER)
@JsonRootName("employee")
@JsonTypeName("employee")
@JsonInclude(Include.NON_EMPTY)
@JsonTypeInfo(include = As.WRAPPER_OBJECT, use = Id.NAME)
@ApiModel(description = "Represents an Employee in the system", value = "employee")
public class Employee {
  private int id;
  private String firstName;
  private String lastName;
  private Link<Department> dept;
  private Link<Employee> manager;
  private Set<Link<Employee>> subordinates;

  public Employee() {
    // TODO Auto-generated constructor stub
  }

  @XmlElement
  @JsonProperty
  @ApiModelProperty(access = "public", notes = "Note, this is server generated.", value = "Read-only")
  public int getId() {
    return id;
  }

  public void setId( int id ) {
    this.id = id;
  }

  @XmlElement
  @JsonProperty
  @ApiModelProperty(access = "public", required = true)
  public String getFirstName() {
    return firstName;
  }

  public void setFirstName( String firstName ) {
    this.firstName = firstName;
  }

  @XmlElement
  @JsonProperty
  @ApiModelProperty(required = true)
  public String getLastName() {
    return lastName;
  }

  public void setLastName( String lastName ) {
    this.lastName = lastName;
  }

  @JsonProperty("department")
  @XmlElement(name = "department")
  @ApiModelProperty(dataType = "models.Link", required = true, value = "The department this employee belongs to.")
  public Link<Department> getDept() {
    return dept;
  }

  public void setDept( Link<Department> dept ) {
    this.dept = dept;
  }

  @JsonProperty("manager")
  @XmlElement(name = "manager")
  @ApiModelProperty(dataType = "models.Link", required = true, value = "The employee this employee reports to.")
  public Link<Employee> getManager() {
    return manager;
  }

  public void setManager( Link<Employee> manager ) {
    this.manager = manager;
  }

  @JsonProperty("team")
  @XmlElement(name = "team")
  @ApiModelProperty(required = true, value = "List of employees that report to this employee.")
  public Set<Link<Employee>> getSubordinates() {
    return subordinates;
  }

  public void setSubordinates( Set<Link<Employee>> subordinates ) {
    this.subordinates = subordinates;
  }
}
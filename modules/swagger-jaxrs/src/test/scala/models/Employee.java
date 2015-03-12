package models;

import javax.xml.bind.annotation.*;

@XmlRootElement(name = "Employee")
public class Employee {
  public long id;
  public String name;
}
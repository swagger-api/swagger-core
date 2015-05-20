package models;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import javax.xml.bind.annotation.XmlID;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "message")
// @XmlAccessorType(XmlAccessType.NONE)
public class Message implements Serializable {       
  @XmlAttribute
  private String id;
  
  @XmlAttribute
  private Date date;
  
  @XmlElement(name = "body")
  private String body;

  private List<String> tag;

  public Message () {}

  public void setId(String id) {
    this.id = id;
  }
  
  public String getId() {
    return this.id;
  }

  public void setUser(String person) {}
  
  @JsonIgnore
  public String getUserName() {
    return null;
  }
  
  @XmlElement
  public List<String> getTags() {
    return new ArrayList<String>();
  }
}
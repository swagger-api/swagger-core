package com.wordnik.swagger.sample.model

import javax.xml.bind.annotation.{XmlRootElement, XmlElement}


abstract class BaseModel() {
  private var id:Long = 0

  @XmlElement(name="id")
  def getId():Long = id
  def setId(id:Long) = this.id = id
}
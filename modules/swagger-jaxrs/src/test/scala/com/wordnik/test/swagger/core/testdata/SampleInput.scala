package com.wordnik.test.swagger.core.testdata

import javax.xml.bind.annotation.XmlRootElement._
import reflect.BeanProperty
import javax.xml.bind.annotation.XmlElement._
import javax.xml.bind.annotation.{XmlAccessType, XmlAccessorType, XmlElement, XmlRootElement}
import annotation.target.field

@XmlRootElement(name = "sampleInput")
@XmlAccessorType(XmlAccessType.NONE)
case class SampleInput (
    @(XmlElement @field)(name = "name") @BeanProperty
    var name: String,

    @(XmlElement @field)(name = "value") @BeanProperty
    var value: String
){
  def this() = this(null,null)
}
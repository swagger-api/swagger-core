package com.wordnik.test.swagger.core.testdata

import javax.xml.bind.annotation.{XmlAccessType, XmlAccessorType, XmlElement, XmlRootElement}

import scala.reflect.BeanProperty

import scala.annotation.target.field

@XmlRootElement(name = "sampleInput")
@XmlAccessorType(XmlAccessType.NONE)
case class SampleInput (
  @(XmlElement @field)(name = "name") @BeanProperty var name: String,
  @(XmlElement @field)(name = "value") @BeanProperty var value: String
) {
  def this() = this(null,null)
}
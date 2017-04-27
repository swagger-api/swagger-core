package com.wordnik.swagger.converter

object ConverterUtil {
  /**
    * @param cls
    * @return - true if the class represents a value in an Enumeration
    *         false otherwise
    */
  def isScalaEnumValue(cls: Class[_]): Boolean ={
    cls.getName().compareTo("scala.Enumeration$Value") == 0
  }

  /**
    * @param cls
    * @return - true if the class represents an Enum or a Scala Enumeration Value
    *         false otherwise
    */
  def isEnum(cls: Class[_]): Boolean ={
    cls.isEnum || isScalaEnumValue(cls)
  }

}

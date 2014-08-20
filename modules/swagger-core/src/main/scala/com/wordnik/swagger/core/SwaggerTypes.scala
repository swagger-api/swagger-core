package com.wordnik.swagger.core

object SwaggerTypes {
  val primitives = Map(
    "void" -> "void",
    "byte" -> "byte",
    "boolean" -> "boolean",
    "int" -> "int",
    "long" -> "long",
    "float" -> "float",
    "double" -> "double",
    "string" -> "string",
    "date" -> "Date",
    "integer" -> "int",
    "double" -> "double",
    "float" -> "float",
    "boolean" -> "boolean",
    "uuid" -> "string")

  def apply(str: String) = {
    primitives.getOrElse(str.toLowerCase, "object")
  }
}
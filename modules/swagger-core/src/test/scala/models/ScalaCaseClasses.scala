package models

import org.joda.time.DateTime

case class SimpleCaseClass (name: String, count: Int)

case class CaseClassWithList (
  name: String,
  items: List[String]
)

case class CaseClassWithOptionLong (
  intValue: Int,
  longValue: Option[Long],
  setValue: Set[String],
  dateValue: java.util.Date,
  booleanValue: Boolean
)

case class NestedModel (
  complexModel: ComplexModel,
  localtime: DateTime
)

case class ComplexModel (
  name: String,
  age: Int
)
package com.wordnik.swagger.sample.model

import com.wordnik.swagger.annotations._

import java.util.Date

case class Pet(var id: Long = 0,
  var category: Category,
  var name: String,
  var photoUrls: List[String],
  var tags: List[Tag] = List[Tag](),
  @ApiProperty(value = "pet status in the store", allowableValues = "available,pending,sold") var status: String)

case class Tag(var id: Long = 0, var name: String)

case class User(var id: Long = 0,
  var username: String,
  var firstName: String,
  var lastName: String,
  var email: String,
  var password: String,
  var phone: String,
  @ApiProperty(value = "User Status", allowableValues = "1-registered,2-active,3-closed") var userStatus: Int)

case class Order(var id: Long = 0,
  var petId: Long = 0,
  var quantity: Int = 0,
  var shipDate: Date,
  @ApiProperty(value = "Order Status", allowableValues = "placed, approved, delivered") var status: String)

case class Category(var id: Long = 0, var name: String)

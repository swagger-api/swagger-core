/**
 *  Copyright 2012 Wordnik, Inc.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */

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

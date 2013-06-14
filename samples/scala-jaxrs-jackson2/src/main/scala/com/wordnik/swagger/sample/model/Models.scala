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

case class Pet(
  id: Long,
  category: Category,
  name: String,
  photoUrls: List[String],
  tags: List[Tag] = List[Tag](),
  @ApiModelProperty(value = "pet status in the store", allowableValues = "available,pending,sold") status: String)

case class Tag(
  id: Long,
  name: String)

case class User(
  id: Long,
  username: String,
  firstName: String,
  lastName: String,
  email: String,
  password: String,
  phone: String,
  @ApiModelProperty(value = "User Status", allowableValues = "1-registered,2-active,3-closed") userStatus: Int)

case class Order(
  id: Long,
  petId: Long,
  quantity: Int,
  shipDate: Date,
  @ApiModelProperty(value = "Order Status", allowableValues = "placed, approved, delivered") status: String)

case class Category(
  id: Long, 
  name: String)

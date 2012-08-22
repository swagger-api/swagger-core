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

import javax.xml.bind.annotation._

import java.lang.Long

@XmlRootElement(name = "User")
class User {
  private var id:Long = 0
  private var username:String = _
  private var firstName:String = _
  private var lastName:String = _
  private var email:String = _
  private var password:String = _
  private var phone:String = _
  private var userStatus:Int = _

  @XmlElement(name="id")
  def getId():Long = {
    id
  }

  def setId(id:Long):Unit = {
    this.id = id
  }


  @XmlElement(name="firstName")
  def getFirstName():String = {
    firstName
  }

  def setFirstName(firstName:String):Unit = {
    this.firstName = firstName
  }

  @XmlElement(name="username")
  def getUsername():String = {
    username
  }

  def setUsername(username:String):Unit = {
    this.username = username
  }


  @XmlElement(name="lastName")
  def getLastName():String = {
    lastName
  }

  def setLastName(lastName:String):Unit = {
    this.lastName = lastName
  }

  @XmlElement(name="email")
  def getEmail():String = {
    email
  }

  def setEmail(email:String):Unit = {
    this.email= email
  }

  @XmlElement(name="password")
  def getPassword():String = {
    password
  }

  def setPassword(password:String):Unit = {
    this.password = password
  }

  @XmlElement(name="phone")
  def getPhone():String = {
    phone
  }

  def setPhone(phone:String):Unit = {
    this.phone = phone
  }

  @XmlElement(name="userStatus")
  @ApiProperty(value = "User Status", allowableValues = "1-registered,2-active,3-closed")
  def getUserStatus():Int = {
    userStatus
  }

  def setUserStatus(userStatus:Int):Unit = {
    this.userStatus = userStatus
  }
}
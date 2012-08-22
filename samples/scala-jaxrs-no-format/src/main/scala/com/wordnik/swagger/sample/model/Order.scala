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

import javax.xml.bind.annotation.XmlRootElement._
import javax.xml.bind.annotation.{XmlElement, XmlRootElement}
import javax.xml.bind.annotation.XmlElement._

@XmlRootElement(name = "Order")
class Order {
  private var id:Long = 0
  private var petId:Long = 0
  private var quantity:Int = 0
  private var shipDate:Date = null
  private var status:String = null

  @XmlElement(name="id")
  def getId():Long = {
    id
  }

  def setId(id:Long):Unit = {
    this.id = id
  }

  @XmlElement(name="petId")
  def getPetId():Long = {
    petId
  }

  def setPetId(petId:Long):Unit = {
    this.petId = petId
  }

  @XmlElement(name="quantity")
  def getQuantity():Int = {
    quantity
  }

  def setQuantity(quantity:Int):Unit = {
    this.quantity = quantity
  }

  @XmlElement(name="status")
  @ApiProperty(value = "Order Status", allowableValues = "placed, approved, delivered")
  def getStatus():String = {
    status
  }

  def setStatus(status:String):Unit = {
    this.status = status
  }

  @XmlElement(name="shipDate")
  def getShipDate():Date = {
    shipDate
  }

  def setShipDate(shipDate:Date):Unit = {
    this.shipDate = shipDate
  }
}
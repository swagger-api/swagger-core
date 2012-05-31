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

import java.util.List
import java.util.ArrayList

import javax.xml.bind.annotation._

@XmlRootElement(name = "pet")
class Pet() {
  private var id:Long = 0
  private var category:Category = null
  private var name:String = null
  private var photoUrls:List[String] = new ArrayList[String]()
  private var tags:List[Tag] = new ArrayList[Tag]()
  private var status:String = null

  @XmlElement(name="id")
  def getId():Long = {
    id
  }

  def setId(id:Long):Unit = {
    this.id = id
  }
  
  @XmlElement(name="category")
  def getCategory():Category = {
    category
  }

  def setCategory(category:Category):Unit = {
    this.category = category
  }

  @XmlElement(name="name")
  def getName():String = {
    name
  }

  def setName(name:String):Unit = {
    this.name = name
  }

  @XmlElement(name="photoUrls")
  def getPhotoUrls():List[String] = {
    photoUrls
  }

  def setPhotoUrls(photoUrls:List[String]):Unit = {
    this.photoUrls = photoUrls
  }

  @XmlElement(name="tags")
  def getTags():List[Tag] = {
    tags
  }

  def setTags(tags:List[Tag]):Unit = {
    this.tags = tags
  }

  @XmlElement(name="status")
  @ApiProperty(value = "pet status in the store", allowableValues = "available,pending,sold")
  def getStatus():String = {
    status
  }

  def setStatus(status:String):Unit = {
    this.status = status
  }
}
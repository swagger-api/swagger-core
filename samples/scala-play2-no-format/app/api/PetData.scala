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

package api

import models._

import java.util.ArrayList

import scala.collection.mutable.ListBuffer
import scala.collection.JavaConversions._

class PetData {
  val pets: ListBuffer[Pet] = new ListBuffer[Pet]()
  val categories: ListBuffer[Category] = new ListBuffer[Category]()

  {
    categories += createCategory(1, "Dogs")
    categories += createCategory(2, "Cats")
    categories += createCategory(3, "Rabbits")
    categories += createCategory(4, "Lions")

    pets += createPet(1, categories(1), "Cat 1", List("url1", "url2"), List("tag1", "tag2"), "available")
    pets += createPet(2, categories(1), "Cat 2", List("url1", "url2"), List("tag2", "tag3"), "available")
    pets += createPet(3, categories(1), "Cat 3", List("url1", "url2"), List("tag3", "tag4"), "pending")

    pets += createPet(4, categories(0), "Dog 1", List("url1", "url2"), List("tag1", "tag2"), "available")
    pets += createPet(5, categories(0), "Dog 2", List("url1", "url2"), List("tag2", "tag3"), "sold")
    pets += createPet(6, categories(0), "Dog 3", List("url1", "url2"), List("tag3", "tag4"), "pending")

    pets += createPet(7, categories(3), "Lion 1", List("url1", "url2"), List("tag1", "tag2"), "available")
    pets += createPet(8, categories(3), "Lion 2", List("url1", "url2"), List("tag2", "tag3"), "available")
    pets += createPet(9, categories(3), "Lion 3", List("url1", "url2"), List("tag3", "tag4"), "available")

    pets += createPet(10, categories(2), "Rabbit 1", List("url1", "url2"), List("tag3", "tag4"), "available")

  }

  def getPetbyId(petId: Long): Option[Pet] = {
    pets.filter(pet => pet.getId == petId) match {
      case pets if(pets.size) > 0 => Some(pets.head)
      case _ => None
    }
  }

  def findPetByStatus(status: String): java.util.List[Pet] = {
    var statues = status.split(",")
    var result = new java.util.ArrayList[Pet]()
    for (pet <- pets) {
      if (statues.contains(pet.getStatus())) {
        result.add(pet)
      }
    }
    result
  }

  def findPetByTags(tags: String): java.util.List[Pet] = {
    var tagList = tags.split(",")
    var result = new java.util.ArrayList[Pet]()
    for (pet <- pets) {
      if (null != pet.getTags()) {
        for (tag <- pet.getTags()) {
          if (tagList.contains(tag.getName)) {
            result.add(pet)
          }
        }
      }
    }
    result
  }

  def addPet(pet: Pet): Unit = {
    pets --= pets.filter(existing => existing.getId == pet.getId)
    pets += pet
  }

  private def createPet(id: Long, cat: Category, name: String, urls: List[String], tags: List[String], status: String): Pet = {
    val pet = new Pet
    pet.setId(id)
    pet.setCategory(cat)
    pet.setName(name)
    if (null != urls) {
      var urlObjs: java.util.List[String] = new java.util.ArrayList[String]()
      for (urlString <- urls) {
        urlObjs.add(urlString)
      }
      pet.setPhotoUrls(urlObjs)
    }
    var tagObjs: java.util.List[Tag] = new java.util.ArrayList[Tag]()
    var i = 0
    if (null != tags) {
      for (tagString <- tags) {
        i = i + 1
        val tag = new Tag()
        tag.setId(i)
        tag.setName(tagString)
        tagObjs.add(tag)
      }
    }
    pet.setTags(tagObjs)
    pet.setStatus(status)
    pet
  }

  private def createCategory(id: Long, name: String): Category = {
    val category = new Category
    category.setId(id)
    category.setName(name)
    category
  }
}

class PetStoreApi {

}
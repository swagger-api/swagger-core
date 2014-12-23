/**
 *  Copyright 2014 Reverb Technologies, Inc.

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
    categories += Category(1, "Dogs")
    categories += Category(2, "Cats")
    categories += Category(3, "Rabbits")
    categories += Category(4, "Lions")

    pets += Pet(1, categories(1), "Cat 1", List("url1", "url2"), List(Tag(1, "tag1"), Tag(2, "tag2")), "available")
    pets += Pet(2, categories(1), "Cat 2", List("url1", "url2"), List(Tag(2, "tag2"), Tag(3, "tag3")), "available")
    pets += Pet(3, categories(1), "Cat 3", List("url1", "url2"), List(Tag(3, "tag3"), Tag(4, "tag4")), "pending")

    pets += Pet(4, categories(0), "Dog 1", List("url1", "url2"), List(Tag(1, "tag1"), Tag(2, "tag2")), "available")
    pets += Pet(5, categories(0), "Dog 2", List("url1", "url2"), List(Tag(2, "tag2"), Tag(3, "tag3")), "sold")
    pets += Pet(6, categories(0), "Dog 3", List("url1", "url2"), List(Tag(3, "tag3"), Tag(4, "tag4")), "pending")

    pets += Pet(7, categories(3), "Lion 1", List("url1", "url2"), List(Tag(1, "tag1"), Tag(2, "tag2")), "available")
    pets += Pet(8, categories(3), "Lion 2", List("url1", "url2"), List(Tag(2, "tag2"), Tag(3, "tag3")), "available")
    pets += Pet(9, categories(3), "Lion 3", List("url1", "url2"), List(Tag(3, "tag3"), Tag(4, "tag4")), "available")

    pets += Pet(10, categories(2), "Rabbit 1", List("url1", "url2"), List(Tag(3, "tag3"), Tag(4, "tag4")), "available")

  }

  def getPetbyId(petId: Long): Option[Pet] = {
    pets.filter(pet => pet.id == petId) match {
      case pets if(pets.size) > 0 => Some(pets.head)
      case _ => None
    }
  }

  def findPetByStatus(status: String): java.util.List[Pet] = {
    var statues = status.split(",")
    var result = new java.util.ArrayList[Pet]()
    for (pet <- pets) {
      if (statues.contains(pet.status)) {
        result.add(pet)
      }
    }
    result
  }

  def findPetByTags(tags: String): java.util.List[Pet] = {
    var tagList = tags.split(",")
    var result = new java.util.ArrayList[Pet]()
    for (pet <- pets) {
      if (null != pet.tags) {
        for (tag <- pet.tags) {
          if (tagList.contains(tag.name)) {
            result.add(pet)
          }
        }
      }
    }
    result
  }

  def addPet(pet: Pet): Unit = {
    pets --= pets.filter(existing => existing.id == pet.id)
    pets += pet
  }
}

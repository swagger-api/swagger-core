/**
 *  Copyright 2015 Reverb Technologies, Inc.
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

package com.wordnik.swagger.sample.data;

import com.wordnik.swagger.sample.model.*;

import java.util.*;

public class PetData {
  static List<Pet> pets = new ArrayList<Pet>();
  static List<Category> categories = new ArrayList<Category>();

  static {
    categories.add(createCategory(1, "Dogs"));
    categories.add(createCategory(2, "Cats"));
    categories.add(createCategory(3, "Rabbits"));
    categories.add(createCategory(4, "Lions"));

    pets.add(createPet(1, categories.get(1), "Cat 1", new String[] {
        "url1", "url2" }, new String[] { "tag1", "tag2" }, "available"));
    pets.add(createPet(2, categories.get(1), "Cat 2", new String[] {
        "url1", "url2" }, new String[] { "tag2", "tag3" }, "available"));
    pets.add(createPet(3, categories.get(1), "Cat 3", new String[] {
        "url1", "url2" }, new String[] { "tag3", "tag4" }, "pending"));

    pets.add(createPet(4, categories.get(0), "Dog 1", new String[] {
        "url1", "url2" }, new String[] { "tag1", "tag2" }, "available"));
    pets.add(createPet(5, categories.get(0), "Dog 2", new String[] {
        "url1", "url2" }, new String[] { "tag2", "tag3" }, "sold"));
    pets.add(createPet(6, categories.get(0), "Dog 3", new String[] {
        "url1", "url2" }, new String[] { "tag3", "tag4" }, "pending"));

    pets.add(createPet(7, categories.get(3), "Lion 1", new String[] {
        "url1", "url2" }, new String[] { "tag1", "tag2" }, "available"));
    pets.add(createPet(8, categories.get(3), "Lion 2", new String[] {
        "url1", "url2" }, new String[] { "tag2", "tag3" }, "available"));
    pets.add(createPet(9, categories.get(3), "Lion 3", new String[] {
        "url1", "url2" }, new String[] { "tag3", "tag4" }, "available"));

    pets.add(createPet(10, categories.get(2), "Rabbit 1", new String[] {
        "url1", "url2" }, new String[] { "tag3", "tag4" }, "available"));
  }

  public Pet getPetbyId(long petId) {
    for (Pet pet : pets) {
      if (pet.getId() == petId) {
        return pet;
      }
    }
    return null;
  }

  public void deletePet(long petId) {
    if(pets.size() > 0) {
      for (int i = pets.size() - 1; i >= 0; i--) {
        Pet pet = pets.get(i);
        if(pet.getId() == petId) {
          pets.remove(i);
        }
      }
    }
  }

  public List<Pet> findPetByStatus(String status) {
    List<Pet> result = new java.util.ArrayList<Pet>();
    if(status == null) {
      return result;
    }
    String[] statuses = status.split(",");
    for (Pet pet : pets) {
      for (String s : statuses) {
        if (s.equals(pet.getStatus())) {
          result.add(pet);
        }
      }
    }
    return result;
  }

  public List<Pet> findPetByTags(String tags) {
    String[] tagList = tags.split(",");
    List<Pet> result = new java.util.ArrayList<Pet>();
    for (Pet pet : pets) {
      if (null != pet.getTags()) {
        for (Tag tag : pet.getTags()) {
          for (String tagListString : tagList) {
            if (tagListString.equals(tag.getName()))
              result.add(pet);
          }
        }
      }
    }
    return result;
  }

  public Pet addPet(Pet pet) {
    if(pet.getId() == 0) {
      long maxId = 0;
      for (int i = pets.size() - 1; i >= 0; i--) {
        if(pets.get(i).getId() > maxId) {
          maxId = pets.get(i).getId();
        }
      }
      pet.setId(maxId + 1);
    }
    if (pets.size() > 0) {
      for (int i = pets.size() - 1; i >= 0; i--) {
        if (pets.get(i).getId() == pet.getId()) {
          pets.remove(i);
        }
      }
    }
    pets.add(pet);
    return pet;
  }

  public Map<String, Integer> getInventoryByStatus() {
    Map<String, Integer> output = new HashMap<String, Integer>();
    for(Pet pet : pets) {
      String status = pet.getStatus();
      if(status != null && !"".equals(status)) {
        Integer count = output.get(status);
        if(count == null)
          count = new Integer(1);
        else
          count = count.intValue() + 1;
        output.put(status, count);
      }
    }
    return output;
  }

  static Pet createPet(long id, Category cat, String name, String[] urls,
      String[] tags, String status) {
    Pet pet = new Pet();
    pet.setId(id);
    pet.setCategory(cat);
    pet.setName(name);
    if (null != urls) {
      List<String> urlObjs = new ArrayList<String>();
      for (String urlString : urls) {
        urlObjs.add(urlString);
      }
      pet.setPhotoUrls(urlObjs);
    }
    List<Tag> tagObjs = new java.util.ArrayList<Tag>();
    int i = 0;
    if (null != tags) {
      for (String tagString : tags) {
        i = i + 1;
        Tag tag = new Tag();
        tag.setId(i);
        tag.setName(tagString);
        tagObjs.add(tag);
      }
    }
    pet.setTags(tagObjs);
    pet.setStatus(status);
    return pet;
  }

  static Category createCategory(long id, String name) {
    Category category = new Category();
    category.setId(id);
    category.setName(name);
    return category;
  }
}
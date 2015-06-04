/**
 *  Copyright 2015 SmartBear Software
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

import java.io.IOException
import java.net.URL
import scala.collection.JavaConversions._

import org.junit.runner.RunWith
import org.scalatest.FlatSpec
import org.scalatest.Matchers
import org.scalatest.junit.JUnitRunner

import com.fasterxml.jackson.databind.node.ObjectNode
import io.swagger.models.Swagger
import io.swagger.util.Json
import io.swagger.util.Yaml


@RunWith(classOf[JUnitRunner])
class ResourceListingIT extends FlatSpec with Matchers {
  val jsons = Array(loadJson(true), loadJson(false))
  val yamls = Array(loadYaml(true), loadYaml(false))

  def createURL(name: String, addKey: Boolean) = {
    new URL("http://localhost:8002/" + name + (if (addKey) "?api_key=special-key" else ""))
  }

  def loadJson(addKey: Boolean) = {
    Json.mapper().readValue(createURL("api/swagger.json", addKey), classOf[Swagger])
  }

  def loadYaml(addKey: Boolean) = {
    Yaml.mapper().readValue(createURL("api/swagger.yaml", addKey), classOf[Swagger])
  }

  def toNode(swagger: Swagger) = Json.mapper().convertValue(swagger, classOf[ObjectNode])

  def collectResources(swagger: Swagger) = {
    for (key <- swagger.getPaths().keySet()) yield key.stripPrefix("/").split("/")(0)
  }

  def collectAPI(swagger: Swagger, prefix: String) = {
    for (key <- swagger.getPaths().keySet() if key.stripPrefix("/").split("/")(0) == prefix) yield key
  }

  it should "check a resource listing" in {
    collectResources(jsons(0)) should equal(Set("pet", "user", "store"))
    collectResources(jsons(1)) should equal(Set("pet", "user"))
  }

  it should "check Pet API" in {
    collectAPI(jsons(0), "pet") should equal(Set("/pet", "/pet/{petId}", "/pet/findByStatus", "/pet/findByTags"))
    collectAPI(jsons(1), "pet") should equal(Set("/pet/{petId}", "/pet/findByStatus", "/pet/findByTags"))
  }

  it should "check Store API" in {
    collectAPI(jsons(0), "store") should equal(Set("/store/order", "/store/order/{orderId}"))
    collectAPI(jsons(1), "store") should equal(Set())
  }

  it should "check User API" in {
    collectAPI(jsons(0), "user") should equal(Set("/user", "/user/createWithArray", "/user/createWithList", "/user/login", "/user/logout", "/user/{username}"))
    collectAPI(jsons(1), "user") should equal(Set("/user/login", "/user/logout", "/user/{username}"))
  }

  it should "compare JSON and YAML" in {
    toNode(jsons(0)) should equal(toNode(yamls(0)))
    toNode(jsons(1)) should equal(toNode(yamls(1)))
  }

  it should "check wheather UI scripts have been deployed" in {
    noException should be thrownBy {
      val ui = createURL("swagger-ui.js", false).openStream()
      ui.close()
    }
  }
}

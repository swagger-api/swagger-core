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

import java.net.URL


import com.wordnik.swagger.models.Swagger
import com.wordnik.swagger.util.Json

import org.junit.runner.RunWith

import org.scalatest.junit.JUnitRunner
import org.scalatest.FlatSpec
import org.scalatest.Matchers


@RunWith(classOf[JUnitRunner])
class ResourceListingIT extends FlatSpec with Matchers {
  it should "read a resource listing" in {
    val swagger = Json.mapper().readValue(new URL("http://localhost:8002/api/swagger.json"), classOf[Swagger])

    Json.prettyPrint(swagger)

    swagger.getHost() should be ("localhost:8002")
    swagger.getBasePath() should be ("/api")

    val info = swagger.getInfo()
    info should not be (null)
    info.getVersion() should be ("1.0.0")
    info.getTitle() should be ("Petstore sample app")
    info.getContact() should not be (null)
    info.getContact().getName() should be ("apiteam@swagger.io")
    info.getLicense() should not be (null)
    info.getLicense().getName() should be ("Apache 2.0 License")
  }
}

import com.wordnik.swagger.converter.ModelConverters
import models.Pet
import resources._
import matchers.SerializationMatchers._

import org.scalatest.{FlatSpec, Matchers}
import com.wordnik.swagger.models.properties.{RefProperty, MapProperty}

import com.wordnik.swagger.models.Swagger
import com.wordnik.swagger.jaxrs.Reader

import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner
import org.scalatest.FlatSpec
import org.scalatest.Matchers

@RunWith(classOf[JUnitRunner])
class ReferenceTest extends FlatSpec with Matchers {

  it should "Scan a model with common reference and reference with ApiModel" in {
    val props = ModelConverters.getInstance().readAll(classOf[Pet]).get("Pet").getProperties()
    val category = props.get("category").asInstanceOf[RefProperty]
    category.getType() should be ("ref")
    category.get$ref() should be ("#/definitions/Category")

    val categoryWithApiModel = props.get("categoryWithApiModel").asInstanceOf[RefProperty]
    categoryWithApiModel.getType() should be ("ref")
    categoryWithApiModel.get$ref() should be ("#/definitions/MyCategory")
  }

  it should "Scan API with operation and response references" in {
    val reader = new Reader(new Swagger())
    val swagger = reader.read(classOf[ResourceWithReferences])

    swagger should serializeToJson (
      """{
  "swagger": "2.0",
  "tags": [
    {
      "name": "basic"
    }
  ],
  "paths": {
    "/anotherModel": {
      "get": {
        "tags": [
          "basic"
        ],
        "summary": "Get Another Model",
        "description": "",
        "operationId": "getAnotherModel",
        "parameters": [],
        "responses": {
          "200": {
            "description": "successful operation",
            "schema": {
              "$ref": "http://swagger.io/schemas.json#/Models"
            }
          }
        }
      }
    },
    "/model": {
      "get": {
        "tags": [
          "basic"
        ],
        "summary": "Get Model",
        "description": "",
        "operationId": "getModel",
        "parameters": [],
        "responses": {
          "200": {
            "description": "successful operation",
            "schema": {
              "$ref": "#/definitions/ModelContainingModelWithReference"
            }
          }
        }
      }
    },
    "/some": {
      "get": {
        "tags": [
          "basic"
        ],
        "summary": "Get Some",
        "description": "",
        "operationId": "getSome",
        "parameters": [],
        "responses": {
          "200": {
            "description": "successful operation",
            "schema": {
              "$ref": "http://swagger.io/schemas.json#/Models/SomeResponse"
            }
          }
        }
      }
    },
    "/test": {
      "get": {
        "tags": [
          "basic"
        ],
        "operationId": "getTest",
        "parameters": [],
        "responses": {
          "500": {
            "description": "Error",
            "schema": {
              "$ref": "http://swagger.io/schemas.json#/Models/ErrorResponse"
            }
          }
        }
      }
    },
    "/testSome": {
      "get": {
        "tags": [
          "basic"
        ],
        "summary": "Get Some",
        "description": "",
        "operationId": "getTestSome",
        "parameters": [],
        "responses": {
          "200": {
            "description": "successful operation",
            "schema": {
              "$ref": "http://swagger.io/schemas.json#/Models/SomeResponse"
            }
          },
          "500": {
            "description": "Error",
            "schema": {
              "$ref": "http://swagger.io/schemas.json#/Models/ErrorResponse"
            }
          }
        }
      }
    }
  },
  "definitions": {
    "ModelWithReference": {
      "type": "object",
      "properties": {
        "description": {
          "$ref": "http://swagger.io/schemas.json#/Models/Description"
        }
      }
    },
    "ModelContainingModelWithReference": {
      "type": "object",
      "properties": {
        "model": {
          "$ref": "http://swagger.io/schemas.json#/Models"
        },
        "anotherModel": {
          "$ref": "http://swagger.io/schemas.json#/Models/AnotherModel"
        }
      }
    }
  }
}""")
  }
}

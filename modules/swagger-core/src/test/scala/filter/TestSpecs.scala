package filter

import converter.models._

import com.wordnik.swagger.converter._
import com.wordnik.swagger.core.filter._
import com.wordnik.swagger.core.SwaggerSpec
import com.wordnik.swagger.model._

import org.json4s._
import org.json4s.JsonDSL._
import org.json4s.jackson.JsonMethods._
import org.json4s.native.Serialization.{read, write}

object TestSpecs {
  implicit val formats = SwaggerSerializers.formats

  def ordered = {
    val apis = List(
      ApiDescription("/", None, List(
        Operation("GET", "does something", "notes", "void", "getSomething", 2),
        Operation("POST", "does something else", "notes", "void", "postSomething", 1)
        )
      )
    )
    ApiListing(
      apiVersion = "1.0.0",
      swaggerVersion = SwaggerSpec.version,
      basePath = "http://localhost:8080/api",
      resourcePath = "/myApi",
      apis = apis
    )
  }

  def subTypes = {
    val apis = List(
      ApiDescription("/", None, List(
        Operation("GET", "does something", "notes", "Animal", "getSomething", 2))
      )
    )
    val models = Some(ModelConverters.readAll(classOf[Animal]).map(m => m.name -> m).toMap)

    ApiListing(
      apiVersion = "1.0.0",
      swaggerVersion = SwaggerSpec.version,
      basePath = "http://localhost:8080/api",
      resourcePath = "/myApi",
      apis = apis,
      models = models
    )
  }

  def getSimple = {
    val str = 
"""
{
  "apiVersion": "1.0",
  "swaggerVersion": "1.2",
  "basePath": "http://petstore.swagger.wordnik.com/api",
  "resourcePath": "/pet",
  "apis": [
    {
      "path": "/pet.{format}",
      "description": "Operations about pets",
      "operations": [
        {
          "method": "POST",
          "summary": "Add a new pet to the store",
          "responseClass": "void",
          "nickname": "addPet",
          "parameters": [
            {
              "description": "Pet object that needs to be added to the store",
              "paramType": "body",
              "required": true,
              "allowMultiple": false,
              "dataType": "Pet",
              "paramAccess": "secret"
            }
          ],
          "errorResponses": [
            {
              "code": 405,
              "reason": "Invalid input"
            }
          ]
        },
        {
          "method": "PUT",
          "summary": "Update an existing pet",
          "responseClass": "void",
          "nickname": "updatePet",
          "parameters": [
            {
              "description": "Pet object that needs to be updated in the store",
              "paramType": "body",
              "required": true,
              "allowMultiple": false,
              "dataType": "Pet"
            }
          ],
          "errorResponses": [
            {
              "code": 400,
              "reason": "Invalid ID supplied"
            },
            {
              "code": 404,
              "reason": "Pet not found"
            },
            {
              "code": 405,
              "reason": "Validation exception"
            }
          ]
        }
      ]
    },
    {
      "path": "/pet.{format}/{petId}",
      "description": "Operations about pets",
      "operations": [
        {
          "method": "GET",
          "summary": "Find pet by ID",
          "notes": "Returns a pet based on ID",
          "responseClass": "Pet",
          "nickname": "getPetById",
          "parameters": [
            {
              "name": "petId",
              "description": "ID of pet that needs to be fetched",
              "paramType": "path",
              "required": true,
              "allowMultiple": false,
              "dataType": "string"
            }
          ],
          "errorResponses": [
            {
              "code": 400,
              "reason": "Invalid ID supplied"
            },
            {
              "code": 404,
              "reason": "Pet not found"
            }
          ]
        }
      ]
    },
    {
      "path": "/pet.{format}/findByStatus",
      "description": "Operations about pets",
      "operations": [
        {
          "method": "GET",
          "summary": "Finds Pets by status",
          "notes": "Multiple status values can be provided with comma seperated strings",
          "responseClass": "List[Pet]",
          "nickname": "findPetsByStatus",
          "parameters": [
            {
              "name": "status",
              "description": "Status values that need to be considered for filter",
              "paramType": "query",
              "defaultValue": "available",
              "allowableValues": {
                "valueType": "LIST",
                "values": [
                  "available",
                  "pending",
                  "sold"
                ]
              },
              "required": true,
              "allowMultiple": true,
              "dataType": "string"
            }
          ],
          "errorResponses": [
            {
              "code": 400,
              "reason": "Invalid status value"
            }
          ]
        }
      ]
    },
    {
      "path": "/pet.{format}/findByTags",
      "description": "Operations about pets",
      "operations": [
        {
          "method": "GET",
          "summary": "Finds Pets by tags",
          "notes": "Muliple tags can be provided with comma seperated strings. Use tag1, tag2, tag3 for testing.",
          "deprecated": true,
          "responseClass": "List[Pet]",
          "nickname": "findPetsByTags",
          "parameters": [
            {
              "name": "tags",
              "description": "Tags to filter by",
              "paramType": "query",
              "required": true,
              "allowMultiple": true,
              "dataType": "string"
            }
          ],
          "errorResponses": [
            {
              "code": 400,
              "reason": "Invalid tag value"
            }
          ]
        }
      ]
    }
  ],
  "models": {
    "Category": {
      "id": "Category",
      "properties": {
        "id": {
          "type": "long"
        },
        "name": {
          "type": "string"
        }
      }
    },
    "Pet": {
      "id": "Pet",
      "properties": {
        "tags": {
          "items": {
            "$ref": "Tag"
          },
          "type": "Array"
        },
        "id": {
          "type": "long"
        },
        "category": {
          "type": "Category"
        },
        "status": {
          "allowableValues": {
            "valueType": "LIST",
            "values": [
              "available",
              "pending",
              "sold"
            ]
          },
          "description": "pet status in the store",
          "type": "string"
        },
        "name": {
          "type": "string"
        },
        "photoUrls": {
          "items": {
            "type": "string"
          },
          "type": "Array"
        }
      }
    },
    "Tag": {
      "id": "Tag",
      "properties": {
        "id": {
          "type": "long"
        },
        "name": {
          "type": "string"
        }
      }
    }
  }
}
"""
    parse(str).extract[ApiListing]
  }

  def getReturnTypeWithList = {
    val str = 
"""
{
  "apiVersion": "1.0",
  "swaggerVersion": "1.2",
  "basePath": "http://petstore.swagger.wordnik.com/api",
  "resourcePath": "/pet",
  "apis": [
    {
      "path": "/houseParts",
      "description": "Operations about house parts",
      "operations": [
        {
          "method": "GET",
          "summary": "gets a window",
          "responseClass": "Window",
          "nickname": "getWindow",
          "parameters": [
            {
              "description": "limit",
              "paramType": "query",
              "required": true,
              "allowMultiple": false,
              "dataType": "int"
            }
          ],
          "errorResponses": [
            {
              "code": 405,
              "reason": "Invalid input"
            }
          ]
        }
      ]
    }
  ],
  "models": {
    "Window": {
      "id": "Window",
      "properties": {
        "tags": {
          "items": {
            "$ref": "Latch"
          },
          "type": "List"
        }
      }
    },
    "Latch": {
      "id": "Latch1",
      "properties": {
        "tags": {
          "items": {
            "$ref": "Tag"
          },
          "type": "Array"
        },
        "id": {
          "type": "long"
        },
        "name": {
          "type": "string"
        }
      }
    }
  }
}
"""
    parse(str).extract[ApiListing]
  }
}
package filter

import com.wordnik.swagger.core.util.ScalaJsonUtil
import com.wordnik.swagger.core.filter._
import com.wordnik.swagger.model._

import org.json4s._
import org.json4s.JsonDSL._
import org.json4s.jackson.JsonMethods._
import org.json4s.native.Serialization.{read, write}

import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner
import org.scalatest.FlatSpec
import org.scalatest.matchers.ShouldMatchers

@RunWith(classOf[JUnitRunner])
class SpecFilterTest extends FlatSpec with ShouldMatchers {
  implicit val formats = SwaggerSerializers.formats

  behavior of "SpecFilter"

  it should "filter an api spec and return all models" in {
    val spec = TestSpecs.getSimple
    val p = new SpecFilter().filter(spec, new SimpleFilter, Map(), Map(), Map())
    p.apis.size should be (4)
    (p.models.get.keys.toSet & Set("Pet", "Category", "Tag")).size should be (3)
  }

  it should "filter away all non-get operations" in {
    val spec = TestSpecs.getSimple
    val p = new SpecFilter().filter(spec, new GetOnlyFilter, Map(), Map(), Map())
    p.apis.size should be (3)
    (p.models.get.keys.toSet & Set("Pet", "Category", "Tag")).size should be (3)
  }

  it should "filter away everything" in {
    val spec = TestSpecs.getSimple
    val p = new SpecFilter().filter(spec, new EatEverythingFilter, Map(), Map(), Map())
    p.apis.size should be (0)
    p.models should be (None)
  }

  it should "filter away secret params" in {
    val spec = TestSpecs.getSimple
    val p = new SpecFilter().filter(spec, new SecretParamFilter, Map(), Map(), Map())

    p.apis.foreach(api => {
      if(api.path == "/pet.{format}") {
        api.operations.foreach(op => {
          if(op.httpMethod == "POST") {
            op.parameters.size should be (0)
          }
        })
      }
    })
  }
}

class SimpleFilter extends SwaggerSpecFilter {
  override def isOperationAllowed(operation: Operation, api: ApiDescription, params: java.util.Map[String, java.util.List[String]], cookies: java.util.Map[String, String], headers: java.util.Map[String, java.util.List[String]]): Boolean = true
  override def isParamAllowed(parameter: Parameter, operation: Operation, api: ApiDescription, params: java.util.Map[String, java.util.List[String]], cookies: java.util.Map[String, String], headers: java.util.Map[String, java.util.List[String]]): Boolean = true
}

class GetOnlyFilter extends SwaggerSpecFilter {
  override def isOperationAllowed(operation: Operation, api: ApiDescription, params: java.util.Map[String, java.util.List[String]], cookies: java.util.Map[String, String], headers: java.util.Map[String, java.util.List[String]]): Boolean = {
    if(operation.httpMethod != "GET") false
    else true
  }
  override def isParamAllowed(parameter: Parameter, operation: Operation, api: ApiDescription, params: java.util.Map[String, java.util.List[String]], cookies: java.util.Map[String, String], headers: java.util.Map[String, java.util.List[String]]): Boolean = true
}

class EatEverythingFilter extends SwaggerSpecFilter {
  override def isOperationAllowed(operation: Operation, api: ApiDescription, params: java.util.Map[String, java.util.List[String]], cookies: java.util.Map[String, String], headers: java.util.Map[String, java.util.List[String]]): Boolean = false
  override def isParamAllowed(parameter: Parameter, operation: Operation, api: ApiDescription, params: java.util.Map[String, java.util.List[String]], cookies: java.util.Map[String, String], headers: java.util.Map[String, java.util.List[String]]): Boolean = true
}

class SecretParamFilter extends SwaggerSpecFilter {
  override def isOperationAllowed(operation: Operation, api: ApiDescription, params: java.util.Map[String, java.util.List[String]], cookies: java.util.Map[String, String], headers: java.util.Map[String, java.util.List[String]]): Boolean = true
  override def isParamAllowed(parameter: Parameter, operation: Operation, api: ApiDescription, params: java.util.Map[String, java.util.List[String]], cookies: java.util.Map[String, String], headers: java.util.Map[String, java.util.List[String]]): Boolean = {
    if(parameter.paramAccess == Some("secret")) false
    else true
  }
}

object TestSpecs {
  implicit val formats = SwaggerSerializers.formats

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
          "httpMethod": "POST",
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
          "httpMethod": "PUT",
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
          "httpMethod": "GET",
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
          "httpMethod": "GET",
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
          "httpMethod": "GET",
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
    val json = parse(str)
    json.extract[ApiListing]
  }
}
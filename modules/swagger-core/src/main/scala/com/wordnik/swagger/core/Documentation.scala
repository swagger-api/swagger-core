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

package com.wordnik.swagger.core

import com.fasterxml.jackson.annotation.{JsonProperty, JsonIgnore}
import com.fasterxml.jackson.databind.annotation.JsonSerialize

import java.util.HashMap
import javax.xml.bind.annotation._

import scala.reflect.BeanProperty
import scala.collection._
import scala.collection.mutable.ListBuffer
import scala.collection.JavaConversions._

trait Name {
  private var name: String = _
  @XmlElement(name = "name")
  @JsonProperty(value = "name")
  def getName: String = name

  @JsonProperty(value = "name")
  def setName(name: String) = this.name = name
}

@JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
@XmlRootElement(name = "ApiDocumentation")
@XmlAccessorType(XmlAccessType.NONE)
class Documentation(var apiVersion: String,
  var swaggerVersion: String,
  var basePath: String,
  var resourcePath: String) {
  def this() = this(null, null, null, null)

  @XmlElement
  def getApiVersion = apiVersion
  def setApiVersion(apiVersion: String) = this.apiVersion = apiVersion

  @XmlElement
  def getSwaggerVersion = swaggerVersion
  def setSwaggerVersion(swaggerVersion: String) = this.swaggerVersion = swaggerVersion

  @XmlElement
  def getBasePath = basePath
  def setBasePath(basePath: String) = this.basePath = basePath

  @XmlElement
  def getResourcePath = resourcePath
  def setResourcePath(resourcePath: String) = this.resourcePath = resourcePath

  private var apis = new ListBuffer[DocumentationEndPoint]

  @XmlElement(name = "apis")
  def getApis(): java.util.List[DocumentationEndPoint] = apis.size match {
    case 0 => null
    case _ => apis
  }

  def setApis(ep: java.util.List[DocumentationEndPoint]) = {
    this.apis.clear()
    if (ep != null) for (n <- ep) apis += n
  }

  def addApi(ep: DocumentationEndPoint) = if (ep != null) apis += ep
  def removeApi(ep: DocumentationEndPoint) = if (ep != null) apis -= ep

  private var models = new java.util.HashMap[String, DocumentationSchema]

  @JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
  @XmlElement(name = "models")
  def getModels(): java.util.HashMap[String, DocumentationSchema] = models.size() match {
    case 0 => null
    case _ => models
  }

  def setModels(sch: java.util.HashMap[String, DocumentationSchema]) = {
    this.models.clear()
    if (sch != null) sch.foreach(schema => models += schema)
  }

  def addModel(propertyName: String, obj: DocumentationSchema) = {
    //the property name is some times studely and some times lower, so make sure it is always studely
    if (propertyName != null && obj != null) models += propertyName.capitalize -> obj
  }

  override def clone(): Object = {
    var doc = new Documentation(apiVersion, swaggerVersion, basePath, resourcePath)
    apis.foreach(ep => doc.addApi((ep.clone()).asInstanceOf[DocumentationEndPoint]))
    for ((name, model) <- models) doc.addModel(name, (model.clone().asInstanceOf[DocumentationSchema]))
    doc
  }
}

@JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
@XmlRootElement(name = "api")
class DocumentationEndPoint(@BeanProperty var path: String, @BeanProperty var description: String) {
  def this() = this(null, null)
  private var ops = new ListBuffer[DocumentationOperation]

  @XmlElement
  def getOperations(): java.util.List[DocumentationOperation] = ops.size match {
    case 0 => null
    case _ => ops
  }

  def setOperations(ep: java.util.List[DocumentationOperation]) = {
    this.ops.clear()
    if (ep != null) ep.foreach(n => ops += n)
  }

  def addOperation(op: DocumentationOperation) = if (op != null) ops += op
  def removeOperation(op: DocumentationOperation) = if (op != null) ops -= op

  override def clone(): Object = {
    var ep = new DocumentationEndPoint(path, description)
    ops.foreach(op => ep.addOperation((op.clone()).asInstanceOf[DocumentationOperation]))
    ep
  }
}

@JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
@XmlRootElement(name = "operation")
class DocumentationOperation(
  @BeanProperty var httpMethod: String,
  @BeanProperty var summary: String,
  @BeanProperty var notes: String) {
  @BeanProperty var deprecated: java.lang.Boolean = null
  @BeanProperty var responseClass: String = _
  @BeanProperty var nickname: String = _

  def this() = this(null, null, null)

  private var parameters = new ListBuffer[DocumentationParameter]

  @XmlElement
  def getParameters(): java.util.List[DocumentationParameter] = parameters.size match {
    case 0 => null
    case _ => parameters
  }

  def setParameters(ep: java.util.List[DocumentationParameter]) = {
    this.parameters.clear()
    if (ep != null) ep.foreach(n => parameters += n)
  }
  def addParameter(param: DocumentationParameter) = parameters += param

  private val tags = new ListBuffer[String]

  @XmlElement
  def getTags(): java.util.List[String] = tags.size match {
    case 0 => null
    case _ => tags
  }

  def setTags(tagList: java.util.List[String]) = {
    this.tags.clear()
    if (tagList != null && tagList.iterator != null) tagList.foreach(tag => tags += tag)
  }

  private var responseTypeInternal: String = _

  def setResponseTypeInternal(s: String) = this.responseTypeInternal = s

  @XmlTransient
  @JsonIgnore
  def getResponseTypeInternal() = this.responseTypeInternal

  private var errorResponses = new ListBuffer[DocumentationError]

  @XmlElement
  def getErrorResponses(): java.util.List[DocumentationError] = errorResponses.size match {
    case 0 => null
    case _ => errorResponses
  }

  def setErrorResponses(ep: java.util.List[DocumentationError]) = {
    this.errorResponses.clear()
    if (ep != null) ep.foreach(n => errorResponses += n)
  }

  def addErrorResponse(error: DocumentationError) = if (error != null) errorResponses += error

  override def clone(): Object = {
    val cloned = new DocumentationOperation(httpMethod, summary, notes)
    cloned.deprecated = deprecated;
    cloned.nickname = nickname;
    cloned.responseClass = responseClass;
    cloned.responseTypeInternal = this.responseTypeInternal
    cloned.setTags(this.tags)

    parameters.foreach(p => cloned.addParameter((p.clone()).asInstanceOf[DocumentationParameter]))
    errorResponses.foreach(er => cloned.addErrorResponse((er.clone()).asInstanceOf[DocumentationError]))
    cloned
  }

  def cloneExternal(): DocumentationOperation = {
    val cloned = new DocumentationOperation(httpMethod, summary, notes)
    cloned.deprecated = deprecated;
    cloned.nickname = nickname;
    cloned.responseClass = responseClass;
    cloned.responseTypeInternal = this.responseTypeInternal
    cloned.setTags(this.tags)
    cloned.setErrorResponses(this.errorResponses)

    parameters.foreach(p => {
      if ("internal" != p.paramAccess) cloned.addParameter((p.clone()).asInstanceOf[DocumentationParameter])
    })
    cloned
  }
}

@JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
@XmlRootElement(name = "parameter")
class DocumentationParameter(
  @BeanProperty var name: String,
  @BeanProperty var description: String,
  @BeanProperty var notes: String,
  @BeanProperty var paramType: String,
  @BeanProperty var defaultValue: String,
  @BeanProperty var allowableValues: DocumentationAllowableValues,
  @BeanProperty var required: Boolean,
  @BeanProperty var allowMultiple: Boolean) {
  @BeanProperty var paramAccess: String = _
  @BeanProperty var internalDescription: String = _
  @BeanProperty var wrapperName: String = _
  @BeanProperty var dataType: String = _

  def this() = this(null, null, null, null, null, null, false, false)

  private var valueTypeInternal: String = _

  def setValueTypeInternal(s: String) = this.valueTypeInternal = s

  @XmlTransient
  @JsonIgnore
  def getValueTypeInternal() = this.valueTypeInternal

  override def clone(): Object = {
    var clonedAllowValues: DocumentationAllowableValues = null;
    if (null != allowableValues) {
      clonedAllowValues = allowableValues.copy()
    }
    val cloned = new DocumentationParameter(name, description, notes, paramType, defaultValue, clonedAllowValues,
      required, allowMultiple)

    cloned.paramAccess = paramAccess
    cloned.internalDescription = internalDescription
    cloned.wrapperName = wrapperName
    cloned.dataType = dataType
    cloned.valueTypeInternal = this.valueTypeInternal

    cloned
  }
}

//TODO - remove this Response class entirely as it is now catured in Operation
@JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
@XmlRootElement(name = "response")
class DocumentationResponse(
  @BeanProperty var valueType: String,
  @BeanProperty var occurs: String) {
  def this() = this(null, null)

  private var valueTypeInternal: String = _
  @XmlTransient
  def getValueTypeInternal() = this.valueTypeInternal
  def setValueTypeInternal(s: String) = this.valueTypeInternal = s

  private var errorResponses = new ListBuffer[DocumentationError]

  @XmlElement
  def getErrorResponses(): java.util.List[DocumentationError] = errorResponses.size match {
    case 0 => null
    case _ => errorResponses
  }
  def setErrorResponses(ep: java.util.List[DocumentationError]) = {
    this.errorResponses.clear()
    if (ep != null) ep.foreach(n => errorResponses += n)
  }

  def addErrorResponse(error: DocumentationError) = if (error != null) errorResponses += error

  override def clone(): Object = {
    var cloned = new DocumentationResponse(valueType, occurs)
    cloned.valueTypeInternal = this.valueTypeInternal
    errorResponses.foreach(er => cloned.addErrorResponse((er.clone()).asInstanceOf[DocumentationError]))
    cloned
  }
}

@JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
@XmlRootElement(name = "error")
class DocumentationError(@BeanProperty var code: Int, @BeanProperty var reason: String) {
  def this() = this(0, null)

  override def clone(): Object = {
    new DocumentationError(code, reason)
  }
}

@JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
@XmlRootElement(name = "docObject")
class DocumentationObject extends Name {
  @BeanProperty
  var uniqueFieldName: String = _
  private var fields = new ListBuffer[DocumentationParameter]

  def getFields(): java.util.List[DocumentationParameter] = fields.size match {
    case 0 => null
    case _ => fields
  }

  def setFields(ep: java.util.List[DocumentationParameter]) = {
    this.fields.clear()
    if (ep != null) ep.foreach(n => fields += n)
  }

  def addField(field: DocumentationParameter) = if (fields != null) fields += field

  override def clone(): Object = {
    var cloned = new DocumentationObject()
    fields.foreach(f => cloned.addField((f.clone()).asInstanceOf[DocumentationParameter]))
    cloned
  }

  def toDocumentationSchema(): DocumentationSchema = {
    val schemaObject = new DocumentationSchema()
    schemaObject.id = this.getName

    if (fields.length > 0) schemaObject.properties = new HashMap[String, DocumentationSchema]() else return null

    fields.foreach(currentField => {
      if (null != currentField.paramType) {
        val fieldSchema: DocumentationSchema = new DocumentationSchema()
        setSchemaTypeDef(currentField, fieldSchema)
        fieldSchema.required = currentField.required
        fieldSchema.description = currentField.description
        fieldSchema.notes = currentField.notes
        fieldSchema.access = currentField.paramAccess

        if (currentField.allowableValues != null) {
          fieldSchema.allowableValues = currentField.allowableValues
        }

        val useWrapperName = currentField.getWrapperName != null && currentField.getWrapperName.trim().length() > 0
        if (useWrapperName)
          schemaObject.properties.put(currentField.getWrapperName(), fieldSchema)
        else
          schemaObject.properties.put(currentField.getName, fieldSchema)
      }
    })

    schemaObject
  }

  private def setSchemaTypeDef(currentField: DocumentationParameter, currentSchema: DocumentationSchema) = {
    val isList = currentField.paramType.startsWith("List[");
    val isSet = currentField.paramType.startsWith("Set[");

    if (isList || isSet) {
      currentSchema.setType("Array")
      currentSchema.uniqueItems = isSet;
      val arrayElementType = currentField.paramType.substring(currentField.paramType.indexOf("[") + 1, currentField.paramType.indexOf("]"))
      val arrayItem = new DocumentationSchema
      if (currentSchema.simpleTypeList.find(item => arrayElementType.equals(item)) != None) {
        arrayItem.setType(arrayElementType)
      } else {
        arrayItem.ref = arrayElementType
      }
      currentSchema.items = arrayItem
    } else {
      currentSchema.setType(currentField.paramType)
    }
  }
}

@JsonSerialize(include = JsonSerialize.Inclusion.NON_DEFAULT)
@XmlRootElement(name = "model")
class DocumentationSchema {
  private var objectType: String = "any"

  @JsonSerialize(include = JsonSerialize.Inclusion.NON_DEFAULT)
  @JsonProperty(value = "type")
  @XmlElement(defaultValue = "any")
  def getType: String = this.objectType
  def setType(objectType: String) = this.objectType = objectType

  @JsonSerialize(include = JsonSerialize.Inclusion.NON_DEFAULT)
  @BeanProperty
  var required: Boolean = false

  @XmlTransient
  @JsonIgnore(value = true)
  @BeanProperty
  var name: String = null

  @BeanProperty
  var id: String = null

  @BeanProperty
  var properties: java.util.Map[String, DocumentationSchema] = null

  @JsonSerialize(include = JsonSerialize.Inclusion.NON_DEFAULT)
  @BeanProperty
  var allowableValues: DocumentationAllowableValues = null

  @JsonSerialize(include = JsonSerialize.Inclusion.NON_DEFAULT)
  @BeanProperty
  var description: String = null

  @JsonSerialize(include = JsonSerialize.Inclusion.NON_DEFAULT)
  @BeanProperty
  var notes: String = null

  @JsonSerialize(include = JsonSerialize.Inclusion.NON_DEFAULT)
  @BeanProperty
  var access: String = null

  @JsonSerialize(include = JsonSerialize.Inclusion.NON_DEFAULT)
  @BeanProperty
  var default: String = null //TODO this should be object

  @JsonSerialize(include = JsonSerialize.Inclusion.NON_DEFAULT)
  @BeanProperty
  var additionalProperties: DocumentationSchema = null

  @JsonSerialize(include = JsonSerialize.Inclusion.NON_DEFAULT)
  @BeanProperty
  var items: DocumentationSchema = null

  /*If type is array this property sets if the 'items' are to be unique - together the three properties determine if it's a List or a Set*/
  @BeanProperty
  var uniqueItems: Boolean = false

  @JsonProperty(value = "$ref")
  @JsonSerialize(include = JsonSerialize.Inclusion.NON_DEFAULT)
  @XmlElement(name = "$ref")
  var ref: String = null

  @XmlTransient
  val simpleTypeList: List[String] = List("string", "number", "int", "boolean", "object", "Array", "null", "any")

  override def clone: Object = {
    val schema = new DocumentationSchema
    schema.objectType = objectType
    schema.required = required
    schema.name = name
    schema.id = id
    schema.properties = new java.util.HashMap[String, DocumentationSchema]
    // todo: clone
    schema.allowableValues = allowableValues
    schema.description = description
    schema.notes = notes
    schema.access = access
    schema.default = default
    // todo: clone
    schema.additionalProperties = additionalProperties
    // todo: clone
    schema.items = items
    schema.uniqueItems = uniqueItems
    schema.ref = ref
    schema
  }
}
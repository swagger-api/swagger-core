package com.wordnik.swagger.core


import scala.reflect.BeanProperty
import scala.collection._
import mutable.{ListBuffer}
import scala.collection.JavaConversions._
import javax.xml.bind.annotation.{XmlTransient, XmlRootElement, XmlElement}
import org.codehaus.jackson.map.annotate.JsonSerialize
import org.codehaus.jackson.annotate.{JsonIgnore, JsonProperty}
import java.util.HashMap

/**
  * @author ayush
  * @since 6/23/11 12:29 PM
  *
  */

trait Name {
	private var name:String =_
	@XmlElement(name="name")
	@JsonProperty(value="name")
	def getName:String = {
		name
	}

	@JsonProperty(value="name")
	def setName(name:String) = {
		this.name = name
	}
}

@JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
@XmlRootElement(name = "ApiDocumentation")
class Documentation (@BeanProperty var apiVersion: String,
                     @BeanProperty var swaggerVersion: String, @BeanProperty var basePath: String){

  def this() = this (null, null, null)
  private var _apis = new ListBuffer[DocumentationEndPoint]

  @XmlElement(name = "apis")
  def getApis(): java.util.List[DocumentationEndPoint] = if (_apis.size > 0) asList(_apis) else null

  def setApis(ep: java.util.List[DocumentationEndPoint]) = {
    this._apis.clear()
    if (ep != null) {
      for (n <- ep) {
        _apis + n
      }
    }
  }

  def addApi(ep: DocumentationEndPoint) = if (ep != null) _apis += ep

  def removeApi(ep: DocumentationEndPoint) = if (ep != null) _apis -= ep

  private var _objs = new ListBuffer[DocumentationObject]

  @JsonIgnore
  @XmlTransient
  def getModels = if (_objs.size > 0) asList(_objs) else null

  def addModel(obj: DocumentationObject) = if (obj != null) _objs += obj

  private var _schemas = new HashMap[String, DocumentationSchema]

  @JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
  @JsonProperty(value = "models")
  @XmlElement(`type` = classOf[ java.util.HashMap[String, DocumentationSchema] ], name = "models")
  def getSchemas():java.util.Map[String, DocumentationSchema] = if( _schemas.size() > 0) _schemas else null

  def setSchemas(sch: java.util.Map[String, DocumentationSchema]) = {
    this._schemas.clear();
    for(sch <- JavaConversions.asIterator( sch.iterator )) {
      _schemas += sch
    }
    /*this._schemas = sch*/
  }

  def addSchema(propertyName : String, obj: DocumentationSchema) = {
    if(propertyName != null && obj != null) {
      _schemas.put(propertyName, obj)
    }
  }

  override def clone(): Object = {
    var doc = new Documentation(apiVersion, swaggerVersion, basePath)
    for (ep <- _apis) {
      doc.addApi((ep.clone()).asInstanceOf[DocumentationEndPoint])
    }
    for (obj <- _objs) {
      doc.addModel((obj.clone()).asInstanceOf[DocumentationObject])
    }
    doc
  }
}

@JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
@XmlRootElement(name = "api")
class DocumentationEndPoint(@BeanProperty var path: String, @BeanProperty var description: String) {
  def this() = this (null, null)

  private var _ops = new ListBuffer[DocumentationOperation]

  @XmlElement
  def getOperations(): java.util.List[DocumentationOperation] = if (_ops.size > 0) asList(_ops) else null

  def setOperations(ep: java.util.List[DocumentationOperation]) = {
    this._ops.clear()
    if (ep != null) {
      for (n <- ep) {
        _ops + n
      }
    }
  }

  def addOperation(op: DocumentationOperation) = if (op != null) _ops += op

  private var _errorResponses = new ListBuffer[DocumentationError]

  @XmlElement
  def getErrorResponses(): java.util.List[DocumentationError] = if (_errorResponses.size > 0) asList(_errorResponses) else null

  def setErrorResponses(ep: java.util.List[DocumentationError]) = {
    this._errorResponses.clear()
    if (ep != null) {
      for (n <- ep) {
        _errorResponses + n
      }
    }
  }

  def addErrorResponse(error: DocumentationError) = if (error != null) _errorResponses += error


  override def clone(): Object = {
    var ep = new DocumentationEndPoint(path, description)
    for (op <- _ops) {
      ep.addOperation((op.clone()).asInstanceOf[DocumentationOperation])
    }
    for (er <- _errorResponses) ep.addErrorResponse((er.clone()).asInstanceOf[DocumentationError])
    ep
  }
}

@JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
@XmlRootElement(name = "operation")
class DocumentationOperation(@BeanProperty var httpMethod: String,
                             @BeanProperty var summary: String,
                             @BeanProperty var notes: String,
                             @BeanProperty var open: Boolean) {
  @BeanProperty var deprecated: Boolean = false
  @BeanProperty var responseClass: String = _
  @BeanProperty var nickname: String = _

  def this() = this (null, null, null, false)

  private var _parameters = new ListBuffer[DocumentationParameter]

  @XmlElement
  def getParameters(): java.util.List[DocumentationParameter] = if (_parameters.size > 0) asList(_parameters) else null

  def setParameters(ep: java.util.List[DocumentationParameter]) = {
    this._parameters.clear()
    if (ep != null) {
      for (n <- ep) {
        _parameters + n
      }
    }
  }
  def addParameter(param: DocumentationParameter) = _parameters += param

  private val _tags = new ListBuffer[String]

  @XmlElement
  def getTags(): java.util.List[String] = if (_tags.size > 0) asList(_tags) else null

  def setTags(tagList: java.util.List[String]) = {
    this._tags.clear()
    if (tagList != null && tagList.iterator != null) {
      for (tag <- tagList) {
        _tags + tag
      }
    }
  }

  private var responseTypeInternal: String = _

  def setResponseTypeInternal(s: String) = this.responseTypeInternal = s

  @XmlTransient
  def getResponseTypeInternal() = this.responseTypeInternal

  override def clone(): Object = {
    val cloned = new DocumentationOperation(httpMethod, summary, notes, open)
    cloned.deprecated = deprecated;
    cloned.nickname = nickname;
    cloned.responseClass = responseClass;
    cloned.responseTypeInternal = this.responseTypeInternal
    cloned.setTags(this._tags)

    for (p <- _parameters) cloned.addParameter((p.clone()).asInstanceOf[DocumentationParameter])

    cloned
  }

  def cloneExternal(): DocumentationOperation = {
    val cloned = new DocumentationOperation(httpMethod, summary, notes, open)
    cloned.deprecated = deprecated;
    cloned.nickname = nickname;
    cloned.responseClass = responseClass;
    cloned.responseTypeInternal = this.responseTypeInternal
    cloned.setTags(this._tags)

    for (p <- _parameters) {
      if ("internal" != p.paramAccess) {
        cloned.addParameter((p.clone()).asInstanceOf[DocumentationParameter])
      }
    }
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
                              @BeanProperty var allowableValues: java.util.List[String],
                              @BeanProperty var required: Boolean,
                              @BeanProperty var allowMultiple: Boolean) {
  @BeanProperty var paramAccess: String = _
  @BeanProperty var internalDescription: String = _
  @BeanProperty var wrapperName: String = _
  @BeanProperty var dataType: String = _

  def this() = this (null, null, null,null, null, null, false, false)

  override def clone(): Object = {
    val cloned = new DocumentationParameter(name, description, notes, paramType, defaultValue, allowableValues, required, allowMultiple)

    cloned.paramAccess = paramAccess
    cloned.internalDescription = internalDescription
    cloned.wrapperName = wrapperName
    cloned.dataType = dataType

    cloned
  }
}

//TODO - remove this Response class entirely as it is now catured in Operation
@JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
@XmlRootElement(name = "response")
class DocumentationResponse(
                             @BeanProperty var valueType: String,
                             @BeanProperty var occurs: String) {

  private var valueTypeInternal: String = _

  def setValueTypeInternal(s: String) = this.valueTypeInternal = s

  @XmlTransient
  def getValueTypeInternal() = this.valueTypeInternal

  def this() = this (null, null)

  private var _errorResponses = new ListBuffer[DocumentationError]

  @XmlElement
  def getErrorResponses(): java.util.List[DocumentationError] = if (_errorResponses.size > 0) asList(_errorResponses) else null

  def setErrorResponses(ep: java.util.List[DocumentationError]) = {
    this._errorResponses.clear()
    if (ep != null) {
      for (n <- ep) {
        _errorResponses + n
      }
    }
  }

  def addErrorResponse(error: DocumentationError) = if (error != null) _errorResponses += error

  override def clone(): Object = {
    var cloned = new DocumentationResponse(valueType, occurs)
    cloned.valueTypeInternal = this.valueTypeInternal
    for (er <- _errorResponses) cloned.addErrorResponse((er.clone()).asInstanceOf[DocumentationError])
    cloned
  }
}

@JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
@XmlRootElement(name = "error")
class DocumentationError(@BeanProperty var code: Int, @BeanProperty var reason: String) {
  def this() = this (0, null)

  override def clone(): Object = {
    new DocumentationError(code, reason)
  }
}

@JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
@XmlRootElement(name = "docObject")
class DocumentationObject extends Name {
  @BeanProperty var uniqueFieldName: String = _
  private var fields = new ListBuffer[DocumentationParameter]

  def getFields(): java.util.List[DocumentationParameter] = if (fields.size > 0) asList(fields) else null

  def setFields(ep: java.util.List[DocumentationParameter]) = {
    this.fields.clear()
    if (ep != null) {
      for (n <- ep) {
        fields + n
      }
    }
  }

  def addField(field: DocumentationParameter) = if (fields != null) fields += field

  override def clone(): Object = {
    var cloned = new DocumentationObject()
    for (f <- fields) cloned.addField((f.clone()).asInstanceOf[DocumentationParameter])
    cloned
  }

  def toDocumentationSchema() : DocumentationSchema = {
    val schemaObject = new DocumentationSchema()
    schemaObject.id = this.getName

    if(fields.length > 0) schemaObject.properties = new HashMap[String, DocumentationSchema]() else return null

    for (currentField <- fields) {
      val fieldSchema: DocumentationSchema = new DocumentationSchema()
      setSchemaTypeDef(currentField, fieldSchema)
      fieldSchema.required = currentField.required
      fieldSchema.description = currentField.description
      //TODO do we need the following fields - access and notes - not part of JSON schema
      fieldSchema.notes = currentField.notes
      fieldSchema.access = currentField.paramAccess

      if(currentField.allowableValues != null && currentField.allowableValues.length > 0 ){
        fieldSchema.enum = new ListBuffer[Object]
        for(allowableValue <- currentField.allowableValues) {
          fieldSchema.enum + allowableValue.asInstanceOf[AnyRef]
        }
      }
      schemaObject.properties.put(currentField.getName, fieldSchema)
    }

    schemaObject
  }

  private def setSchemaTypeDef(currentField: DocumentationParameter, currentSchema: DocumentationSchema) = {
    val isList = currentField.paramType.contains("List[");
    val isSet = currentField.paramType.contains("Set[");

    if(isList || isSet){
      currentSchema.setType("array")
      currentSchema.uniqueItems = isSet;
      val arrayElementType = currentField.paramType.slice(currentField.paramType.indexOf("[") + 1, currentField.paramType.indexOf("]"))
      val arrayItem = new DocumentationSchema
      if (currentSchema.simpleTypeList.find(item => arrayElementType.equals(item)) != None ) {
        arrayItem.setType(arrayElementType)
      } else {
        arrayItem.ref = arrayElementType
      }
      currentSchema.items = arrayItem
    }
    else{
      currentSchema.setType( currentField.paramType )
    }
  }
}

@JsonSerialize(include = JsonSerialize.Inclusion.NON_DEFAULT)
@XmlRootElement(name = "model")
class DocumentationSchema ( ){

  private var objectType: String = "any"

  @JsonSerialize(include = JsonSerialize.Inclusion.NON_DEFAULT)
  @JsonProperty(value = "type")
  @XmlElement(defaultValue = "any")
  def getType: String = this.objectType

  def setType(objectType : String): Unit = {
    this.objectType = objectType
  }

  @JsonSerialize(include = JsonSerialize.Inclusion.NON_DEFAULT)
  @BeanProperty
  var required: Boolean = false

  @XmlTransient
  @JsonIgnore(value = true)
  @BeanProperty var name: String = null

  @BeanProperty var id: String = null

  @BeanProperty var properties: java.util.Map[String, DocumentationSchema] = null

  @JsonSerialize(include = JsonSerialize.Inclusion.NON_DEFAULT)
  @BeanProperty var enum: java.util.List[Object] = null //TODO this should be object array

  @JsonSerialize(include = JsonSerialize.Inclusion.NON_DEFAULT)
  @BeanProperty var description: String = null

  @JsonSerialize(include = JsonSerialize.Inclusion.NON_DEFAULT)
  @BeanProperty var notes: String = null

  @JsonSerialize(include = JsonSerialize.Inclusion.NON_DEFAULT)
  @BeanProperty var access: String = null

  @JsonSerialize(include = JsonSerialize.Inclusion.NON_DEFAULT)
  @BeanProperty var default: String = null //TODO this should be object

  @BeanProperty var additionalProperties: DocumentationSchema = null

  @BeanProperty var items: DocumentationSchema = null

  /*If type is array this property sets if the 'items' are to be unique - together the three properties determine if it's a List or a Set*/
  @BeanProperty var uniqueItems: Boolean = false

  @XmlElement(name = "$ref") var ref: String = null

  @XmlTransient
  val simpleTypeList: List[String] = List("string", "number", "integer", "boolean", "object", "array", "null", "any")

}
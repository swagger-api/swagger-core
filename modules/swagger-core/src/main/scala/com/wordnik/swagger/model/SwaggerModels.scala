/**
 *  Copyright 2013 Wordnik, Inc.
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

package com.wordnik.swagger.model

import scala.collection.mutable.LinkedHashMap

case class ResourceListing(
  apiVersion: String, 
  swaggerVersion: String, 
  apis: List[ApiListingReference] = List(),
  authorizations: List[AuthorizationType] = List(),
  info: Option[ApiInfo] = None)

case class ApiInfo(
  title: String,
  description: String, 
  termsOfServiceUrl: String, 
  contact: String, 
  license: String, 
  licenseUrl: String)

case class LoginEndpoint(url: String)
case class TokenRequestEndpoint(url: String, clientIdName: String, clientSecretName: String)
case class TokenEndpoint(url: String, tokenName: String)

case class ApiListingReference(path:String, description: Option[String])

trait AllowableValues
case object AnyAllowableValues extends AllowableValues
case class AllowableListValues (values: List[String] = List(), valueType: String = "LIST") extends AllowableValues
case class AllowableRangeValues(min: String, max: String) extends AllowableValues

case class Model(
  id: String,
  name: String,
  qualifiedType: String,
  properties: LinkedHashMap[String, ModelProperty],
  description: Option[String] = None,
  baseModel: Option[String] = None,
  discriminator: Option[String] = None)

case class ModelProperty(
  `type`: String,
  qualifiedType: String,
  position: Int = 0,
  required: Boolean = false,
  description: Option[String] = None,
  allowableValues: AllowableValues = AnyAllowableValues,
  items: Option[ModelRef] = None)

case class ModelRef(
  `type`: String,
  ref: Option[String] = None,
  qualifiedType: Option[String] = None)

case class ApiListing (
  apiVersion: String,
  swaggerVersion: String,
  basePath: String,
  resourcePath: String,
  produces: List[String] = List.empty,
  consumes: List[String] = List.empty,
  protocols: List[String] = List.empty,
  authorizations: List[String] = List.empty,
  apis: List[ApiDescription] = List(),
  models: Option[Map[String, Model]] = None,
  description: Option[String] = None)

case class ApiDescription (
  path: String,
  description: Option[String],
  operations: List[Operation] = List())

case class Operation (
  method: String,
  summary: String,
  notes: String,
  responseClass: String,
  nickname: String,
  position: Int,
  produces: List[String] = List.empty,
  consumes: List[String] = List.empty,
  protocols: List[String] = List.empty,
  authorizations: List[String] = List.empty,
  parameters: List[Parameter] = List.empty,
  responseMessages: List[ResponseMessage] = List.empty,
  `deprecated`: Option[String] = None)

case class Parameter (
  name: String,
  description: Option[String],
  defaultValue: Option[String],
  required: Boolean,
  allowMultiple: Boolean,
  dataType: String,
  allowableValues: AllowableValues = AnyAllowableValues,
  paramType: String,
  paramAccess: Option[String] = None)

case class ResponseMessage (
  code: Int,
  message: String,
  responseModel: Option[String] = None)

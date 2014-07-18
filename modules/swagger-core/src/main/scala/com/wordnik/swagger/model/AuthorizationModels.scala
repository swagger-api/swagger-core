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

import scala.collection.mutable.ListBuffer

import scala.collection.JavaConverters._

trait AuthorizationType {
  def `type`: String
  def getName: String
}

class OAuthBuilder {
  val _scopes = new ListBuffer[AuthorizationScope]
  val _grantTypes = new ListBuffer[GrantType]

  def scopes(s: java.util.List[AuthorizationScope]) = {
    _scopes ++= s.asScala.toList
    this
  }
  def grantTypes(g: java.util.List[GrantType]) = {
    _grantTypes ++= g.asScala.toList
    this
  }
  def build() = {
    OAuth(_scopes.toList, _grantTypes.toList)
  }
}
case class Authorization(
  `type`: String,
  scopes: Array[AuthorizationScope]
)

case class AuthorizationScope(
  scope: String,
  description: String
)

case class OAuth(
  scopes: List[AuthorizationScope], 
  grantTypes: List[GrantType]) extends AuthorizationType {
  override def `type` = "oauth2"
  override def getName: String = `type`
}
case class ApiKey(keyname: String, passAs: String = "header") extends AuthorizationType {
  override def `type` = "apiKey"
  override def getName: String = keyname
}
case class BasicAuth() extends AuthorizationType {
  override def `type` = "basicAuth"
  override def getName: String = `type`
}

trait GrantType {
  def `type`: String
}
case class ImplicitGrant(
  loginEndpoint: LoginEndpoint, 
  tokenName: String) extends GrantType {
  def `type` = "implicit"
}
case class AuthorizationCodeGrant(
  tokenRequestEndpoint: TokenRequestEndpoint,
  tokenEndpoint: TokenEndpoint) extends GrantType {
  def `type` = "authorization_code"
}

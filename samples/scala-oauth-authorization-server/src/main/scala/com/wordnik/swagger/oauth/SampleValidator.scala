package com.wordnik.swagger.oauth

class SampleValidator extends com.wordnik.swagger.auth.service.Validator {
  def isValidUser(username: String, password: String): Boolean = {
  	true
  }

  def isValidClient(clientId: String, clientSecret: String): Boolean = {
  	true
  }
  def isValidRedirectUri(uri: String): Boolean = {
  	true
  }
}

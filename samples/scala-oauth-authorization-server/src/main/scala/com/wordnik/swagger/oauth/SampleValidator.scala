package com.wordnik.swagger.oauth

class SampleValidator extends com.wordnik.swagger.auth.service.Validator {
  def isValidUser(username: String, password: String): Boolean = {
    if((username != null && username != "") && (password != null && password != ""))
  	  true
    else
      false
  }

  def isValidClient(clientId: String, clientSecret: String): Boolean = {
  	true
  }
  def isValidRedirectUri(uri: String): Boolean = {
  	true
  }
}

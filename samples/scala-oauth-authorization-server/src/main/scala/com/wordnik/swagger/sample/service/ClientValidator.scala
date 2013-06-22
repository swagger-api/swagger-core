package com.wordnik.swagger.sample.service

/**
 * put in your client validation, username logic here
 **/
trait ClientValidator {
  def isValidUser(username: String, password: String) = {
    if("bob" == username && "dobbs" == password) true
    else false
  }

  def isValidClient(clientId: String, clientSecret: String) = {
    if("someclientid" == clientId && "secret" == clientSecret) true
    else false
  }

  def isValidRedirectUri(uri: String): Boolean = {
    uri.startsWith("http://localhost")
  }
}
package com.wordnik.swagger.auth.service

object ValidatorFactory {
  var validator: Validator = new NoOpValidator
}

class NoOpValidator extends Validator {
  def isValidUser(username: String, password: String) = false
  def isValidClient(clientId: String, clientSecret: String) = false
  def isValidRedirectUri(clientId: String, uri: String): Boolean = false
}

trait Validator {
  def isValidUser(username: String, password: String): Boolean
  def isValidClient(clientId: String, clientSecret: String): Boolean
  def isValidRedirectUri(clientId: String, uri: String): Boolean
}

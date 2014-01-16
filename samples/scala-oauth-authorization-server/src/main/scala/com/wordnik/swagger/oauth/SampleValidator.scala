package com.wordnik.swagger.oauth

import org.slf4j.LoggerFactory

class SampleValidator extends com.wordnik.swagger.auth.service.Validator {
  private val LOGGER = LoggerFactory.getLogger(classOf[SampleValidator])

  def isValidUser(username: String, password: String): Boolean = {
    LOGGER.debug("validing user " + username + ", password " + password)
    if((username != null && username != "") && (password != null && password != ""))
  	  true
    else
      false
  }

  def isValidClient(clientId: String, clientSecret: String): Boolean = {
    LOGGER.debug("validating client '" + clientId + "' / '" + clientSecret + "'")
  	true
  }
  def isValidRedirectUri(clientId: String, uri: String): Boolean = {
    LOGGER.debug("validating redirect URL '" + clientId + "' / '" + uri + "'")
  	true
  }
}

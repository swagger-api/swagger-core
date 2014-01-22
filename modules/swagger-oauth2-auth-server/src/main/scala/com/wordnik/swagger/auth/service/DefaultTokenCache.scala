package com.wordnik.swagger.auth.service

import com.wordnik.swagger.auth.model.TokenWrapper

import scala.collection.mutable.{ HashSet, HashMap }

class DefaultTokenCache extends TokenCache {
  val codeCache = new HashSet[String]
  val tokenCache = new HashMap[String, TokenWrapper]
  val requestCache = new HashMap[String, Map[String, Option[String]]]

  def hasCode(code: String): Boolean = codeCache.contains(code)
  def addCode(code: String) = codeCache += code
  def removeCode(code: String) = codeCache.remove(code)

  def hasAccessCode(accessCode: String): Boolean = tokenCache.contains(accessCode)
  def getTokenForAccessCode(accessCode: String) = tokenCache(accessCode)
  def addAccessCode(accessCode: String, token: TokenWrapper) = tokenCache += accessCode -> token
  def removeAccessCode(accessCode: String) = tokenCache.remove(accessCode)

  def hasRequestId(requestId: String): Boolean = requestCache.contains(requestId)
  def getRequestId(requestId: String): Map[String, Option[String]] = requestCache(requestId)
  def addRequestId(requestId: String, requestMap: Map[String, Option[String]]) = requestCache += requestId -> requestMap
  def removeRequestId(requestId: String) = requestCache.remove(requestId)
}
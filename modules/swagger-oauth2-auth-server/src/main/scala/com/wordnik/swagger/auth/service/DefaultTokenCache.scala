package com.wordnik.swagger.auth.service

import com.wordnik.swagger.auth.model.TokenWrapper

import scala.collection.mutable.{ HashSet, HashMap }

class DefaultTokenCache extends TokenCache {
  val tokenCache = new HashMap[String, TokenWrapper]
  val requestCache = new HashMap[String, Map[String, Option[String]]]

  def hasAccessCode(accessCode: String): Boolean = tokenCache.contains(accessCode)
  def exchangeRequestIdForCode(requestId: String): String = TokenGenerator.generateAccessToken()
  def getTokenForAccessCode(accessCode: String) = tokenCache(accessCode)
  def addAccessCode(accessCode: String, token: TokenWrapper) = tokenCache += accessCode -> token
  def removeAccessCode(accessCode: String) = tokenCache.remove(accessCode)

  def hasRequestId(requestId: String): Boolean = requestCache.contains(requestId)
  def getRequestId(requestId: String): Map[String, Option[String]] = requestCache(requestId)
  def addRequestId(requestId: String, requestMap: Map[String, Option[String]]) = requestCache += requestId -> requestMap
  def removeRequestId(requestId: String) = requestCache.remove(requestId)

  def allowAnonymousTokens(): Boolean = true
}
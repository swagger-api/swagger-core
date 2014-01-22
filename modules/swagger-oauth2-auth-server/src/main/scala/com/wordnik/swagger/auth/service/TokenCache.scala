package com.wordnik.swagger.auth.service

import com.wordnik.swagger.auth.model.TokenWrapper

import org.apache.oltu.oauth2.as.issuer.{ MD5Generator, OAuthIssuerImpl }

import scala.collection.mutable.{ HashSet, HashMap }

trait TokenCache {
  def hasCode(code: String): Boolean
  def addCode(code: String)
  def removeCode(code: String)

  def hasAccessCode(accessCode: String): Boolean
  def getTokenForAccessCode(accessCode: String): TokenWrapper
  def addAccessCode(accessCode: String, token: TokenWrapper)
  def removeAccessCode(accessCode: String)

  def hasRequestId(requestId: String): Boolean
  def getRequestId(requestId: String): Map[String, Option[String]]
  def addRequestId(requestId: String, requestMap: Map[String, Option[String]])
  def removeRequestId(requestId: String)
}

object TokenFactory {
  var tokenCache: Option[TokenCache] = None

  def apply(): TokenCache = {
    if(tokenCache == None)
      tokenCache = Some(new DefaultTokenCache())
    tokenCache.get
  }
}

trait TokenStore {
  def hasCode(code: String): Boolean = TokenFactory().hasCode(code)
  def addCode(code: String) = TokenFactory().addCode(code)
  def removeCode(code: String) = TokenFactory().removeCode(code)

  def hasAccessCode(accessCode: String): Boolean = TokenFactory().hasAccessCode(accessCode)
  def getTokenForAccessCode(accessCode: String): TokenWrapper = TokenFactory().getTokenForAccessCode(accessCode)
  def addAccessCode(accessCode: String, token: TokenWrapper) = TokenFactory().addAccessCode(accessCode, token)
  def removeAccessCode(accessCode: String) = TokenFactory().removeAccessCode(accessCode)

  def hasRequestId(requestId: String): Boolean = TokenFactory().hasRequestId(requestId)
  def getRequestId(requestId: String): Map[String, Option[String]] = TokenFactory().getRequestId(requestId)
  def addRequestId(requestId: String, requestMap: Map[String, Option[String]]) = TokenFactory().addRequestId(requestId, requestMap)
  def removeRequestId(requestId: String) = TokenFactory().removeRequestId(requestId)

  def generateRequestId(clientId: String) = generateRandomCode("requestId", clientId)

  def generateCode(clientId: String) = generateRandomCode("code", clientId)

  def generateAccessToken() = {
    val oauthIssuerImpl = new OAuthIssuerImpl(new MD5Generator())
    oauthIssuerImpl.accessToken()
  }

  def generateRandomCode(scope: String, seed: String) = {
    val keySource = scope + seed + System.currentTimeMillis.toString + new scala.util.Random(1).nextInt(1000)
    val md = java.security.MessageDigest.getInstance("SHA-1")
    new sun.misc.BASE64Encoder().encode(md.digest(keySource.getBytes))
  }
}

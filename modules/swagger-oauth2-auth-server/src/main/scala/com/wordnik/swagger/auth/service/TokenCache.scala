package com.wordnik.swagger.auth.service

import com.wordnik.swagger.auth.model.TokenWrapper

import org.apache.oltu.oauth2.as.issuer.{ MD5Generator, OAuthIssuerImpl }

import scala.collection.mutable.{ HashSet, HashMap }

object TokenCache {
  val codeCache = new HashSet[String]
  val tokenCache = new HashMap[String, TokenWrapper]
  val requestCache = new HashMap[String, Map[String, Option[String]]]
}

trait TokenCache {
  def codeCache = TokenCache.codeCache
  def tokenCache = TokenCache.tokenCache
  def requestCache = TokenCache.requestCache

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

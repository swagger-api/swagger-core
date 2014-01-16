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

  def generateRequestId() = {
    "request-" + System.currentTimeMillis
  }

  def generateCode() = {
    "code-" + System.currentTimeMillis
  }

  def generateAccessToken() = {
    val oauthIssuerImpl = new OAuthIssuerImpl(new MD5Generator())
    oauthIssuerImpl.accessToken()
  }
}

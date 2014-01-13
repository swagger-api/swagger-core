package com.wordnik.swagger.auth.service

import com.wordnik.swagger.auth.model._

import org.apache.oltu.oauth2.as.issuer.{ MD5Generator, OAuthIssuerImpl }
import org.apache.oltu.oauth2.as.request.{ OAuthAuthzRequest, OAuthTokenRequest }
import org.apache.oltu.oauth2.as.response.OAuthASResponse
import org.apache.oltu.oauth2.common.OAuth
import org.apache.oltu.oauth2.common.exception.{ OAuthProblemException, OAuthSystemException }
import org.apache.oltu.oauth2.common.message.OAuthResponse
import org.apache.oltu.oauth2.common.message.types.ResponseType
import org.apache.oltu.oauth2.common.utils.OAuthUtils

import org.slf4j.LoggerFactory

import javax.servlet.http.{ Cookie, HttpServletRequest, HttpServletResponse }

import javax.ws.rs.core.Response
import javax.ws.rs.WebApplicationException

import java.util.Date
import java.net.URLEncoder
import java.net.URI

import scala.collection.mutable.HashSet

class AuthService extends TokenCache {
  private val LOGGER = LoggerFactory.getLogger(classOf[AuthService])

  val validator = ValidatorFactory.validator

  def validate[T](authorizationCode: String, f: => T):T = {
    tokenCache.contains(authorizationCode) match {
      case true => {
        val token = tokenCache(authorizationCode)
        token.tokenResponse match {
          case e: AnonymousTokenResponse => {
            TokenScope.unsetUserId()
          }
          case e: UserTokenResponse => {
            // add user id + other scope to the TLV
            TokenScope.setUserId(e.userId)
          }
        }
        if(token.getRemaining > 0) f
        else throw new Exception("unauthorized")
      }
      case false => {
        throw new Exception("unauthorized")
      }
    }
  }

  def login(request: HttpServletRequest, response: HttpServletResponse) = {
    val scope = request.getParameter("scope")
    val redirectUri = request.getParameter("redirect_uri")
    val username = request.getParameter("username")
    val password = request.getParameter("password")
    val clientId = request.getParameter("client_id")
    val accept = request.getParameter("accept")

    if(scope == "anonymous") {
      if(validator.isValidRedirectUri(redirectUri)) {
        // it's good to proceed
        val oauthIssuerImpl = new OAuthIssuerImpl(new MD5Generator())
        val accessToken = oauthIssuerImpl.accessToken()
        val token = AnonymousTokenResponse(3600, accessToken)
        tokenCache += accessToken -> TokenWrapper(new Date, token)
        val redirectTo = {
          (redirectUri.indexOf("#") match {
            case i: Int if(i >= 0) => redirectUri + "&"
            case i: Int => redirectUri + "#"
          }) + "access_token=" + accessToken
        }

        val cookie = new Cookie("access_token", token.access_token)
        cookie.setPath("/")
        response.addCookie(cookie)

        response.sendRedirect(redirectTo)
      }
      else response.getOutputStream.write("bad redirect_uri".getBytes("utf-8"))
    }

    if(validator.isValidUser(username, password) && accept.toLowerCase != "deny") {
      if(validator.isValidRedirectUri(redirectUri)) {
        // it's good to proceed
        val oauthIssuerImpl = new OAuthIssuerImpl(new MD5Generator())
        val accessToken = oauthIssuerImpl.accessToken()
        val token = UserTokenResponse(3600, accessToken, 1)
        tokenCache += accessToken -> TokenWrapper(new Date, token)
        val redirectTo = {
          (redirectUri.indexOf("#") match {
            case i: Int if(i >= 0) => redirectUri + "&"
            case i: Int => redirectUri + "#"
          }) + "access_token=" + accessToken
        }
        val cookie = new Cookie("access_token", token.access_token)
        cookie.setPath("/")
        response.addCookie(cookie)

        response.sendRedirect(redirectTo)
      }
      else response.getOutputStream.write("bad redirect_uri".getBytes("utf-8"))
    }
    else response.getOutputStream.write("invalid credentials".getBytes("utf-8"))
  }

  def tokenStatus(authorizationCode: String) = {

    tokenCache.contains(authorizationCode) match {
      case true => {
        val token = tokenCache(authorizationCode)
        if(token.getRemaining > 0)
          ApiResponseMessage(200, "%d seconds remaining".format(token.getRemaining))
        else
          ApiResponseMessage(400, "invalid token")
      }
      case false => ApiResponseMessage(400, "invalid token")
    }
  }

  def token(request: HttpServletRequest, 
    response: HttpServletResponse): TokenResponse = {
    try {
      val oauthRequest = new OAuthTokenRequest(request)
      val oauthIssuerImpl = new OAuthIssuerImpl(new MD5Generator())
      val code = oauthRequest.getParam(OAuth.OAUTH_CODE)
      val grantType = oauthRequest.getParam(OAuth.OAUTH_GRANT_TYPE)
      if(validator.isValidClient(oauthRequest.getParam(OAuth.OAUTH_CLIENT_ID), oauthRequest.getParam(OAuth.OAUTH_CLIENT_SECRET))) {
        if("authorization_code" == grantType) {
          if(!codeCache.contains(code)){
            throw new Exception("invalid code supplied")
          }
          else {
            val accessToken = oauthIssuerImpl.accessToken()
            val token = AnonymousTokenResponse(3600, accessToken)
            tokenCache += accessToken -> TokenWrapper(new Date, token)
            token
          }
        }
        else throw new Exception("unsupported grant type")
      }
      else throw new Exception("invalid client id")
    }
    catch {
      case e: Exception => {
        throw new Exception(e.getMessage)
      }
    }
  }

  def authorize(request:HttpServletRequest, response: HttpServletResponse): ApiResponseMessage = {
    var oauthRequest: OAuthAuthzRequest = null;
    var oauthIssuerImpl = new OAuthIssuerImpl(new MD5Generator())

    try {
      oauthRequest = new OAuthAuthzRequest(request)

      //build response according to response_type
      val responseType = oauthRequest.getParam(OAuth.OAUTH_RESPONSE_TYPE)

      val builder = OAuthASResponse.authorizationResponse(request, HttpServletResponse.SC_FOUND)

      if (responseType.equals(ResponseType.CODE.toString())) {
        // server flow!
        val authCode = oauthIssuerImpl.authorizationCode()
        codeCache.add(authCode)
        builder.setCode(authCode)
        val redirectURI = oauthRequest.getParam(OAuth.OAUTH_REDIRECT_URI)

        val response = builder.location(redirectURI).buildQueryMessage()
        val url = new URI(response.getLocationUri())
        ApiResponseMessage(response.getResponseStatus(), url.toString)
      }
      else if (responseType.equals(ResponseType.TOKEN.toString())) {
        // client ID is required!
        if(oauthRequest.getParam(OAuth.OAUTH_CLIENT_ID) == null) {
          ApiResponseMessage(HttpServletResponse.SC_BAD_REQUEST, "client_id not found")
        }
        else {
          val sb = new StringBuilder()
          try{
            sb.append(OAuth.OAUTH_CLIENT_ID).append("=").append(URLEncoder.encode(oauthRequest.getParam(OAuth.OAUTH_CLIENT_ID), "UTF-8")).append("&")
            if(oauthRequest.getParam(OAuth.OAUTH_REDIRECT_URI) != null)
              sb.append(OAuth.OAUTH_REDIRECT_URI).append("=").append(URLEncoder.encode(oauthRequest.getParam(OAuth.OAUTH_REDIRECT_URI), "UTF-8")).append("&")
            if(oauthRequest.getParam(OAuth.OAUTH_RESPONSE_TYPE) != null)
              sb.append(OAuth.OAUTH_RESPONSE_TYPE).append("=").append(URLEncoder.encode(oauthRequest.getParam(OAuth.OAUTH_RESPONSE_TYPE), "UTF-8")).append("&")
            if(oauthRequest.getParam(OAuth.OAUTH_STATE) != null)
              sb.append(OAuth.OAUTH_STATE).append("=").append(URLEncoder.encode(oauthRequest.getParam(OAuth.OAUTH_STATE), "UTF-8")).append("&")
            if(oauthRequest.getParam(OAuth.OAUTH_SCOPE) != null)
              sb.append(OAuth.OAUTH_SCOPE).append("=").append(URLEncoder.encode(oauthRequest.getParam(OAuth.OAUTH_SCOPE), "UTF-8"))
          }
          catch {
            case e: Exception => e.printStackTrace()
          }
          val redirectURI = new URI("/foo?" + sb.toString())
          ApiResponseMessage(307, redirectURI.toString)
        }
      }
      else {
        val redirectURI = oauthRequest.getParam(OAuth.OAUTH_REDIRECT_URI)
        val response = builder.location(redirectURI).buildQueryMessage()
        val url = new URI(response.getLocationUri())
        ApiResponseMessage(200, url.toString)
      }
    } catch {
      case e: OAuthProblemException => {
        e.printStackTrace();
        val responseBuilder = Response.status(HttpServletResponse.SC_FOUND)

        val redirectUri = e.getRedirectUri();

        if (OAuthUtils.isEmpty(redirectUri)) {
          ApiResponseMessage(400, "OAuth callback url needs to be provided by client")
        }
        else ApiResponseMessage(400, e.getMessage)
      }
    }
  }
}

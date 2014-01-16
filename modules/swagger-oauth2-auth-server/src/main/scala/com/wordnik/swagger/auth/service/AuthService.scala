package com.wordnik.swagger.auth.service

import com.wordnik.swagger.auth.model._
import com.wordnik.swagger.core.SwaggerContext

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
    LOGGER.debug("validating code " + authorizationCode)
    Option(tokenCache.getOrElse(authorizationCode, null)) match {
      case Some(token) if(token.getRemaining > 0) => {
        token.tokenResponse match {
          case e: AnonymousTokenResponse => {
            TokenScope.unsetUserId()
          }
          case e: UserTokenResponse => {
            // add user id + other scope to the TLV
            TokenScope.setUserId(e.userId)
          }
          case _ => throw new Exception("unauthorized")
        }
        f
      }
      case _ => throw new Exception("unauthorized")
    }
  }

  def login(request: HttpServletRequest, response: HttpServletResponse) = {
    val scope = request.getParameter("scope")
    val redirectUri = request.getParameter("redirect_uri")
    val username = request.getParameter("username")
    val password = request.getParameter("password")
    val clientId = request.getParameter("client_id")
    val accept = request.getParameter("accept")
    val requestId = request.getParameter("request_id")

    LOGGER.debug("logging in user " + username + ", accept=" + accept)

    if(accept.toLowerCase == "deny") {
      LOGGER.debug("user " + username + " denied the login request")
      val redirectTo = {
        (redirectUri.indexOf("#") match {
          case i: Int if(i >= 0) => redirectUri + "&"
          case i: Int => redirectUri + "#"
        }) + "error=user_cancelled"
      }
      response.sendRedirect(redirectTo)
    }
    else if(validator.isValidUser(username, password)) {
      LOGGER.debug("username " + username + " has valid password")
      if(validator.isValidRedirectUri(clientId, redirectUri)) {
        LOGGER.debug("username " + username + " has valid redirect URI: " + redirectUri)
        val redirectTo = {
          (redirectUri.indexOf("?") match {
            case i: Int if(i >= 0) => redirectUri + "&"
            case i: Int => redirectUri + "?"
          })
        }
        if(requestId != null && !"".equals(requestId)) {
          LOGGER.debug("username " + username + " has request id=" + requestId)
          Option(TokenCache.requestCache.getOrElse(requestId, null)) match {
            case Some(token) => {
              LOGGER.debug("token for requestId " + requestId + " found")
              val redirectUri = token(OAuth.OAUTH_REDIRECT_URI ).get
              val redirectTo = {
                (redirectUri.indexOf("?") match {
                  case i: Int if(i >= 0) => redirectUri + "&"
                  case i: Int => redirectUri + "?"
                })
              }
              val code = generateCode
              TokenCache.codeCache += code
              LOGGER.debug("redirecting to " + redirectTo + "code=" + code)
              response.sendRedirect(redirectTo + "code=" + code)
            }
            case None => {
              LOGGER.debug("token for requestId " + requestId + " NOT found")
              response.sendRedirect(redirectTo + "error=invalid_code")
            }
          }
        }
        else {
          LOGGER.debug("no request id, generating access token")
          val accessToken = generateAccessToken()
          val token = UserTokenResponse(3600, accessToken, 1)
          tokenCache += accessToken -> TokenWrapper(new Date, token)
          val redirectTo = {
            (redirectUri.indexOf("#") match {
              case i: Int if(i >= 0) => redirectUri + "&"
              case i: Int => redirectUri + "#"
            }) + "access_token=" + accessToken
          }
          response.sendRedirect(redirectTo)
        }
      }
      else response.getOutputStream.write("bad redirect_uri".getBytes("utf-8"))
    }
    else {
      LOGGER.debug("invalid credentials")
      val redirectTo = {
        (redirectUri.indexOf("#") match {
          case i: Int if(i >= 0) => redirectUri + "&"
          case i: Int => redirectUri + "#"
        }) + "error=invalid_credentials"
      }
      response.sendRedirect(redirectTo)
    }
  }

  def authorizationCodeStatus(authorizationCode: String) = {
    LOGGER.debug("checking code status for " + authorizationCode)
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

  def token(request: HttpServletRequest, response: HttpServletResponse): TokenResponse = {
    try {
      val oauthRequest = new OAuthTokenRequest(request)
      val code = oauthRequest.getParam(OAuth.OAUTH_CODE)
      val grantType = oauthRequest.getParam(OAuth.OAUTH_GRANT_TYPE)
      val clientId = oauthRequest.getParam(OAuth.OAUTH_CLIENT_ID)
      val clientSecret = oauthRequest.getParam(OAuth.OAUTH_CLIENT_SECRET)

      LOGGER.debug("get token for '" + clientId + "'' with client secret '" + clientSecret + "'")
      if(validator.isValidClient(clientId, clientSecret)) {
        if("authorization_code" == grantType) {
          LOGGER.debug("grant type is " + grantType)
          if(!codeCache.contains(code)){
            throw new Exception("invalid code supplied")
          }
          else {
            val accessToken = generateAccessToken()
            val token = AnonymousTokenResponse(3600, accessToken)
            tokenCache += accessToken -> TokenWrapper(new Date, token)
            token
          }
        }
        else {
          LOGGER.debug("unsupported grant type " + grantType)
          throw new Exception("unsupported grant type")
        }
      }
      else {
        LOGGER.debug("invalid client id " + clientId)
        throw new Exception("invalid client id")
      }
    }
    catch {
      case e: Exception => {
        throw new Exception(e.getMessage)
      }
    }
  }

  def authorize(request: HttpServletRequest,response: HttpServletResponse): ApiResponseMessage = {
    var oauthRequest: OAuthAuthzRequest = null;
    try {
      oauthRequest = new OAuthAuthzRequest(request)

      //build response according to response_type
      val responseType = oauthRequest.getParam(OAuth.OAUTH_RESPONSE_TYPE)
      val builder = OAuthASResponse.authorizationResponse(request, HttpServletResponse.SC_FOUND)

      if (responseType.equals(ResponseType.CODE.toString())) {
        // val showDialog = true
        // if(showDialog) {
        val requestMap = Map(
          OAuth.OAUTH_STATE -> Option(oauthRequest.getParam(OAuth.OAUTH_STATE)),
          OAuth.OAUTH_REDIRECT_URI -> Option(oauthRequest.getParam(OAuth.OAUTH_REDIRECT_URI)),
          OAuth.OAUTH_CLIENT_ID -> Option(oauthRequest.getParam(OAuth.OAUTH_CLIENT_ID)))

        val requestId = generateRequestId
        TokenCache.requestCache += requestId -> requestMap

        val dialogClass = Option(request.getSession.getServletContext.getInitParameter("DialogImplementation")).getOrElse({
          LOGGER.warn("using default dialog implementation")
          "com.wordnik.swagger.auth.service.DefaultAuthDialog"
        })
        val dialog = SwaggerContext.loadClass(dialogClass).newInstance.asInstanceOf[AuthDialog]
        // write the dialog UI
        dialog.show(oauthRequest.getParam(OAuth.OAUTH_CLIENT_ID),
          request.getRequestURI,
          "scope",
          Option(requestId))
        /*
        }
        else {
          println(responseType)
          Option(oauthRequest.getParam(OAuth.OAUTH_STATE)) match {
            case Some(state) => //builder.setState(state)
            case _ =>
          }
          val code = generateCode

          codeCache.add(code)
          val redirectURI = oauthRequest.getParam(OAuth.OAUTH_REDIRECT_URI)
          val response = builder.location(redirectURI).buildQueryMessage()
          val url = new URI(response.getLocationUri())
          ApiResponseMessage(response.getResponseStatus(), url.toString)
        }*/
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

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

class AuthService extends TokenStore {
  private val LOGGER = LoggerFactory.getLogger(classOf[AuthService])

  val validator = ValidatorFactory.validator

  def validate[T](accessCode: String, f: => T):T = {
    LOGGER.debug("validating access code " + accessCode)
    if(hasAccessCode(accessCode)) {
      val token = getTokenForAccessCode(accessCode)
      if(token.getRemaining > 0) {
        token.tokenResponse match {
          case e: AnonymousTokenResponse => TokenScope.unsetUsername()
          case e: UserTokenResponse => TokenScope.setUsername(e.username)
          case _ => throw new Exception("unauthorized")
        }
        f
      }
      else throw new Exception("unauthorized")
    }
    else throw new Exception("unauthorized")
  }

  def login(request: HttpServletRequest, response: HttpServletResponse) = {
    val scope = request.getParameter("scope")
    val redirectUri = request.getParameter("redirect_uri")
    val username = request.getParameter("username")
    val password = request.getParameter("password")
    val clientId = request.getParameter("client_id")
    val accept = request.getParameter("accept")
    val requestId = request.getParameter("request_id")
    val responseType = request.getParameter("response_type")

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
          if(hasRequestId(requestId)) {
            LOGGER.debug("token for requestId " + requestId + " found")
            // now that we have the username, save it in the hash
            val requestToken = getRequestId(requestId) ++ Map("username" -> Some(username))
            addRequestId(requestId, requestToken)

            val redirectUri = requestToken(OAuth.OAUTH_REDIRECT_URI ).get
            val redirectTo = {
              (redirectUri.indexOf("?") match {
                case i: Int if(i >= 0) => redirectUri + "&"
                case i: Int => redirectUri + "?"
              })
            }
            val code = exchangeRequestIdForCode(requestId)
            val token = UserTokenResponse(3600, code, username)
            addAccessCode(code, TokenWrapper(new Date, token))
            LOGGER.debug("redirecting to " + redirectTo + "code=" + code)
            response.sendRedirect(redirectTo + "code=" + code)
          }
          else {
            LOGGER.debug("token for requestId " + requestId + " NOT found")
            response.sendRedirect(redirectTo + "error=invalid_code")
          }
        }
        else {
          // should this actually exist?
          LOGGER.debug("no request id, generating access token")
          val accessToken = generateAccessToken()
          val token = UserTokenResponse(3600, accessToken, username)
          addAccessCode(accessToken, TokenWrapper(new Date, token))

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

  def authorizationCodeStatus(accessCode: String) = {
    LOGGER.debug("checking code status for " + accessCode)
    if(hasAccessCode(accessCode)) {
      val token = getTokenForAccessCode(accessCode)
      if(token.getRemaining > 0)
        ApiResponseMessage(200, "%d seconds remaining".format(token.getRemaining))
      else
        ApiResponseMessage(400, "invalid token")
    }
    else 
      ApiResponseMessage(400, "invalid token")
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
          val validCode = hasAccessCode(code)

          if(validCode) {
            val username = getTokenForAccessCode(code).tokenResponse match {
              case e: UserTokenResponse => e.username
              case _ => null
            }
            removeAccessCode(code)
            val accessToken = generateAccessToken()
            val token = UserTokenResponse(3600, accessToken, username)
            addAccessCode(accessToken, TokenWrapper(new Date, token))
            token
          }
          else if(allowAnonymousTokens()) {
            val accessToken = generateAccessToken()
            val token = AnonymousTokenResponse(3600, accessToken)
            addAccessCode(accessToken, TokenWrapper(new Date, token))
            token
          }
          else
            throw new Exception("invalid code supplied")
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
    import scala.collection.JavaConverters._

    var oauthRequest: OAuthAuthzRequest = null;
    try {
      oauthRequest = new OAuthAuthzRequest(request)

      //build response according to response_type
      val responseType = oauthRequest.getParam(OAuth.OAUTH_RESPONSE_TYPE)
      val builder = OAuthASResponse.authorizationResponse(request, HttpServletResponse.SC_FOUND)

      if (responseType.equals(ResponseType.CODE.toString())) {
        val requestMap = Map(
          OAuth.OAUTH_STATE -> Option(oauthRequest.getParam(OAuth.OAUTH_STATE)),
          OAuth.OAUTH_REDIRECT_URI -> Option(oauthRequest.getParam(OAuth.OAUTH_REDIRECT_URI)),
          OAuth.OAUTH_CLIENT_ID -> Option(oauthRequest.getParam(OAuth.OAUTH_CLIENT_ID)),
          OAuth.OAUTH_SCOPE -> Option(oauthRequest.getParam(OAuth.OAUTH_SCOPE)))

        val requestId = generateRequestId(oauthRequest.getParam(OAuth.OAUTH_CLIENT_ID))
        addRequestId(requestId, requestMap)
        val dialogClass = Option(request.getSession.getServletContext.getInitParameter("DialogImplementation")).getOrElse({
          LOGGER.warn("using default dialog implementation")
          "com.wordnik.swagger.auth.service.DefaultAuthDialog"
        })
        val dialog = SwaggerContext.loadClass(dialogClass).newInstance.asInstanceOf[AuthDialog]
        // write the dialog UI
        dialog.show(oauthRequest.getParam(OAuth.OAUTH_CLIENT_ID),
          oauthRequest.getParam(OAuth.OAUTH_REDIRECT_URI),
          oauthRequest.getParam(OAuth.OAUTH_SCOPE),
          ResponseType.CODE.toString(),
          Option(requestId))
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

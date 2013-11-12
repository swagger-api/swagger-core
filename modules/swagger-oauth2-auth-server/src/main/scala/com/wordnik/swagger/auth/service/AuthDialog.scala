package com.wordnik.swagger.auth.service

import com.wordnik.swagger.auth.model._

import org.apache.oltu.oauth2.as.issuer.{ MD5Generator, OAuthIssuerImpl }

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.util.Date
import java.net.URLEncoder
import java.net.URI

trait AuthDialog extends TokenCache {
  /**
   * In this sample, the scope 'anonymous' will allow access if the redirectUri
   * is 'localhost' and provide an AnonymousTokenRequest, which is good for 3600
   * seconds
   */
  def dialog(clientId: String, redirectUri: String, scope: String) = {
    if(scope == "anonymous") {
      val url = "/oauth/login"

      if(redirectUri.startsWith("http://localhost")) {
        // it's good to proceed
        val oauthIssuerImpl = new OAuthIssuerImpl(new MD5Generator())
        val accessToken = oauthIssuerImpl.accessToken()
        val token = AnonymousTokenResponse(3600, accessToken)
        tokenCache += accessToken -> TokenWrapper(new Date, token)
        val redirectTo = {
          (redirectUri.indexOf("?") match {
            case i: Int if(i >= 0) => redirectUri + "&"
            case i: Int => redirectUri + "?"
          }) + "access_token=" + accessToken
        }
        ApiResponseMessage(302, redirectTo)
      }
      else throw new Exception("bad redirect_uri")
    }
    else {
      /**
       * render the login dialog
       */
      val html = 
<html class="js placeholder">
  <head>
    <link rel="stylesheet" href="/css/login.css"></link>
    <script src="/lib/jquery-1.8.0.min.js"></script>
    <script src="/lib/modernizr.js"></script>
    <script src="/lib/shared.js"></script>
    <script src="/lib/login.js"></script>
    <script src="/lib/qframe.js"></script>
  </head>
  <body id = "oauth-authorize" class = "oauth-page">
    <header>
      <h1></h1>
      <p>
        <strong><a href="#">Some Sample App</a>would like to connect to your service account.</strong>
      </p>
    </header>
    <section class = "email_form">
      <form class="confirm" method="POST" id="oauth-authorize-login" action={"/oauth/login?redirect_uri=" + redirectUri}>
        <input name="scope" type="hidden" value="email"></input>
        <input name="client_id" type="hidden" value="someclientid"></input>
        <input name="accept" type="hidden" value="Allow"></input>

        <div class="initial_form">
          <button type="button" name="deny" class="button medium grey">
            <span>Cancel and go back</span>
          </button>
          <button type="button" name="allow" class="button medium blue">
            <span>Login</span>
          </button>
        </div>
        <div class="secondary_form" >
          <label for="username">Username</label>
          <input name="username" type="text" placeholder="Username" value=""></input>
          <label for="password">Password</label>
          <input name="password" type="password" placeholder="Password"></input>
          <button type="submit" name="confirm" class="button medium blue">
            <span>Confirm</span>
          </button>
        </div>
      </form>
    </section>
  </body>
</html>
      ApiResponseMessage(200, html.toString)
    }
  }
}
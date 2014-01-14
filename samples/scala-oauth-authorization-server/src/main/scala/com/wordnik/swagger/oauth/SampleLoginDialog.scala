package com.wordnik.swagger.auth.service

import com.wordnik.swagger.auth.model._

import org.apache.oltu.oauth2.as.issuer.{ MD5Generator, OAuthIssuerImpl }

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.util.Date
import java.net.URLEncoder
import java.net.URI

class DefaultAuthDialog extends AuthDialog with TokenCache {
  /**
   * In this sample, the scope 'anonymous' will allow access if the redirectUri
   * is 'localhost' and provide an AnonymousTokenRequest, which is good for 3600
   * seconds
   */
  def show(clientId: String, redirectUri: String, scope: String) = {
    if(scope == "anonymous") {
      val url = "/oauth/login"

      if(redirectUri.startsWith("http://localhost")) {
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
    <link rel="stylesheet" href="/css/auth.css"></link>
    <script src="/lib/jquery-1.8.0.min.js"></script>
    <script src="/js/auth.js"></script>
  </head>
  <body id = "oauth-authorize" class = "oauth-page">
    <header>
      <img class="main_image" src="/images/dog.png"></img>
      <h1></h1>
      <p class="oauth_body title">
        <strong>Hello, <a href="#">Swagger Petstore</a>&nbsp;would like to you log in.</strong>
      </p>
      <p class="oauth_body">        
        This is a sample OAuth2 server which supports the Implicit or Client-side flow.  You can find 
        the source code to the server <a href="https://github.com/wordnik/swagger-core/tree/master/samples/scala-oauth-authorization-server">here</a>.
      </p>
      <p class="oauth_body">        
        Since we don't really keep track of usernames in this sample server, you can enter any non-blank username
        and password to simulate a successful login.  An empty username or password will be treated like a
        login failure.
      </p>
    </header>
    <section class = "email_form">
      <form class="confirm" method="POST" id="oauth-authorize-login" action={"/oauth/login?redirect_uri=" + redirectUri}>
        <input name="scope" id="scope" type="hidden" value="email"></input>
        <input name="client_id" id="client_id" type="hidden" value="someclientid"></input>
        <input name="accept" id="accept" type="hidden" value="Allow"></input>

        <div class="oauth_submit initial_form">
          <button type="button" id="deny" class="button medium grey">
            <span>Cancel and go back</span>
          </button>
          <button type="button" id="allow" class="button medium blue">
            <span>Allow</span>
          </button>
        </div>
        <div class="oauth_submit secondary_form" >
          <label for="username">Username</label>
          <input name="username" type="text" placeholder="Username" value=""></input>
          <label for="password">Password</label>
          <input name="password" type="password" placeholder="Password"></input>
          <button type="submit" id="login" name="login" class="button medium blue">
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

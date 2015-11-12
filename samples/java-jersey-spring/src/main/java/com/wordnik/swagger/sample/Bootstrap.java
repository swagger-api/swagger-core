package com.wordnik.swagger.sample;

import com.wordnik.swagger.model.*;
import com.wordnik.swagger.config.*;
import com.wordnik.swagger.sample.util.*;
import com.wordnik.swagger.config.FilterFactory;

import javax.servlet.http.HttpServlet;

import java.util.List;
import java.util.ArrayList;

public class Bootstrap extends HttpServlet {
  static {
    List<AuthorizationScope> scopes = new ArrayList<AuthorizationScope>();
    scopes.add(new AuthorizationScope("email", "Access to your email address"));
    scopes.add(new AuthorizationScope("pets", "Access to your pets"));

    List<GrantType> grantTypes = new ArrayList<GrantType>();

    ImplicitGrant implicitGrant = new ImplicitGrant(
      new LoginEndpoint("http://localhost:8002/oauth/dialog"), 
      "access_code");

    grantTypes.add(implicitGrant);

    AuthorizationType oauth = new OAuthBuilder().scopes(scopes).grantTypes(grantTypes).build();

    ConfigFactory.config().addAuthorization(oauth);
  }
}

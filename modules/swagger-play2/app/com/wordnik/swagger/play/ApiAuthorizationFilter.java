package com.wordnik.swagger.play;

import com.wordnik.swagger.core.AuthorizationFilter;

import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.UriInfo;

public interface ApiAuthorizationFilter extends AuthorizationFilter {
    public boolean authorize(String apiPath, String method, HttpHeaders headers, UriInfo uriInfo);
    public boolean authorizeResource(String apiPath, HttpHeaders headers, UriInfo uriInfo);
}

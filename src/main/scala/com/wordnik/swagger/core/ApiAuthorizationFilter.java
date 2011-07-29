package com.wordnik.swagger.core;

import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.UriInfo;

/**
 * @author deepak
 * @since 7/5/11 12:26 PM
 */
public interface ApiAuthorizationFilter {

    public boolean authorize(String apiPath, HttpHeaders headers, UriInfo uriInfo);
}

package com.wordnik.swagger.sample.resource;

import javax.ws.rs.*;

import com.wordnik.swagger.jaxrs.JavaHelp;
import com.wordnik.swagger.core.Api;

import com.sun.jersey.spi.resource.Singleton;

@Path("/resources/user")
@Api(value = "/user",
  description = "Operations about users",
  listingPath = "/resources/user",
  listingClass = "com.wordnik.swagger.sample.resource.UserResourceJSONXML")
@Singleton
@Produces({"application/json", "application/xml"})
public class UserResourceListing extends JavaHelp {}

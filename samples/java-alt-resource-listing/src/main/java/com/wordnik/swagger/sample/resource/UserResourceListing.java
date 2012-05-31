package com.wordnik.swagger.sample.resource;

import com.wordnik.swagger.annotations.*;
import com.wordnik.swagger.jaxrs.JavaHelp;

import com.sun.jersey.spi.resource.Singleton;

import javax.ws.rs.*;

@Path("/resources/user")
@Api(value = "/user",
  description = "Operations about users",
  listingPath = "/resources/user",
  listingClass = "com.wordnik.swagger.sample.resource.UserResourceJSONXML")
@Singleton
@Produces({"application/json", "application/xml"})
public class UserResourceListing extends JavaHelp {}

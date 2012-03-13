package com.wordnik.swagger.sample.resource;

import javax.ws.rs.*;

import com.wordnik.swagger.jaxrs.JavaHelp;
import com.wordnik.swagger.core.Api;

import com.sun.jersey.spi.resource.Singleton;

@Path("/resources/pet")
@Api(value = "/pet",
  description = "Operations about pets",
  listingPath = "/resources/pet",
  listingClass = "com.wordnik.swagger.sample.resource.PetResourceJSONXML")
@Singleton
@Produces({"application/json", "application/xml"})
public class PetResourceListing extends JavaHelp {}


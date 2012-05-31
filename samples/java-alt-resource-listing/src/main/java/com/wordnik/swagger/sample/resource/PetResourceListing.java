package com.wordnik.swagger.sample.resource;

import com.wordnik.swagger.annotations.*;
import com.wordnik.swagger.jaxrs.JavaHelp;

import com.sun.jersey.spi.resource.Singleton;

import javax.ws.rs.*;

@Path("/resources/pet")
@Api(value = "/pet",
  description = "Operations about pets",
  listingPath = "/resources/pet",
  listingClass = "com.wordnik.swagger.sample.resource.PetResourceJSONXML")
@Singleton
@Produces({"application/json", "application/xml"})
public class PetResourceListing extends JavaHelp {}


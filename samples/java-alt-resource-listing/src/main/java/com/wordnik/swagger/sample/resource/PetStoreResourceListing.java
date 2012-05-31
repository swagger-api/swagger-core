package com.wordnik.swagger.sample.resource;

import javax.ws.rs.*;

import com.wordnik.swagger.annotations.*;

import com.sun.jersey.spi.resource.Singleton;

import com.wordnik.swagger.jaxrs.JavaHelp;

@Path("/resources/store")
@Api(value = "/store",
  description = "Operations about pet stores",
  listingPath = "/resources/store",
  listingClass = "com.wordnik.swagger.sample.resource.PetStoreResourceJSONXML")
@Singleton
@Produces({"application/json", "application/xml"})
public class PetStoreResourceListing extends JavaHelp {}

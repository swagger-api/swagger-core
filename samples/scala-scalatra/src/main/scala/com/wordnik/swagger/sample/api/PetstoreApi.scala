package com.wordnik.swagger.sample.api 

import com.wordnik.swagger.sample.data._
import com.wordnik.swagger.sample.model._

import org.scalatra._
import org.scalatra.scalate.ScalateSupport
import org.scalatra.swagger._
import org.scalatra.json.{ JValueResult, JacksonJsonSupport }

import org.json4s._
import org.json4s.JsonDSL._


case class SomethingNeat(message: String)

class PetstoreApi(implicit val swagger: Swagger) extends ScalatraServlet
  with SwaggerSupport
  with ScalateSupport
  with JacksonJsonSupport {

  protected implicit val jsonFormats: Formats = DefaultFormats
  /* set the name of this api as "pet" in the Resource Listing */
  override protected val applicationName: Option[String] = Some("pet")

  /* description for the resource listing */
  protected val applicationDescription: String = "Pet API"

  before() {
    contentType = formats("json")
    response.headers += ("Access-Control-Allow-Origin" -> "*")
  }

  get("/:petId", operation(
    apiOperation[Pet]
      ("getPetById")
      .summary("Find pet by ID")
      .notes("Returns a pet based on ID")
      .parameter(pathParam[String]("petId").description("ID of pet that needs to be fetched")))) {
    params.getAs[Int]("petId") match {
      case Some(id) => PetData.getPetById(id)
      case _ => halt(400, "invalid pet id specified")
    }
  }

  post("/", operation(
    apiOperation[Unit]("addPet")
    .summary("Add a new pet tot he store")
    .parameter(bodyParam[Pet]("body").description("Pet object to add to the store").required))) {
    parse(request.body).extractOpt[Pet] match {
      case Some(pet) => PetData.addPet(pet)
      case _ => halt(400, "invalid pet supplied, not added")
    }
  }

  put("/", operation(
    apiOperation[Unit]("updatePet")
    .summary("Update an existing pet")
    .parameter(bodyParam[Pet]("body").description("Pet object to update in the store").required))) {
    parse(request.body).extractOpt[Pet] match {
      case Some(pet) => PetData.addPet(pet)
      case _ => halt(400, "invalid pet supplied, not updated")
    }
  }

  get("/findByStatus", operation(
    apiOperation[List[Pet]]("findPetsByStatus")
    .summary("Finds Pets by status")
    .parameter(queryParam[String]("status")
    .allowableValues(List("available", "pending", "sold"))
    .description("Status values that need to be considered for filter").required))) {
    params.getAs[String]("status") match {
      case Some(status) => PetData.findPetByStatus(status)
      case _ => halt(400, "invalid stauts supplied")
    }
  }

  get("/findByTags", operation(
    apiOperation[List[Pet]]("findPetsByStatus")
    .summary("Finds Pets by tags")
    .parameter(queryParam[String]("tags")
    .description("Tags to filter by").required))) {
    params.getAs[String]("tags") match {
      case Some(tags) => PetData.findPetByTags(tags)
      case _ => halt(400, "invalid stauts supplied")
    }
  }

  notFound {
    // remove content type in case it was set through an action
    contentType = null
    // Try to render a ScalateTemplate if no route matched
    findTemplate(requestPath) map { path =>
      contentType = "text/html"
      layoutTemplate(path)
    } orElse serveStaticResource() getOrElse resourceNotFound()
  }
}

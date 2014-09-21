package test

import com.wordnik.swagger.core._
import com.wordnik.swagger.model._

import org.json4s._
import org.json4s.JsonDSL._
import org.json4s.jackson.JsonMethods._
import org.json4s.jackson.Serialization.{read, write}

import org.specs2.mutable._

import play.api.test._
import play.api.test.Helpers._

import scala.io._
import scala.collection.JavaConverters._

class IntegrationSpec extends Specification {
  implicit val formats = SwaggerSerializers.formats

  "Application" should {    
    "have the proper resource metadata" in {
      running(TestServer(3333)) {
        val json = Source.fromURL("http://localhost:3333/api-docs").mkString
        val doc = parse(json).extract[ResourceListing]
        doc.swaggerVersion must_==("1.2")
      }
    }

    "contain all apis defined in the routes without api key" in {
      running(TestServer(3333)) {
        val json = Source.fromURL("http://localhost:3333/api-docs").mkString
        val doc = parse(json).extract[ResourceListing]
        doc.apis.size must_==(3)
        (doc.apis.map(_.path).toSet &
          Set(
            "/admin",
            "/pet",
            "/user")
          ).size must_==(3)
      }
    }

    "contain all operations defined in the pet resource without api key" in {
      running(TestServer(3333)) {
        val json = Source.fromURL("http://localhost:3333/api-docs/pet").mkString
        val doc = parse(json).extract[ApiListing]
        (doc.models.get.keys.toSet &
          Set(
            "Category",
            "Tag",
            "Pet")
          ).size must_==(3)
      }
    }

    "contain models without api key" in {
      running(TestServer(3333)) {
      val json = Source.fromURL("http://localhost:3333/api-docs/pet").mkString
        val doc = parse(json).extract[ApiListing]
        (doc.apis.map(_.path).toSet &
          Set(
            "/pet/findByTags",
            "/pet/findByStatus",
            "/pet/{id}")
          ).size must_==(3)
      }
    }

    "no apis from store resource without valid api key" in {
      running(TestServer(3333)) {
        val json = Source.fromURL("http://localhost:3333/api-docs/store").mkString
        val doc = parse(json).extract[ApiListing]
        Option(doc.apis) must_==(Some(List.empty))
      }
    }
    
    "no models from store resource without valid api key" in {
      running(TestServer(3333)) {
        val json = Source.fromURL("http://localhost:3333/api-docs/store").mkString
        val doc = parse(json).extract[ApiListing]
        doc.models must_==None
      }
    }

    "contain apis from store resource valid api key" in {
      running(TestServer(3333)) {
        val json = Source.fromURL("http://localhost:3333/api-docs/store?api_key=special-key").mkString
        val doc = parse(json).extract[ApiListing]
        doc.apis.map(_.path).size must_==(2)
      }
    }

    "contain correct models from store resource valid api key" in {
      running(TestServer(3333)) {
        val json = Source.fromURL("http://localhost:3333/api-docs/store?api_key=special-key").mkString
        val doc = parse(json).extract[ApiListing]
        doc.models.get.keys.toSet.size must_==(1)
      }
    }

    "contain all operations defined in the pet resource with api key" in {
      running(TestServer(3333)) {
        val json = Source.fromURL("http://localhost:3333/api-docs/pet?api_key=special-key").mkString
        val doc = parse(json).extract[ApiListing]
        (doc.apis.map(_.path).toSet &
          Set(
            "/pet/findByTags",
            "/pet/findByStatus",
            "/pet/{id}")
          ).size must_==(3)
      }
    }
  }
}
package test

import com.wordnik.swagger.core._
import com.wordnik.swagger.core.util.JsonUtil

import org.specs2.mutable._

import play.api.test._
import play.api.test.Helpers._

import scala.io._
import scala.collection.JavaConverters._

class IntegrationSpec extends Specification {
	val mapper = JsonUtil.getJsonMapper

  "Application" should {    
    "have the proper resource metadata" in {
      running(TestServer(3333)) {
      	val json = Source.fromURL("http://localhost:3333/api-docs.json").mkString
      	val doc = mapper.readValue(json, classOf[Documentation])
      	doc.swaggerVersion must_==("1.1")
      }
    }

    "contain all apis defined in the routes" in {
    	running(TestServer(3333)) {
      	val json = Source.fromURL("http://localhost:3333/api-docs.json").mkString
      	val doc = mapper.readValue(json, classOf[Documentation])

		    doc.getApis.size must_==(4)
		    (doc.getApis.asScala.map(_.getPath).toSet &
		      Set(
		      	"/api-docs.{format}/admin",
		        "/api-docs.{format}/pet",
		        "/api-docs.{format}/store",
		        "/api-docs.{format}/user")
		      ).size must_==(4)
      }
    }

    "contain all operations defined in the pet resource without api key" in {
    	running(TestServer(3333)) {
      	val json = Source.fromURL("http://localhost:3333/api-docs.json/pet").mkString
      	val doc = mapper.readValue(json, classOf[Documentation])

		    (doc.getApis.asScala.map(_.getPath).toSet &
		      Set(
		      	"/pet.{format}/findByTags",
		        "/pet.{format}/findByStatus",
		        "/pet.{format}/{petId}")
		      ).size must_==(3)
      }
    }

    "contain all operations defined in the pet resource with api key" in {
    	running(TestServer(3333)) {
      	val json = Source.fromURL("http://localhost:3333/api-docs.json/pet?api_key=special-key").mkString
      	val doc = mapper.readValue(json, classOf[Documentation])

		    (doc.getApis.asScala.map(_.getPath).toSet &
		      Set(
		      	"/pet.{format}/findByTags",
		        "/pet.{format}/findByStatus",
		        "/pet.{format}/{petId}",
		        "/pet.{format}")
		      ).size must_==(4)
      }
    }
  }
}
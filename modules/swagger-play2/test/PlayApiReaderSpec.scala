package test

import play.modules.swagger._

import org.specs2.mutable._

import play.api.test._
import play.api.test.Helpers._

class PlayApiReaderSpec extends Specification {
	"PlayApiReader" should {
		"convert a simple play route comment" in {
			val path = """/pet.json/$id<[^/]+>/test/$nothing<[^/]+>"""
			SwaggerUtils.convertPathString(path) must be_== ("""/pet.json/{id}/test/{nothing}""")
		}
		"convert a play route comment with numeric matchers" in {
			val path = """/pet.json/$id<[0-9]+>/test/$nothing<[^/]+>"""
			SwaggerUtils.convertPathString(path) must be_== ("""/pet.json/{id}/test/{nothing}""")
		}
		"convert a play route comment with string matchers" in {
			val path = """/pet.json/$id<[A-Z]+>/test/$nothing<[^/]+>"""
			SwaggerUtils.convertPathString(path) must be_== ("""/pet.json/{id}/test/{nothing}""")
		}
		"convert a play route comment with complex matchers" in {
			val path = """/pet.json/$id<[A-Z0-9]+>/test/$nothing<[^/]+>"""
			SwaggerUtils.convertPathString(path) must be_== ("""/pet.json/{id}/test/{nothing}""")
		}
	}
}
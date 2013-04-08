package com.wordnik.swagger.sample

import org.scalatra.test.specs2._

// For more on Specs2, see http://etorreborre.github.com/specs2/guide/org.specs2.guide.QuickStart.html
class PetstoreApiSpec extends ScalatraSpec { def is =
  "GET / on PetstoreApi"                     ^
    "should return status 200"                  ! root200^
                                                end

  addServlet(classOf[PetstoreApi], "/*")

  def root200 = get("/") {
    status must_== 200
  }
}

package matchers

import org.scalatest.matchers.MatchResult
import org.scalatest.matchers.Matcher

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.node.ObjectNode
import com.wordnik.swagger.util._

trait SerializationMatchers {

  class SerializeToStringMatcher(str: String, mapper: ObjectMapper) extends Matcher[Object] {
    def apply(left: Object) = {
      val lhs = mapper.convertValue(left, classOf[ObjectNode])
      val rhs = mapper.readValue(str, classOf[ObjectNode])
      MatchResult(
        lhs.equals(rhs),
        s"""Serialized object:\n$lhs\ndoes not equal expected serialized string:\n$rhs"""",
        s"""Serialized object equals expected serialized string""""
      )
    }
  }

  def serializeToYaml(yamlStr: String) = new SerializeToStringMatcher(yamlStr, Yaml.mapper())
  def serializeToJson(jsonStr: String) = new SerializeToStringMatcher(jsonStr, Json.mapper())
}

object SerializationMatchers extends SerializationMatchers
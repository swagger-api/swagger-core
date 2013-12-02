package com.wordnik.resource.util

import com.wordnik.swagger.annotations._
import com.wordnik.util.perf.{ ProfileCounter, Profile }

import javax.ws.rs.{ DefaultValue, QueryParam, Path, GET }
import javax.ws.rs.core.Response

import scala.collection.mutable.ListBuffer
import scala.collection.JavaConversions._

trait ProfileEndpointTrait {
  @GET
  @ApiOperation(value = "Gets current profile data", response = classOf[ProfileCounter], responseContainer="List")
  @Path("/profile")
  def getProfile(
    @ApiParam(value = "Filter to sort by")@QueryParam("filter") filter: String,
    @ApiParam(value = "Field to sort by", allowableValues = "name,count,totalDuration,minDuration,avgDuration,maxDuration", defaultValue = "name")@QueryParam("sortBy") sortBy: String = "name",
    @ApiParam(value = "Sort direction", allowableValues = "asc,desc", defaultValue = "asc")@QueryParam("sortOrder") sortOrder: String = "asc",
    @ApiParam(value = "Resets the profile information", allowableValues = "false,true", defaultValue = "false")@DefaultValue("false")@QueryParam("reset") action: String): Response = {
    action match {
      case s: String if ("true" == s || "" == s) => Profile.reset
      case _ =>
    }

    val f = { if (null == filter) None else Some(filter) }
    val data = getProfileCounters(f, Some(sortBy), Some(sortOrder))
    Response.ok.entity(data.toArray).build
  }

  def getProfileCounters(filter: Option[String], sortByString: Option[String], sortOrderString: Option[String]): List[ProfileCounter] = {
    val counters = Profile.getCounters(filter)
    // name,count,totalDuration,minDuration,avgDuration,maxDuration
    var sorter = new ListBuffer[Tuple7[String, Long, Long, Long, Double, Double, ProfileCounter]]
    counters.foreach(counter => {
      sorter += new Tuple7(counter.key, counter.count, counter.totalDuration, counter.minDuration, counter.avgDuration, counter.maxDuration, counter)
    })
    var output = new ListBuffer[ProfileCounter]
    sorter = sortByString.get match {
      case "name" => {
        sortOrderString.get match {
          case "desc" => sorter sortWith (_._1 > _._1)
          case _ => sorter sortWith (_._1 < _._1)
        }
      }
      case "count" => {
        sortOrderString.get match {
          case "desc" => sorter sortWith (_._2 > _._2)
          case _ => sorter sortWith (_._2 < _._2)
        }
      }
      case "totalDuration" => {
        sortOrderString.get match {
          case "desc" => sorter sortWith (_._3 > _._3)
          case _ => sorter sortWith (_._3 < _._3)
        }
      }
      case "minDuration" => {
        sortOrderString.get match {
          case "desc" => sorter sortWith (_._4 > _._4)
          case _ => sorter sortWith (_._4 < _._4)
        }
      }
      case "avgDuration" => {
        sortOrderString.get match {
          case "desc" => sorter sortWith (_._5 > _._5)
          case _ => sorter sortWith (_._5 < _._5)
        }
      }
      case "maxDuration" => {
        sortOrderString.get match {
          case "desc" => sorter sortWith (_._6 > _._6)
          case _ => sorter sortWith (_._6 < _._6)
        }
      }
      case _ => sorter
    }
    sorter.foreach(o => output += o._7)
    output.toList
  }
}

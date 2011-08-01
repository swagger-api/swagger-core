package com.wordnik.swagger.core.util

import java.text.SimpleDateFormat
import java.util.Date

trait RestResourceUtil {
  def getInt(minVal: Int, maxVal: Int, defaultValue: Int, inputString: String): Int = {
    var output: Int = defaultValue;
    try output = inputString.toInt
    catch {
      case _ => output = defaultValue
    }

    if (output < minVal) output = minVal
    if (maxVal == -1) { if (output < minVal) output = minVal }
    else if (output > maxVal) output = maxVal
    output
  }

  def getLong(minVal: Long, maxVal: Long, defaultValue: Long, inputString: String): Long = {
    var output: Long = defaultValue;
    try output = inputString.toLong
    catch {
      case _ => output = defaultValue
    }

    if (output < minVal) output = minVal
    if (maxVal == -1) { if (output < minVal) output = minVal }
    else if (output > maxVal) output = maxVal
    output
  }

  def getDouble(minVal: Double, maxVal: Double, defaultValue: Double, inputString: String): Double = {
    var output: Double = defaultValue;
    try output = inputString.toDouble
    catch {
      case _ => output = defaultValue
    }

    if (output < minVal) output = minVal
    if (maxVal == -1) { if (output < minVal) output = minVal }
    else if (output > maxVal) output = maxVal
    output
  }

  def getBoolean(defaultValue: Boolean, booleanString: String): Boolean = {
    var output: Boolean = defaultValue
    if (booleanString == null) output = defaultValue

    //	treat "", "YES" as "true"
    if ("".equals(booleanString)) output = true
    else if ("YES".equalsIgnoreCase(booleanString)) output = true
    else if ("NO".equalsIgnoreCase(booleanString)) output = false
    else {
      try output = booleanString.toBoolean
      catch {
        case _ => output = defaultValue
      }
    }
    output
  }
  
  def getDate(defaultValue:Date, dateString:String):Date = {
    try new SimpleDateFormat("yyyy-MM-dd").parse(dateString)
    catch{
      case _ => defaultValue
    }
  }
}
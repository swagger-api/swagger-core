package com.wordnik.resource.util

import com.wordnik.util.perf.Profile

trait Profiler {
  def profile[T](f: => T): T = {
    val name: String = getClass.getName + "." + Thread.currentThread.getStackTrace()(3).getMethodName
    Profile(name, f)
  }

  def profile[T](name:String, f: => T): T = Profile(name, f)
}

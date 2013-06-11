package com.wordnik.swagger.servlet.listing

import com.wordnik.swagger.config._
import com.wordnik.swagger.model._
import com.wordnik.swagger.reader._
import com.wordnik.swagger.core.util.ReaderUtil

object ApiListingCache extends ReaderUtil {
  var cache: Option[Map[String, com.wordnik.swagger.model.ApiListing]] = None

  def listing(docRoot: String): Option[Map[String, com.wordnik.swagger.model.ApiListing]] = {
    cache.orElse{
      ClassReaders.reader.map{reader => 
        ScannerFactory.scanner.map(scanner => {
          val classes = scanner match {
            case scanner: Scanner => scanner.classes()
            case _ => List()
          }
          val listings = (for(cls <- classes) yield reader.read(docRoot, cls, ConfigFactory.config)).flatten
          // println("listings: " + listings)
          val mergedListings = groupByResourcePath(listings)
          println("merged listings: " + mergedListings.map(m => (m.resourcePath, m)).toMap)
          cache = Some(mergedListings.map(m => (m.resourcePath, m)).toMap)
        })
      }
      cache
    }
  }
}

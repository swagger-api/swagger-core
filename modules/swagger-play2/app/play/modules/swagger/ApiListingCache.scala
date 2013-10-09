package play.modules.swagger

import com.wordnik.swagger.config._
import com.wordnik.swagger.reader._
import com.wordnik.swagger.core.util.ReaderUtil
import play.api.Logger

object ApiListingCache extends ReaderUtil {
  var cache: Option[Map[String, com.wordnik.swagger.model.ApiListing]] = None

  def listing(docRoot: String): Option[Map[String, com.wordnik.swagger.model.ApiListing]] = {
    cache.orElse{
      Logger("swagger").info("Loading API metadata")
      ClassReaders.reader.map{reader =>
        ScannerFactory.scanner.map(scanner => {
          val classes = scanner match {
            case scanner: Scanner => scanner.classes()
            case _ => List()
          }
          Logger("swagger").debug("Classes count: %s".format(classes.length))
          classes.foreach{ clazz =>
            Logger("swagger").debug("Controller: %s".format(clazz.getName))
          }
          val listings = (for(cls <- classes) yield reader.read(docRoot, cls, ConfigFactory.config)).flatten
          val mergedListings = groupByResourcePath(listings)
          cache = Some(mergedListings.map(m => (m.resourcePath, m)).toMap)
        })
      }
      cache
    }
  }
}
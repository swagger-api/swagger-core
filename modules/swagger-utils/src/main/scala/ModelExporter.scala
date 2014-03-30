import com.wordnik.swagger.converter._
import com.wordnik.swagger.core.util.JsonSerializer

import org.reflections.Reflections
import org.reflections.scanners._
import org.reflections.util._

import java.io._

import scala.collection.JavaConverters._

object ModelExporter {
  def main(args: Array[String]) = {
    val packageName = args match {
      case i if(i.length > 0) => i(0)
      case _ => "com.wordnik"
    }
    val models = (for(model <- classesForPackage(packageName)) yield {
      try{
        ModelConverters.read(model) match {
          case Some(model) => {
            if(model.name.indexOf("$") == -1) Some(model)
            else None
          }
          case _ => None
        }
      }
      catch {
        case e: Exception => None
      }
    }).flatten.toList
    if(args.length > 1) {
      val writer = new PrintWriter(args(1), "UTF-8")
      val jsons = models.map(m => (m.id, m)).toMap

      writer.println(JsonSerializer.asJson(jsons))
      writer.close()
    }
    else models.map(m => {println(JsonSerializer.asJson(m))})
  }

  def classesForPackage(packageName: String): List[Class[_]] = {
    val cls = this.getClass.getClassLoader
    val reflections = new Reflections(new ConfigurationBuilder()
      .setScanners(new SubTypesScanner(false), new ResourcesScanner())
      .setUrls(ClasspathHelper.forPackage(packageName))
      .filterInputsBy(new FilterBuilder().include(FilterBuilder.prefix(packageName)))
    )
    val allClasses = reflections.getSubTypesOf(classOf[java.lang.Object])
    allClasses.asScala.toList
  }
}

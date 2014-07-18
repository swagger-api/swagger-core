package play.modules.swagger

import com.wordnik.swagger.model.ModelProperty
import play.api.mvc.RequestHeader

trait ApiDynamicBuilder {
  def build(requestHeader: RequestHeader): List[ModelProperty]
}

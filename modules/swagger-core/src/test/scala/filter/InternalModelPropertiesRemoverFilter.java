package filter;

import java.util.List;
import java.util.Map;

import com.wordnik.swagger.core.filter.AbstractSpecFilter;
import com.wordnik.swagger.models.Model;
import com.wordnik.swagger.models.properties.Property;

/**
 * Sample filter to model properties starting with "_" unless a header
 * "super-user" is passed
 */
public class InternalModelPropertiesRemoverFilter extends AbstractSpecFilter {
  @Override
  public boolean isPropertyAllowed(
    Model model,
    Property property,
    String propertyName,
    Map<String, List<String>> params,
    Map<String, String> cookies,
    Map<String, List<String>> headers) {
    if(propertyName.startsWith("_")) {
      if(headers != null && headers.containsKey("super-user"))
        return true;
      return false;
    }
    else
      return true;
  }
}

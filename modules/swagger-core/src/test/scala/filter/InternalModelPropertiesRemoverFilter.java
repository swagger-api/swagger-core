package filter;

import com.wordnik.swagger.util.Json;
import com.wordnik.swagger.core.filter.*;

import com.wordnik.swagger.models.*;
import com.wordnik.swagger.models.properties.*;
import com.wordnik.swagger.model.ApiDescription;

import java.util.*;

/**
 * Sample filter to model properties starting with "_" unless a header
 * "super-user" is passed
 **/
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
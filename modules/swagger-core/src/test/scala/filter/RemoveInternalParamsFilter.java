package filter;

import com.wordnik.swagger.util.Json;
import com.wordnik.swagger.core.filter.*;

import com.wordnik.swagger.models.*;
import com.wordnik.swagger.models.parameters.*;
import com.wordnik.swagger.model.ApiDescription;

import java.util.*;

/**
 * Sample filter to parameters if "internal" has been set and the header
 * "super-user" is not passed
 **/
public class RemoveInternalParamsFilter extends AbstractSpecFilter {
  @Override
  public boolean isParamAllowed(
    Parameter parameter,
    Operation operation,
    ApiDescription api,
    Map<String, List<String>> params,
    Map<String, String> cookies,
    Map<String, List<String>> headers) {
    if(parameter.getDescription() != null
      && parameter.getDescription().startsWith("secret:")) {
      if(headers != null) {
         if(headers.containsKey("super-user"))
           return true;
      }
      return false;
    }
    return true;
  }
}
package filter;

import com.wordnik.swagger.core.filter.*;

import com.wordnik.swagger.models.*;
import com.wordnik.swagger.models.parameters.*;
import com.wordnik.swagger.model.ApiDescription;

import java.util.*;

/**
 * Sample filter to avoid all resources for the /user resource
 **/
public class NoUserOperationsFilter extends AbstractSpecFilter {
  @Override
  public boolean isOperationAllowed(
    Operation operation,
    ApiDescription api,
    Map<String, List<String>> params,
    Map<String, String> cookies,
    Map<String, List<String>> headers) {
    if(api.getPath().startsWith("/user"))
      return false;
    return true;
  }
}
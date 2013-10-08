package security;

import com.wordnik.swagger.model.*;
import com.wordnik.swagger.core.filter.SwaggerSpecFilter;

import org.slf4j.*;

import java.util.Map;
import java.util.List;

public class AuthorisationFilter implements SwaggerSpecFilter {
    static Logger logger = LoggerFactory.getLogger("swagger");

    public boolean isOperationAllowed(Operation operation, ApiDescription api, Map<String, List<String>> params, Map<String, String> cookies, Map<String, List<String>> headers) {
        boolean isAuthorized = checkKey(params, headers);
        if (isAuthorized) {
            return true;
        } else {
            if (operation.method().equals("GET")) {
                if (api.path().startsWith("/store")) {
                    return false;
                } else {
                    return true;
                }
            } else {
                return false;
            }
        }
    }

    public boolean isParamAllowed(Parameter parameter, Operation operation, ApiDescription api, Map<String, List<String>> params, Map<String, String> cookies, Map<String, List<String>> headers) {
        boolean isAuthorized = checkKey(params, headers);
        if ((parameter.paramAccess().isDefined() && parameter.paramAccess().get().equals("internal")) && !isAuthorized)
            return false;
        else
            return true;
    }

    public boolean checkKey(Map<String, List<String>> params, Map<String, List<String>> headers) {
        String keyValue = null;
        if (params.containsKey("api_key"))
            keyValue = params.get("api_key").get(0);
        else {
            if (headers.containsKey("api_key"))
                keyValue = headers.get("api_key").get(0);
        }
        if ("special-key".equals(keyValue))
            return true;
        else
            return false;
    }
}
package io.swagger.filter;

import io.swagger.core.filter.AbstractSpecFilter;
import io.swagger.models.Model;

import java.util.List;
import java.util.Map;

/**
 * signals to remove unreferenced definitions.
 **/
public class DefinitionAllowedFilter extends AbstractSpecFilter {

    @Override
    public boolean isDefinitionAllowed(Model model, Map<String, List<String>> params, Map<String, String> cookies, Map<String, List<String>> headers) {
        if (model.getProperties() != null && model.getProperties().containsKey("hidden")) {
            return false;
        }
        return super.isDefinitionAllowed(model, params, cookies, headers);
    }

}
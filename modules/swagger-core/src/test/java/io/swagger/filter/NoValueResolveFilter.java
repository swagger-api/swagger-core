package io.swagger.filter;

import io.swagger.core.filter.AbstractSpecFilter;

/**
 * Sample filter if there is no value to resolve to.
 **/
public class NoValueResolveFilter extends AbstractSpecFilter {

    @Override
    public boolean isRemovingUnreferencedDefinitions() {
        return true;
    }
}

package io.swagger.v3.core.filter.resources;

import io.swagger.v3.core.filter.AbstractSpecFilter;

/**
 * signals to remove unreferenced definitions.
 **/
public class RemoveUnreferencedDefinitionsFilter extends AbstractSpecFilter {
    @Override
    public boolean isRemovingUnreferencedDefinitions() {
        return true;
    }
}
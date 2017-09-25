package io.swagger.filter.resources;

import io.swagger.core.filter.AbstractSpecFilter;

/**
 * signals to remove unreferenced definitions.
 **/
public class RemoveUnreferencedDefinitionsFilter extends AbstractSpecFilter {
    @Override
    public boolean isRemovingUnreferencedDefinitions() {
        return true;
    }
}
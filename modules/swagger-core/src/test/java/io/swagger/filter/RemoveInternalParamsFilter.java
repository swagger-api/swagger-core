package io.swagger.filter;

import io.swagger.core.filter.AbstractSpecFilter;
import io.swagger.model.ApiDescription;
import io.swagger.oas.models.Operation;
import io.swagger.oas.models.parameters.Parameter;

import java.util.List;
import java.util.Map;

/**
 * Sample filter to parameters if "internal" has been set and the header
 * "super-user" is not passed
 **/
public class RemoveInternalParamsFilter extends AbstractSpecFilter {
   
}
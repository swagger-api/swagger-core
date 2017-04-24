package io.swagger.resources.generics;

import io.swagger.models.Response;

/**
 * Created on 4/24/17
 *
 * @author Jason Bau (jbau@wavefront.com).
 */
public interface CrudInterface<T extends AbstractEntity> {
    Response doCreate(T entity) throws Exception;
}

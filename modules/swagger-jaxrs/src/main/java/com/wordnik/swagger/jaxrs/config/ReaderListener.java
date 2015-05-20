package com.wordnik.swagger.jaxrs.config;

import com.wordnik.swagger.jaxrs.Reader;
import com.wordnik.swagger.models.Swagger;

public interface ReaderListener {

    void beforeScan( Reader reader, Swagger swagger );

    void afterScan( Reader reader, Swagger swagger );
}

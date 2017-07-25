package io.swagger.oas.web;

import io.swagger.oas.models.OpenAPI;

public interface OASReader {
    OpenAPI read(OASConfig configuration, OASScanner scanner);
}

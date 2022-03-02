/**
 * Copyright 2021 SmartBear Software
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package io.swagger.v3.jaxrs2.integration;

import io.swagger.v3.oas.integration.StringOpenApiConfigurationLoader;
import io.swagger.v3.oas.integration.api.OpenAPIConfiguration;
import org.apache.commons.lang3.StringUtils;

import javax.servlet.ServletConfig;
import java.io.IOException;

public class ServletPathConfigurationLoader implements StringOpenApiConfigurationLoader {

    private ServletConfig servletConfig;

    public ServletPathConfigurationLoader(ServletConfig servletConfig) {
        this.servletConfig = servletConfig;
    }

    @Override
    public OpenAPIConfiguration load(String path) throws IOException {
        if (servletConfig == null) {
            return null;
        }
        if (StringUtils.isBlank(path)) {
            return null;
        }
        String sanitized = (path.startsWith("/") ? path : "/" + path);
        String configString = readInputStreamToString(servletConfig.getServletContext().getResourceAsStream(sanitized));
        return deserializeConfig(path, configString);
    }

    @Override
    public boolean exists(String path) {

        if (servletConfig == null) {
            return false;
        }
        if (StringUtils.isBlank(path)) {
            return false;
        }
        String sanitized = (path.startsWith("/") ? path : "/" + path);
        return servletConfig.getServletContext().getResourceAsStream(sanitized) != null;
    }
}

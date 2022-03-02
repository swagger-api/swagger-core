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

package io.swagger.v3.jaxrs2.integration.servlet;

import io.swagger.v3.jaxrs2.integration.JaxrsOpenApiContextBuilder;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.integration.IgnoredPackages;
import io.swagger.v3.oas.integration.OpenApiConfigurationException;
import io.swagger.v3.oas.integration.SwaggerConfiguration;

import javax.servlet.ServletContainerInitializer;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.HandlesTypes;
import javax.ws.rs.ApplicationPath;
import javax.ws.rs.Path;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.stream.Collectors;

/**
 *
 * @since 2.1.2
 */
@HandlesTypes({Path.class, OpenAPIDefinition.class, ApplicationPath.class})
public class SwaggerServletInitializer implements ServletContainerInitializer {

    static final Set<String> ignored = new HashSet();

    static {
        ignored.addAll(IgnoredPackages.ignored);
    }

    public SwaggerServletInitializer() {
    }

    @Override
    public void onStartup(Set<Class<?>> classes, ServletContext servletContext) throws ServletException {
        if (classes != null && ! classes.isEmpty()) {
            Set<Class<?>> resources = new LinkedHashSet();
            classes.stream()
                    .filter(c -> ignored.stream().noneMatch(i -> c.getName().startsWith(i)))
                    .forEach(resources::add);
            if (!resources.isEmpty()) {
                // init context
                try {
                    SwaggerConfiguration oasConfig = new SwaggerConfiguration()
                            .resourceClasses(resources.stream().map(Class::getName).collect(Collectors.toSet()));

                    new JaxrsOpenApiContextBuilder()
                            .openApiConfiguration(oasConfig)
                            .buildContext(true);
                } catch (OpenApiConfigurationException e) {
                    throw new RuntimeException(e.getMessage(), e);
                }
            }
        }
    }

}

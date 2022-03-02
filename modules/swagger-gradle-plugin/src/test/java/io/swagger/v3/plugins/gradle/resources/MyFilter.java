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

package io.swagger.v3.plugins.gradle.resources;

import io.swagger.v3.core.filter.AbstractSpecFilter;
import io.swagger.v3.core.model.ApiDescription;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.Operation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public class MyFilter extends AbstractSpecFilter {
        private static Logger LOGGER = LoggerFactory.getLogger(MyFilter.class);

        @Override
        public Optional<OpenAPI> filterOpenAPI(
                OpenAPI openAPI,
                Map<String, List<String>> params,
                Map<String, String> cookies,
                Map<String, List<String>> headers) {
            openAPI.getInfo().setTitle("UPDATEDBYFILTER");
            return Optional.of(openAPI);
            //  some processing
            //return super.filterOpenAPI(openAPI, params, cookies, headers);
        }

        @Override
        public Optional<io.swagger.v3.oas.models.Operation> filterOperation(
                Operation operation,
                ApiDescription api,
                Map<String, List<String>> params,
                Map<String, String> cookies,
                Map<String, List<String>> headers) {
            // some processing
            return Optional.of(operation);
        }

    }
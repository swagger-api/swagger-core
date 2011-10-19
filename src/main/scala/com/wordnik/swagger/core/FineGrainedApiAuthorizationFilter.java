/**
 *  Copyright 2011 Wordnik, Inc.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */

package com.wordnik.swagger.core;

import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.UriInfo;

/**
 * Implement this interface when the security API documents needs to be at individual input arguments
 * User: ramesh
 * Date: 10/18/11
 * Time: 6:38 AM
 */
public interface FineGrainedApiAuthorizationFilter extends AuthorizationFilter{

    public boolean authorizeOperation(String apiPath, DocumentationOperation operation, HttpHeaders headers, UriInfo uriInfo);

    public boolean authorizeResource(String apiPath, DocumentationEndPoint endpoint,  HttpHeaders headers, UriInfo uriInfo);

}

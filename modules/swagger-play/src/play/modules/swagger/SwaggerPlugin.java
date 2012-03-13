/**
 *  Copyright 2012 Wordnik, Inc.
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

package play.modules.swagger;

import play.Logger;
import play.PlayPlugin;
import play.mvc.Http.Request;
import play.mvc.Http.Response;
import play.mvc.Router;

import java.util.List;

/**
 * This plugin adds two new entries to play route table for swagger help api.<br/>
 * help.route key can be used to application.conf to specify base help api path. By default this is "/help"<br/>
 *
 * @author ayush
 * @since 10/7/11 3:19 PM
 */
public class SwaggerPlugin extends PlayPlugin {

    private static final String JSON = ".json";
    private static final String APPLICATION_JSON = "application/json";
    private static final String APPLICATION_XML = "application/xml";
    private static final String UTF_8 = "UTF-8";
    private static final String XML = ".xml";
    private static final String RESOURCES_XML = "/resources.xml";
    private static final String RESOURCES_JSON = "/resources.json";

    @Override
    public boolean rawInvocation(Request request, Response response) throws Exception {
//        Logger.info("Got request for " + request.path);

        try {
            final List<String> resourceNames = ApiHelpInventory.getResourceNames();
            for (String resourceName : resourceNames) {
                if (request.path.equals(resourceName + JSON)) {
                    response.contentType = APPLICATION_JSON;
                    String apiHelp = ApiHelpInventory.getPathHelpJson(resourceName);
                    response.out.write(apiHelp.getBytes(UTF_8));
                    return true;
                } else if (request.path.equals(resourceName + XML)) {
                    response.contentType = APPLICATION_XML;
                    String apiHelp = ApiHelpInventory.getPathHelpXml(resourceName);
                    response.out.write(apiHelp.getBytes(UTF_8));
                    return true;
                } else if (request.path.equals(RESOURCES_JSON)) {
                    response.contentType = APPLICATION_JSON;
                    String apiHelp = ApiHelpInventory.getRootHelpJson(resourceName);
                    response.out.write(apiHelp.getBytes(UTF_8));
                    return true;
                } else if (request.path.equals(RESOURCES_XML)) {
                    response.contentType = APPLICATION_XML;
                    String apiHelp = ApiHelpInventory.getRootHelpXml(resourceName);
                    response.out.write(apiHelp.getBytes(UTF_8));
                    return true;
                }

            }
        } catch (Exception e) {
            Logger.error(e, "Error in SwaggerPlugin");
        }

        return false;
    }

    @Override
    public void onApplicationStart() {
        final List<String> resourceNames = ApiHelpInventory.getResourceNames();

        if (resourceNames.size() > 0) {
            Router.prependRoute("GET", RESOURCES_XML, "ApiHelpController.catchAll");
            Router.prependRoute("GET", RESOURCES_JSON, "ApiHelpController.catchAll");
            Logger.info("Swagger: Added ROOT help api @ " + RESOURCES_JSON);
            Logger.info("Swagger: Added ROOT help api @ " + RESOURCES_XML);
            for (String resourceName : resourceNames) {
                Router.prependRoute("GET", resourceName + ".json", "ApiHelpController.catchAll");
                Router.prependRoute("GET", resourceName + ".xml", "ApiHelpController.catchAll");

                Logger.info("Swagger: Added help api @ " + resourceName + ".json");
                Logger.info("Swagger: Added help api @ " + resourceName + ".xml");
            }
        }
    }


    @Override
    public void afterApplicationStart() {
        Logger.info("afterApplicationStart");
    }
}

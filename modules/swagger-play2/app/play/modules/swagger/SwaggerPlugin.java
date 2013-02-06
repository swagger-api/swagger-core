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

import play.*;

public class SwaggerPlugin extends Plugin {
    private final Application application;
    
    public SwaggerPlugin(Application application) {
        this.application = application;
    }
    
    public void onStart() {
		Logger.debug("SwaggerPlugin.onStart");
		ApiHelpInventory.reload();
	}
    
    public void onStop() {
		Logger.debug("SwaggerPlugin.onStop");
	}
}
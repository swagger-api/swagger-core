package play.modules.swagger;

import play.*;

public class SwaggerPlugin extends Plugin {
    private final Application application;
    
    public SwaggerPlugin(Application application) {
        this.application = application;
    }
    
    public void onStart() {
		Logger.debug("SwaggerPlugin.onStart");
		ApiHelpInventory.clear();
		PlayApiReader.clear();
	}
    
    public void onStop() {
		Logger.debug("SwaggerPlugin.onStop");
	}

}